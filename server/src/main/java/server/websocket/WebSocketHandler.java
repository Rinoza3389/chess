package server.websocket;

import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

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

        connections.join(key, username, gameID, session);
        var message = String.format("%s has joined the game.", username);
        var notification= new NotificationMessage(message);
        connections.broadcast(username, notification);
    }

}
