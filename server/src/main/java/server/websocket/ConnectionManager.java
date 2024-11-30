package server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void join(String key, String username, Integer gameID, Session session) {
        var connection = new Connection(key, username, gameID, session);
        connections.put(key, connection);
    }

    public void broadcast(String excludeKey, ServerMessage serverMessage) {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.key.equals(excludeKey)) {
                    try {
                        switch (serverMessage.getServerMessageType()) {
                            case LOAD_GAME -> c.send(((LoadGameMessage) serverMessage).getMessage());
                            case NOTIFICATION -> c.send(((NotificationMessage) serverMessage).getMessage());
                            case ERROR -> c.send(((ErrorMessage) serverMessage).getMessage());
                        }
                    } catch (IOException e) {
                        //BURN THIS BRIDGE LATER
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c: removeList) {
            connections.remove(c.key);
        }
    }
}
