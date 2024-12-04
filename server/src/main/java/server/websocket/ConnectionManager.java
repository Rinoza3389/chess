package server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import websocket.GsonServerMessage;
import websocket.messages.*;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void join(String key, String username, Integer gameID, Session session, String role) {
        var connection = new Connection(key, username, gameID, session, role);
        connections.put(key, connection);
    }

    public void leave(String key) {
        connections.remove(key);
    }

    public void broadcast(String excludeKey, ServerMessage serverMessage) {
        var removeList = new ArrayList<Connection>();
        Integer gameID = connections.get(excludeKey).gameID;
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.key.equals(excludeKey) && Objects.equals(c.gameID, gameID)) {
                    String msg = GsonServerMessage.getGson().toJson(serverMessage);
                    try {
                        c.send(msg);
                    } catch (IOException e) {
                        //*woman shrug emoji*
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

    public void sendRoot(String key, ServerMessage serverMessage) {
        Connection c = connections.get(key);
        String msg = GsonServerMessage.getGson().toJson(serverMessage);
        try {
            c.send(msg);
        } catch (IOException e) {
            //*woman shrug emoji*
        }
    }

    public Connection get(String key) {
        return connections.get(key);
    }
}
