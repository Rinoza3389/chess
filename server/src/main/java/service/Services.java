package service;
import dataaccess.*;
import model.*;

import javax.xml.crypto.Data;

public class Services {

    private final DataAccess dataAccess;

    public Services() {
        this.dataAccess = new MemoryDataAccess();
    }

    public void clear()  throws DataAccessException {
        dataAccess.clear();
    }

    public void registerUser(UserData user) throws DataAccessException {
        if (dataAccess.getUser(user.username()) == null) {
            dataAccess.createUser(user);
            dataAccess.createAuth(user.username());
        }
        else {
            throw new DataAccessException("Error: already taken");
        }
    }
}
