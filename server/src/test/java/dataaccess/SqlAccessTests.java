package dataaccess;

import model.*;
import org.junit.jupiter.api.*;
import server.ErrorResponse;
import server.RegisterRequest;
import server.RegisterResponse;
import service.Services;


public class SqlAccessTests {

    private static SqlDataAccess currAccess;
    private static Services mainService;
    private static UserData existingUser;
    private static UserData newUser;
    private static String existingAuth;

    @BeforeAll
    public static void setup() throws DataAccessException {
        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");

        mainService = new Services();
        currAccess = new SqlDataAccess();
        currAccess.clear();

        RegisterRequest logExistUser = new RegisterRequest(existingUser.username(), existingUser.password(), existingUser.email());
        RegisterResponse regResult = (RegisterResponse) mainService.registerUser(logExistUser);
        existingAuth = regResult.authToken();
    }

    @Test
    @DisplayName("Test Add User")
    public void createUserGood() throws DataAccessException {
        UserData user1 = new UserData("username", "password", "email.com");
        String resultUser1 = currAccess.createUser(user1);
        UserData user2 = new UserData("yourMom", "1234", "urmom.com");
        String resultUser2 = currAccess.createUser(user2);

        assert resultUser1.equals("username");
        assert resultUser2.equals("yourMom");
    }

    @Test
    @DisplayName("Test Duplicate User")
    public void createUserBad() throws DataAccessException {
        RegisterRequest regNewUser = new RegisterRequest(newUser.username(), newUser.password(), newUser.email());
        mainService.registerUser(regNewUser);
        RegisterRequest regSameUser = new RegisterRequest(newUser.username(), newUser.password(), newUser.email());
        Object regTwoResult = mainService.registerUser(regSameUser);
        assert regTwoResult instanceof ErrorResponse;
    }

    @Test
    @DisplayName("Test Get User")
    public void getUserGood() throws DataAccessException {
        UserData user3 = new UserData("gallwaygirl", "edward", "sheeran.com");
        String resultUser3 = currAccess.createUser(user3);
        UserData existingUserResult = currAccess.getUser(resultUser3);
        assert existingUserResult != null;
        assert existingUserResult.username().equals(resultUser3);
        assert existingUserResult.email().equals("sheeran.com");
    }

    @Test
    @DisplayName("Get User Bad")
    public void getUserBad() throws DataAccessException {
        UserData existingUserResult = currAccess.getUser("maddyyyyy");
        assert existingUserResult == null;
    }

    @Test
    @DisplayName("Create Auth Good")
    public void createAuthGood() throws DataAccessException {
        String authToken = currAccess.createAuth(existingUser.username());
        assert authToken != null;
    }

    @Test
    @DisplayName("CreateAuthBad")
    public void createAuthBad() throws DataAccessException {
        String authToken = currAccess.createAuth(existingUser.username());
        assert authToken != null;
    }

    @Test
    @DisplayName("GetAuthGood")
    public void getAuthGood() throws DataAccessException {
        String authToken = currAccess.createAuth(existingUser.username());
        AuthData authData = currAccess.getAuth(authToken);
        assert authData != null;
    }

    @Test
    @DisplayName("GetAuthBad")
    public void getAuthBad() throws DataAccessException {
        AuthData authToken = currAccess.getAuth("existingAuth");
        assert authToken == null;
    }

    @Test
    @DisplayName("DeleteAuthGood")
    public void deleteAuthGood() throws DataAccessException {
        currAccess.deleteAuth(existingAuth);
        AuthData authToken = currAccess.getAuth(existingAuth);
        assert authToken == null;
    }

    @Test
    @DisplayName("DeleteAuthBad")
    public void deleteAuthBad() throws DataAccessException {
        currAccess.deleteAuth(existingAuth);
        AuthData authToken = currAccess.getAuth(existingAuth);
        assert authToken == null;
    }

    @Test
    @DisplayName("CreateGameGood")
    public void createGameGood() throws DataAccessException {
        Integer momCode = currAccess.createGame("Your MOm's House");
        assert momCode != null;
    }

    @Test
    @DisplayName("CreateGameBad")
    public void createGameBad() throws DataAccessException {
        Integer windowCode = currAccess.createGame("Defenestration");
        assert windowCode != null;
    }

    @Test
    @DisplayName("GetGameGood")
    public void getGameGood() throws DataAccessException {
        Integer harryPotter = currAccess.createGame("pOTTER");
        GameData potterGame = currAccess.getGame(harryPotter);
        assert potterGame != null;
        assert potterGame.gameName().equals("pOTTER");
    }

    @Test
    @DisplayName("GetGameBad")
    public void getGameBad() throws DataAccessException {
        GameData makeAWish = currAccess.getGame(33890);
        assert makeAWish == null;
    }

    @Test
    @DisplayName("UpdateGameGood")
    public void updateGameGood() throws DataAccessException {
        Integer weesh = currAccess.createGame("11:11");
        GameData makeAWish = currAccess.getGame(weesh);
        assert makeAWish != null;
        currAccess.updateGame("WHITE", weesh, "MaddyPaddy");
        makeAWish = currAccess.getGame(weesh);
        assert makeAWish.whiteUsername().equals("MaddyPaddy");
    }

    @Test
    @DisplayName("UpdateGameBad")
    public void updateGameBad() throws DataAccessException {
        Integer grrr = currAccess.createGame("rawr");
        GameData animalGrowl = currAccess.getGame(grrr);
        assert animalGrowl != null;
        currAccess.updateGame("BLACK", grrr, "MaddyPaddy");
        animalGrowl = currAccess.getGame(grrr);
        assert animalGrowl.blackUsername().equals("MaddyPaddy");
    }

    @Test
    @DisplayName("ListGames")
    public void listGames() throws DataAccessException {
        var listOfGames = currAccess.listGames();
        assert listOfGames != null;
    }

    @Test
    @DisplayName("Test Clear")
    public void clear() throws DataAccessException {
        currAccess.clear();
        var listOfGames = currAccess.listGames();
        assert listOfGames.isEmpty();
    }

    @Test
    @DisplayName("ListGamesBad")
    public void listGamesBad() throws DataAccessException {
        currAccess.clear();
        var listOfGames = currAccess.listGames();
        assert listOfGames.isEmpty();
    }
}
