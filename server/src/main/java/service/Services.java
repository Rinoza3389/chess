package service;
import dataaccess.*;
import model.*;
import server.*;

import javax.xml.crypto.Data;

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
}
