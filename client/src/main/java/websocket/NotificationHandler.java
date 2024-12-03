package websocket;

import chess.ChessGame;
import chess.ChessPosition;
import ui.ChessBoardUI;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Set;

import static ui.EscapeSequences.*;

public class NotificationHandler {
    ChessGame currGameState = null;

    public NotificationHandler(){

    }

    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case LOAD_GAME:
                this.currGameState = ((LoadGameMessage) notification).getGame();
                new ChessBoardUI(((LoadGameMessage) notification).getGame().getBoard()).run(((LoadGameMessage) notification).getRole(), null, null);
            case NOTIFICATION:
                System.out.println(SET_TEXT_COLOR_GREEN + ((NotificationMessage) notification).getMessage());
                System.out.print(RESET_TEXT_COLOR);
            case ERROR :
                System.out.println(SET_TEXT_COLOR_RED + ((ErrorMessage) notification).getMessage());
                System.out.print(RESET_TEXT_COLOR);
        }
    }

    public void highlight(String role, Set<ChessPosition> possPos, ChessPosition currPos) {
        new ChessBoardUI(currGameState.getBoard()).run(role, possPos, currPos);
    }

    public void run(String role) {
        new ChessBoardUI(currGameState.getBoard()).run(role, null, null);
    }
}
