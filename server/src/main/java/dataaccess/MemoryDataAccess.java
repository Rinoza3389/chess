package dataaccess;
import model.*;
import java.util.HashMap;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {

    final private HashMap<String, UserData> UserDataSet = new HashMap<>();
    final private HashMap<Integer, GameData> GameDataSet = new HashMap<>();
    final private HashMap<String, AuthData> AuthDataSet = new HashMap<>();

    public void clear() {
        UserDataSet.clear();
        GameDataSet.clear();
        AuthDataSet.clear();
    }

    public UserData getUser(String username) {
        return UserDataSet.get(username);
    }

    public AuthData getAuth(String authToken) {
        return AuthDataSet.get(authToken);
    }

    public void createUser(UserData user) {
        UserDataSet.put(user.username(), user);
    }

    public void createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        if (AuthDataSet.get(authToken) == null) {
            AuthData auth = new AuthData(authToken, username);
            AuthDataSet.put(authToken, auth);
        }
    }
}
