package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.sql.*;
import java.util.UUID;

import static dataaccess.DatabaseManager.*;

public class SqlDataAccess implements DataAccess{

    public SqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        String[] statements = {"TRUNCATE GameData", "TRUNCATE UserData", "TRUNCATE AuthData"};
        try (var conn = getConnection()) {
            for (var statement : statements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s", e.getMessage()));
        }
    };

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM UserData WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        UserData returnedUser = new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                        return returnedUser;
                    } else { return null;}
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    };

    public String createUser(UserData user) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO UserData (username, password, email) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.username());
                String hashedPass = BCrypt.hashpw(user.password(), BCrypt.gensalt());
                preparedStatement.setString(2, hashedPass);
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();
                var rs = preparedStatement.getGeneratedKeys();
                if (rs != null) {
                    return user.username();
                }
                else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    };

    public String createAuth(String username) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO AuthData (authToken, username) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                String authToken = UUID.randomUUID().toString();
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();

                var rs = preparedStatement.getGeneratedKeys();
                if (rs != null) {
                    return authToken;
                }
                else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    };

    public AuthData getAuth(String authToken) throws DataAccessException{
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT authToken, username FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                    else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    };

    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try {
                    var rs = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    };

    public Integer createGame(String gameName) {
        return null;
    };

    public GameData getGame(Integer gameID) {
        return null;
    };

    public void updateGame(String playerColor, Integer gameID, String username) {

    };

    public ArrayList<GameData> listGames() {
        return null;
    };

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  GameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameID),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
