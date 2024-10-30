package dataaccess;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataAccess {

    void clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    String createUser(UserData user) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    Integer createGame(String gameName);

    GameData getGame(Integer gameID);

    void updateGame(String playerColor, Integer gameID, String username);

    ArrayList<GameData> listGames();
}
