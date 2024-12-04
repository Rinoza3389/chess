package websocket;

import chess.ChessMove;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import websocket.GsonUserGameCommand;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notifHandler;

    public WebSocketFacade(Integer port, NotificationHandler notifHandler) throws Exception {
        try {
            String url = "ws://localhost:"+port;
            URI socketURI = new URI(url + "/ws");
            this.notifHandler = notifHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = GsonServerMessage.getGson().fromJson(message, ServerMessage.class);
                    notifHandler.notify(notification);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception(e.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connectToGame(String authToken, Integer gameID) throws Exception {
        try {
            var userGameCom = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCom));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void leaveGame(String authToken, Integer gameID) throws Exception {
        try {
            var userGameCom = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(GsonUserGameCommand.getGson().toJson(userGameCom));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void makeMove(ChessMove move, String authToken, Integer gameID) throws Exception {
        try {
            var userGameCom = new MakeMoveCommand(move, authToken, gameID);
            this.session.getBasicRemote().sendText(GsonUserGameCommand.getGson().toJson(userGameCom));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void resignGame(String authToken, Integer gameID) throws Exception {
        try {
            var userGameCom = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(GsonUserGameCommand.getGson().toJson(userGameCom));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
