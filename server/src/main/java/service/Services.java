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
}
