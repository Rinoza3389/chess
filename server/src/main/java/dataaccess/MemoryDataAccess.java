package dataaccess;
import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Random;

public class MemoryDataAccess implements DataAccess {

    final private HashMap<String, UserData> userDataSet = new HashMap<>();
    final private HashMap<Integer, GameData> gameDataSet = new HashMap<>();
    final private HashMap<String, AuthData> authDataSet = new HashMap<>();
    @SuppressWarnings("CanBeFinal")
    Random rand = new Random();

    public void clear() {
        userDataSet.clear();
        gameDataSet.clear();
        authDataSet.clear();
    }

    public UserData getUser(String username) {
        return userDataSet.get(username);
    }

    public AuthData getAuth(String authToken) {
        return authDataSet.get(authToken);
    }

    public String createUser(UserData user) {
        userDataSet.put(user.username(), user);
        return user.username();
    }

    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        if (authDataSet.get(authToken) == null) {
            AuthData auth = new AuthData(authToken, username);
            authDataSet.put(authToken, auth);
            return authToken;
        } else {
            throw new DataAccessException("Error: generated authToken already exists. Try again.");
        }
    }

    public void deleteAuth(String authToken) {
        authDataSet.remove(authToken);
    }

    public Integer createGame(String gameName) {
        Integer gameID = this.rand.nextInt(9999);
        ChessGame newGame = new ChessGame();
        GameData game = new GameData(gameID, null,null, gameName, newGame);
        gameDataSet.put(gameID, game);
        return gameID;
    }

    public GameData getGame(Integer gameID) {
        return gameDataSet.get(gameID);
    }

    public void updateGame(String playerColor, Integer gameID, String username) {
        GameData curGameData = gameDataSet.get(gameID);
        GameData newGameData;
        if (playerColor.equals("WHITE")) {
            newGameData = new GameData(gameID, username, curGameData.blackUsername(), curGameData.gameName(), curGameData.game());
        } else {
            newGameData = new GameData(gameID, curGameData.whiteUsername(), username, curGameData.gameName(), curGameData.game());
        }
        gameDataSet.put(gameID, newGameData);
    }

    public void updateGameStatus(Integer gameID, ChessGame game) throws DataAccessException {
        GameData curGameData = gameDataSet.get(gameID);
        GameData newGameData = new GameData(gameID, curGameData.whiteUsername(), curGameData.blackUsername(), curGameData.gameName(), game);
        gameDataSet.put(gameID, newGameData);
    }

    public ArrayList<GameData> listGames() {
        return new ArrayList<>(gameDataSet.values());
    }
}
