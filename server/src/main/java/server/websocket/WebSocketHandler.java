package server.websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.GsonServerMessage;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

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
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getAuthToken(), command.getGameID());
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
            if (gameData.whiteUsername().equals(username)) {
                currColor = "WHITE";
                message = String.format("%s has joined the game as white.", username);
            } else if (gameData.blackUsername().equals(username)) {
                currColor = "BLACK";
                message = String.format("%s has joined the game as black.", username);
            } else {
                currColor = null;
                message = String.format("%s has joined as an observer.", username);
            }

            connections.join(key, username, gameID, session);
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game(), currColor);
            connections.sendRoot(key, loadGameMessage);

            var notification= new NotificationMessage(message);
            connections.broadcast(key, notification);
        } catch (DataAccessException | NullPointerException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
            String msg = GsonServerMessage.getGson().toJson(errorMessage);
            try {
                session.getRemote().sendString(msg);
            } catch (IOException ex) {
                //woman shrug emoji
            }
        }


    }

}
