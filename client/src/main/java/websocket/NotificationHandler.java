package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class NotificationHandler {
    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case LOAD_GAME -> System.out.println(SET_TEXT_COLOR_RED + ((LoadGameMessage) notification).getMessage());
            case NOTIFICATION -> System.out.println(SET_TEXT_COLOR_RED + ((NotificationMessage) notification).getMessage());
            case ERROR -> System.out.println(SET_TEXT_COLOR_RED + ((ErrorMessage) notification).getMessage());
        }
    }
}
