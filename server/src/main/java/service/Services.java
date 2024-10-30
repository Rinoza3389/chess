package service;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import server.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class Services {

    private final DataAccess dataAccess;

    public Services() {
        try {
            this.dataAccess = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object clear() throws DataAccessException {
        dataAccess.clear();
        return null;
    }

    public Object registerUser(RegisterRequest regRequest) throws DataAccessException {
        UserData user = new UserData(regRequest.username(), regRequest.password(), regRequest.email());

        if (dataAccess.getUser(user.username()) == null) {
            try {
                String username = dataAccess.createUser(user);
                String authToken = dataAccess.createAuth(user.username());
                return new RegisterResponse(username, authToken);
            } catch (DataAccessException e) {
                return new ErrorResponse(500, e.getMessage());
            }
        }
        else {
            return new ErrorResponse(403, "Error: already taken");
        }
    }

    public Object loginUser(LoginRequest logReq) throws DataAccessException {
        UserData user = dataAccess.getUser(logReq.username());
        if (user == null) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        if (!BCrypt.checkpw(logReq.password(), user.password())) {
            return new ErrorResponse(401, "Error: unauthorized");
        } else {
            String authToken = dataAccess.createAuth(user.username());
            return new LoginResponse(user.username(), authToken);
        }

    }

    public Object logoutUser(LogoutRequest logReq) {
        AuthData authData = dataAccess.getAuth(logReq.authToken());
        if (authData == null) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        dataAccess.deleteAuth(logReq.authToken());
        return null;
    }

    public Object createNewGame(CreateGameRequest cgReq) {
        if (cgReq.authToken() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        AuthData authData = dataAccess.getAuth(cgReq.authToken());
        if (authData == null) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        Integer gameID = dataAccess.createGame(cgReq.gameName());
        return new CreateGameResponse(gameID);
    }

    public Object joinGame(JoinRequest joinReq) {
        AuthData authData = dataAccess.getAuth(joinReq.authToken());
        if (authData == null) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        GameData gameData = dataAccess.getGame(joinReq.gameID());
        if (gameData == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        if (joinReq.playerColor().equals("WHITE")) {
            if (!(gameData.whiteUsername() == null)) {
                return new ErrorResponse(403, "Error: already taken");
            }
        } else {
            if (!(gameData.blackUsername() == null)) {
                return new ErrorResponse(403, "Error: already taken");
            }
        }
        dataAccess.updateGame(joinReq.playerColor(), joinReq.gameID(), authData.username());
        return new JoinResponse(true);
    }

    public Object listAllGames(ListRequest lisReq) {
        AuthData authData = dataAccess.getAuth(lisReq.authToken());
        if (authData == null) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        ArrayList<GameData> gamesList = dataAccess.listGames();
        return new ListResponse(gamesList);
    }
}
