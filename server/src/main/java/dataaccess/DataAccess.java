package dataaccess;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataAccess {

    void clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    String createUser(UserData user) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    Integer createGame(String gameName) throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    void updateGame(String playerColor, Integer gameID, String username) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;
}
