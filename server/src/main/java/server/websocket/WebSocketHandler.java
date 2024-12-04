package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.GsonServerMessage;
import websocket.GsonUserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccess dataAccess;

    public WebSocketHandler() {
        try {
            this.dataAccess = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        UserGameCommand command = GsonUserGameCommand.getGson().fromJson(msg, UserGameCommand.class);

        String username = null;
        try {
            username = dataAccess.getAuth(command.getAuthToken()).username();
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getAuthToken(), command.getGameID());
                case LEAVE -> leave(username, command.getAuthToken());
                case MAKE_MOVE -> makeMove(((MakeMoveCommand) command).getMove(), username, command.getAuthToken(), command.getGameID());
            }
        } catch (DataAccessException | NullPointerException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
            String message = GsonServerMessage.getGson().toJson(errorMessage);
            try {
                session.getRemote().sendString(message);
            } catch (IOException ex) {
                //woman shrug emoji
            }
        }

//        connections.saveSession(command.getGameID(), session);


    }

    private void connect(Session session, String username, String authToken, Integer gameID) {
        String key = username + authToken;

        try {
            String message;
            String currColor;
            GameData gameData = dataAccess.getGame(gameID);
            if (gameData.whiteUsername()!=null && gameData.whiteUsername().equals(username)) {
                currColor = "WHITE";
                message = String.format("%s has joined the game as white.", username);
            } else if (gameData.blackUsername()!=null && gameData.blackUsername().equals(username)) {
                currColor = "BLACK";
                message = String.format("%s has joined the game as black.", username);
            } else {
                currColor = null;
                message = String.format("%s has joined as an observer.", username);
            }

            connections.join(key, username, gameID, session, currColor);
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game(), currColor);
            connections.sendRoot(key, loadGameMessage);

            var notification= new NotificationMessage(message);
            connections.broadcast(key, notification);
        } catch (DataAccessException | NullPointerException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized (inside connect)" + e.getMessage());
            String msg = GsonServerMessage.getGson().toJson(errorMessage);
            try {
                session.getRemote().sendString(msg);
            } catch (IOException ex) {
                //woman shrug emoji
            }
        }


    }

    private void leave(String username, String authToken) {
        String key = username + authToken;
        Connection currConn = connections.get(key);
        String message = String.format("%s has left the game.", username);
        var notification= new NotificationMessage(message);
        connections.broadcast(key, notification);

        Session session = currConn.session;
        Integer gameID = currConn.gameID;
        try {
            String currColor = null;
            GameData gameData = dataAccess.getGame(gameID);
            if (gameData.whiteUsername()!=null && gameData.whiteUsername().equals(username)) {
                currColor = "WHITE";
                dataAccess.updateGame(currColor, gameID, null);
            } else if (gameData.blackUsername()!=null && gameData.blackUsername().equals(username)) {
                currColor = "BLACK";
                dataAccess.updateGame(currColor, gameID, null);
            }
            connections.leave(key);
        } catch (DataAccessException | NullPointerException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized (inside websocket leave)");
            String msg = GsonServerMessage.getGson().toJson(errorMessage);
            try {
                session.getRemote().sendString(msg);
            } catch (IOException ex) {
                //woman shrug emoji
            }
        }
    }

    private void makeMove(ChessMove move, String username, String authToken, Integer gameID) {
        String key = username + authToken;
        Connection currConn = connections.get(key);
        Session session = currConn.session;

        try {
            GameData gameData = dataAccess.getGame(gameID);
            ChessGame currGame = gameData.game();
            ChessGame.TeamColor currTeam = null;
            if (gameData.whiteUsername().equals(username)) {
                currTeam = ChessGame.TeamColor.WHITE;
            } else if (gameData.blackUsername().equals(username)) {
                currTeam = ChessGame.TeamColor.BLACK;
            }
            if (currGame.getGameStatus()) {
                if (currGame.getTeamTurn().equals(currTeam)) {
                    try {
                        currGame.makeMove(move);
                        NotificationMessage gameStatus = null;
                        if (currGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                            currGame.endGame();
                            gameStatus = new NotificationMessage(String.format("%s is in checkmate. White wins!!", gameData.blackUsername()));
                        } else if (currGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                            currGame.endGame();
                            gameStatus = new NotificationMessage(String.format("%s is in checkmate. Black wins!!", gameData.whiteUsername()));
                        } else if (currGame.isInCheck(ChessGame.TeamColor.WHITE)) {
                            gameStatus = new NotificationMessage(String.format("%s is in check.", gameData.whiteUsername()));
                        } else if (currGame.isInCheck(ChessGame.TeamColor.BLACK)) {
                            gameStatus = new NotificationMessage(String.format("%s is in check.", gameData.blackUsername()));
                        } else if (currGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
                            currGame.endGame();
                            gameStatus = new NotificationMessage(String.format("%s is in stalemate. Game is over at a draw.", gameData.whiteUsername()));
                        } else if (currGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
                            currGame.endGame();
                            gameStatus = new NotificationMessage(String.format("%s is in stalemate. Game is over at a draw.", gameData.blackUsername()));
                        }
                        dataAccess.updateGameStatus(gameID, currGame);
                        for (Connection c : connections.connections.values()) {
                            LoadGameMessage loadGameMessage = new LoadGameMessage(currGame, c.role);
                            connections.sendRoot(c.key, loadGameMessage);
                        }
                        String pastPos = reformat(move.getStartPosition());
                        String newPos = reformat(move.getEndPosition());
                        NotificationMessage notificationMessage = null;
                        if (move.getPromotionPiece() == null) {
                            notificationMessage = new NotificationMessage(String.format("%s moved %s to %s.", username, pastPos, newPos));
                        } else {
                            notificationMessage = new NotificationMessage(String.format("%s moved %s to %s, promoting the pawn to %s.", username, pastPos, newPos, move.getPromotionPiece().toString()));
                        }
                        connections.broadcast(key, notificationMessage);

                        if (gameStatus != null) {
                            connections.broadcast(key, gameStatus);
                            String msg = GsonServerMessage.getGson().toJson(gameStatus);
                            try {
                                session.getRemote().sendString(msg);
                            } catch (IOException ex) {
                                //woman shrug emoji
                            }
                        }
                    } catch (InvalidMoveException iMEx) {
                        ErrorMessage errorMessage = new ErrorMessage("Error: invalid move");
                        String msg = GsonServerMessage.getGson().toJson(errorMessage);
                        try {
                            session.getRemote().sendString(msg);
                        } catch (IOException ex) {
                            //woman shrug emoji
                        }
                    }
                } else {
                    ErrorMessage errorMessage = new ErrorMessage("Error: It's not your turn!");
                    String msg = GsonServerMessage.getGson().toJson(errorMessage);
                    try {
                        session.getRemote().sendString(msg);
                    } catch (IOException ex) {
                        //woman shrug emoji
                    }
                }
            } else {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game is over. No more moves can be made.");
                String msg = GsonServerMessage.getGson().toJson(errorMessage);
                try {
                    session.getRemote().sendString(msg);
                } catch (IOException ex) {
                    //woman shrug emoji
                }
            }
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized (inside websocket makeMove)");
            String msg = GsonServerMessage.getGson().toJson(errorMessage);
            try {
                session.getRemote().sendString(msg);
            } catch (IOException ex) {
                //woman shrug emoji
            }
        }


    }

    private String reformat(ChessPosition pos) {
        Character col = null;
        switch (pos.getColumn()) {
            case 1 -> col = 'a';
            case 2 -> col = 'b';
            case 3 -> col = 'c';
            case 4 -> col = 'd';
            case 5 -> col = 'e';
            case 6 -> col = 'f';
            case 7 -> col = 'g';
            case 8 -> col = 'h';
        }
        return String.format("%c%d", col, pos.getRow());
    }
}
