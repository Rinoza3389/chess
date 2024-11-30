package websocket.messages;

public class NotificationMessage extends ServerMessage {

    String message;

    public NotificationMessage(String msg) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
