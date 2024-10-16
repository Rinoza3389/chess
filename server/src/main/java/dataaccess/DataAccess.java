package dataaccess;
import model.*;

public interface DataAccess {

    void clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    void createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;
}
