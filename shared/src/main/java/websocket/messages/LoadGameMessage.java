package websocket.messages;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    ChessGame game;
    String role;

    public LoadGameMessage(ChessGame game, String role) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.role = role;
    }

    public ChessGame getGame() {
        return this.game;
    }

    public String getRole() {
        return this.role;
    }

}
