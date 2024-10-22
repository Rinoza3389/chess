package service;
import dataaccess.*;
import model.*;
import server.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Services {

    private final DataAccess dataAccess;

    public Services() {
        this.dataAccess = new MemoryDataAccess();
    }

    public Object clear()  throws DataAccessException {
        try {
            dataAccess.clear();
            return null;
        } catch (DataAccessException e) {
            return new ErrorResponse(500, "Error: Something went wrong.");
        }
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
            ErrorResponse nouser = new ErrorResponse(401, "Error: unauthorized");
            return nouser;
        }
        if (!user.password().equals(logReq.password())) {
            ErrorResponse badpass = new ErrorResponse(401, "Error: unauthorized");
            return badpass;
        } else {
            String authToken = dataAccess.createAuth(user.username());
            LoginResponse logResult = new LoginResponse(user.username(), authToken);
            return logResult;
        }

    }

    public Object logoutUser(LogoutRequest logReq) throws DataAccessException {
        AuthData authData = dataAccess.getAuth(logReq.authToken());
        if (authData == null) {
            ErrorResponse nouser = new ErrorResponse(401, "Error: unauthorized");
            return nouser;
        }
        dataAccess.deleteAuth(logReq.authToken());
        return null;
    }

    public Object createNewGame(CreateGameRequest cgReq) throws DataAccessException {
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

    public Object joinGame(JoinRequest joinReq) throws DataAccessException {
        AuthData authData = dataAccess.getAuth(joinReq.authToken());
        if (authData == null) {
            ErrorResponse nouser = new ErrorResponse(401, "Error: unauthorized");
            return nouser;
        }
        GameData gameData = dataAccess.getGame(joinReq.gameID());
        if (gameData == null) {
            ErrorResponse nogame = new ErrorResponse(400, "Error: bad request");
            return nogame;
        }
        if (joinReq.playerColor().equals("WHITE")) {
            if (!(gameData.whiteUsername() == null)) {
                ErrorResponse usertaken = new ErrorResponse(403, "Error: already taken");
                return usertaken;
            }
        } else {
            if (!(gameData.blackUsername() == null)) {
                ErrorResponse usertaken = new ErrorResponse(403, "Error: already taken");
                return usertaken;
            }
        }
        try {
            dataAccess.updateGame(joinReq.playerColor(), joinReq.gameID(), authData.username());
            return new JoinResponse(true);
        } catch (DataAccessException e) {
            return new ErrorResponse(500, e.getMessage());
        }
    }

    public Object listAllGames(ListRequest lisReq) throws DataAccessException {
        AuthData authData = dataAccess.getAuth(lisReq.authToken());
        if (authData == null) {
            ErrorResponse nouser = new ErrorResponse(401, "Error: unauthorized");
            return nouser;
        }
        ArrayList<GameData> gamesList = dataAccess.listGames();
        return new ListResponse(gamesList);
    }
}
