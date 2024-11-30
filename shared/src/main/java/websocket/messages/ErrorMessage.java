package websocket.messages;

public class ErrorMessage extends ServerMessage {

    String message;

    public ErrorMessage(String msg) {
        super(ServerMessageType.ERROR);
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
