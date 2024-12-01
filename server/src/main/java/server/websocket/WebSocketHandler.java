package server.websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

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
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

        String username = null;
        try {
            username = dataAccess.getAuth(command.getAuthToken()).username();
        } catch (DataAccessException e) {
            //figure it out in a minute
        }

//        connections.saveSession(command.getGameID(), session);

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, username, command.getAuthToken(), command.getGameID());
        }
    }

    private void connect(Session session, String username, String authToken, Integer gameID) {
        String key = username + authToken;

        try {
            String message;
            GameData gameData = dataAccess.getGame(gameID);
            if (gameData.whiteUsername().equals(username)) {
                ChessGame.TeamColor currColor = ChessGame.TeamColor.WHITE;
                message = String.format("%s has joined the game as white.", username);
            } else if (gameData.blackUsername().equals(username)) {
                ChessGame.TeamColor currColor = ChessGame.TeamColor.BLACK;
                message = String.format("%s has joined the game as black.", username);
            } else {
                ChessGame.TeamColor currColor = null;
                message = String.format("%s has joined as an observer.", username);
            }

            connections.join(key, username, gameID, session);
            LoadGameMessage loadGameMessage = new LoadGameMessage("Successfully joined the game.");
            connections.sendRoot(key, loadGameMessage);

            var notification= new NotificationMessage(message);
            connections.broadcast(key, notification);
        } catch (DataAccessException e) {
            //Burn that bridge if we get to it.
        }


    }

}
