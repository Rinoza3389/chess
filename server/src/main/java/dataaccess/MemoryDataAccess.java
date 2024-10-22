package dataaccess;
import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Random;

public class MemoryDataAccess implements DataAccess {

    final private HashMap<String, UserData> UserDataSet = new HashMap<>();
    final private HashMap<Integer, GameData> GameDataSet = new HashMap<>();
    final private HashMap<String, AuthData> AuthDataSet = new HashMap<>();
    Random rand = new Random();

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

    public String createUser(UserData user) {
        UserDataSet.put(user.username(), user);
        return user.username();
    }

    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        if (AuthDataSet.get(authToken) == null) {
            AuthData auth = new AuthData(authToken, username);
            AuthDataSet.put(authToken, auth);
            return authToken;
        } else {
            throw new DataAccessException("Error: generated authToken already exists. Try again.");
        }
    }

    public void deleteAuth(String authToken) {
        AuthDataSet.remove(authToken);
    }

    public Integer createGame(String gameName) {
        Integer gameID = this.rand.nextInt(9999);
        ChessGame newGame = new ChessGame();
        GameData game = new GameData(gameID, null,null, gameName, newGame);
        GameDataSet.put(gameID, game);
        return gameID;
    }

    public GameData getGame(Integer gameID) {
        return GameDataSet.get(gameID);
    }

    public void updateGame(String playerColor, Integer gameID, String username) {
        GameData curGameData = GameDataSet.get(gameID);
        GameData newGameData;
        if (playerColor.equals("WHITE")) {
            newGameData = new GameData(gameID, username, curGameData.blackUsername(), curGameData.gameName(), curGameData.game());
        } else {
            newGameData = new GameData(gameID, curGameData.whiteUsername(), username, curGameData.gameName(), curGameData.game());
        }
        GameDataSet.put(gameID, newGameData);
    }

    public ArrayList<GameData> listGames() {
        return new ArrayList<>(GameDataSet.values());
    }
}
