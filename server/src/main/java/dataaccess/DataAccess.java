package dataaccess;
import model.*;

import java.util.ArrayList;

public interface DataAccess {

    void clear();

    UserData getUser(String username);

    String createUser(UserData user);

    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    Integer createGame(String gameName);

    GameData getGame(Integer gameID);

    void updateGame(String playerColor, Integer gameID, String username);

    ArrayList<GameData> listGames();
}
