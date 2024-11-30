package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String key;
    public String username;
    public Integer gameID;
    public Session session;

    public Connection(String key, String username, Integer gameID, Session session) {
        this.key = key;
        this.username = username;
        this.gameID = gameID;
        this.session = session;
    };

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
