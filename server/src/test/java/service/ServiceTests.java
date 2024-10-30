package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import passoff.model.TestUser;
import server.*;

public class ServiceTests {

    private static Services mainService;

    private static TestUser existingUser;

    private static TestUser newUser;

    private String existingAuth;

    @BeforeAll
    public static void init() {
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        mainService = new Services();
        mainService.clear();

        //one user already logged in
        RegisterRequest logExistUser = new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
        RegisterResponse regResult = (RegisterResponse) mainService.registerUser(logExistUser);
        existingAuth = regResult.authToken();
    }


    @Test
    @DisplayName("Register Success")
    public void goodRegister() throws DataAccessException {
        RegisterRequest regNewUser = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        Object regResult = mainService.registerUser(regNewUser);
        assert regResult instanceof RegisterResponse;
    }

    @Test
    @DisplayName("Already Taken")
    public void badRequest() throws DataAccessException {
        RegisterRequest regNewUser = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        mainService.registerUser(regNewUser);
        RegisterRequest regSameUser = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        Object regTwoResult = mainService.registerUser(regSameUser);
        assert regTwoResult instanceof ErrorResponse;
    }


    @Test
    @DisplayName("Login Success")
    public void goodLogin() throws DataAccessException {
        LoginRequest loginUser = new LoginRequest(existingUser.getUsername(), existingUser.getPassword());
        Object loginResult = mainService.loginUser(loginUser);
        assert loginResult instanceof LoginResponse;
    }

    @Test
    @DisplayName("Bad User")
    public void badUser() throws DataAccessException {
        LoginRequest loginUser = new LoginRequest(newUser.getUsername(), newUser.getPassword());
        Object loginResult = mainService.loginUser(loginUser);
        assert loginResult instanceof ErrorResponse;
    }

    @Test
    @DisplayName("Bad Pass")
    public void badPass() throws DataAccessException {
        LoginRequest loginUser = new LoginRequest(existingUser.getUsername(), newUser.getPassword());
        Object loginResult = mainService.loginUser(loginUser);
        assert loginResult instanceof ErrorResponse;
    }


    @Test
    @DisplayName("Successful Logout")
    public void goodLogout() throws DataAccessException {
        LogoutRequest logoutUser = new LogoutRequest(existingAuth);
        Object logoutResult = mainService.logoutUser(logoutUser);
        assert logoutResult == null;
        logoutResult = mainService.logoutUser(logoutUser);
        assert logoutResult instanceof ErrorResponse;
    }

    @Test
    @DisplayName("Logout Fake User")
    public void badLogout() throws DataAccessException {
        LogoutRequest logoutUser = new LogoutRequest("existingAuth");
        Object logoutResult = mainService.logoutUser(logoutUser);
        assert logoutResult instanceof ErrorResponse;
    }


    @Test
    @DisplayName("Create Good Game")
    public void goodCreateGame()throws DataAccessException {
        CreateGameRequest creation = new CreateGameRequest(existingAuth, "My New Game");
        Object created = mainService.createNewGame(creation);
        assert created instanceof CreateGameResponse;
    }

    @Test
    @DisplayName("No Auth Token - Create Game")
    public void createNoAuth() throws DataAccessException {
        CreateGameRequest creation = new CreateGameRequest(null, "My New Game");
        Object created = mainService.createNewGame(creation);
        assert created instanceof ErrorResponse;
    }

    @Test
    @DisplayName("fAKE Token - Create Game")
    public void createFakeAuth() throws DataAccessException {
        CreateGameRequest creation = new CreateGameRequest("existingAuth", "My New Game");
        Object created = mainService.createNewGame(creation);
        assert created instanceof ErrorResponse;
    }


    @Test
    @DisplayName("Join Game Good")
    public void successJoin() throws DataAccessException {
        CreateGameResponse cgRes = (CreateGameResponse) mainService.createNewGame(new CreateGameRequest(existingAuth, "Join Game"));
        JoinRequest jReq = new JoinRequest(existingAuth, "WHITE", cgRes.gameID());
        Object jRes = mainService.joinGame(jReq);
        assert jRes instanceof JoinResponse;
    }

    @Test
    @DisplayName("Join Bad Auth")
    public void joinBadAuth() throws DataAccessException {
        CreateGameResponse cgRes = (CreateGameResponse) mainService.createNewGame(new CreateGameRequest(existingAuth, "Join Game"));
        JoinRequest jReq = new JoinRequest("existingAuth", "WHITE", cgRes.gameID());
        Object jRes = mainService.joinGame(jReq);
        assert jRes instanceof ErrorResponse;
    }

    @Test
    @DisplayName("Join Game Bad")
    public void joinBadGame() throws DataAccessException {
        JoinRequest jReq = new JoinRequest(existingAuth, "WHITE", 1234);
        Object jRes = mainService.joinGame(jReq);
        assert jRes instanceof ErrorResponse;
    }

    @Test
    @DisplayName("Join Game Repeat User")
    public void joinRepeat() throws DataAccessException {
        CreateGameResponse cgRes = (CreateGameResponse) mainService.createNewGame(new CreateGameRequest(existingAuth, "Join Game"));
        JoinRequest jReq = new JoinRequest(existingAuth, "WHITE", cgRes.gameID());
        mainService.joinGame(jReq);
        Object jRes2 = mainService.joinGame(jReq);
        assert jRes2 instanceof ErrorResponse;
    }


    @Test
    @DisplayName("List Games Good")
    public void listGamesGood() throws DataAccessException {
        mainService.createNewGame(new CreateGameRequest(existingAuth, "My New Game"));
        mainService.createNewGame(new CreateGameRequest(existingAuth, "My second Game"));
        mainService.createNewGame(new CreateGameRequest(existingAuth, "My third Game"));
        ListRequest lstReq = new ListRequest(existingAuth);
        Object lstRes = mainService.listAllGames(lstReq);
        assert lstRes instanceof ListResponse;
    }

    @Test
    @DisplayName("List Games Bad Auth")
    public void listBadAuth() throws DataAccessException {
        mainService.createNewGame(new CreateGameRequest(existingAuth, "My New Game"));
        mainService.createNewGame(new CreateGameRequest(existingAuth, "My second Game"));
        mainService.createNewGame(new CreateGameRequest(existingAuth, "My third Game"));
        ListRequest lstReq = new ListRequest("existingAuth");
        Object lstRes = mainService.listAllGames(lstReq);
        assert lstRes instanceof ErrorResponse;
    }


    @Test
    @DisplayName("Test Clear")
    public void testClear() throws DataAccessException {
        mainService.registerUser(new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail()));
        mainService.createNewGame(new CreateGameRequest(existingAuth, "The Game"));
        Object clearRes = mainService.clear();
        assert clearRes == null;
        Object loginRes = mainService.loginUser(new LoginRequest(existingUser.getUsername(), existingUser.getPassword()));
        assert loginRes instanceof ErrorResponse;
    }

}
