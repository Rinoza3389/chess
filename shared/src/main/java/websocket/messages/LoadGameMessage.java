package websocket.messages;

public class LoadGameMessage extends ServerMessage {

    String message;

    public LoadGameMessage(String msg) {
        super(ServerMessageType.LOAD_GAME);
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
