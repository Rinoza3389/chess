package client;

import org.junit.jupiter.api.*;
import ui.reqRes.*;
import ui.ServerFacade;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() {
        facade.clearFacade();
    }


    @Test
    @DisplayName("Successful Register")
    public void register() throws Exception{
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.registerFacade(regReq);
        assertTrue(((RegisterResponse) authData).authToken().length() > 10);
    }

    @Test
    @DisplayName("Register Bad Input Type")
    public void registerBadInput() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", null, "p1@email.com");
        var authData = facade.registerFacade(regReq);
        assertInstanceOf(ErrorResponse.class, authData);
    }

    @Test
    @DisplayName("Register User Twice")
    public void reRegister() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        facade.registerFacade(regReq);
        var authData = facade.registerFacade(regReq);
        assertTrue(authData instanceof String);
    }

    @Test
    @DisplayName("Login Successful")
    public void login() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        facade.registerFacade(regReq);

        LoginRequest logReq = new LoginRequest("player1", "password");
        var authData = facade.loginFacade(logReq);
        assertTrue(((LoginResponse) authData).authToken().length() > 10);
    }

    @Test
    @DisplayName("Login Bad Input Type")
    public void loginBad() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        facade.registerFacade(regReq);

        LoginRequest logReq = new LoginRequest("player1", null);
        var authData = facade.loginFacade(logReq);
        assertTrue(authData instanceof ErrorResponse);

        logReq = new LoginRequest(null, "password");
        authData = facade.loginFacade(logReq);
        assertTrue(authData instanceof ErrorResponse);
    }

    @Test
    @DisplayName("Login Fake User")
    public void loginFake() throws Exception {
        LoginRequest logReq = new LoginRequest("player1", "password");
        var authData = facade.loginFacade(logReq);
        assertTrue(authData instanceof String);
    }

    @Test
    @DisplayName("Logout Successful")
    public void logout() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        LogoutRequest logReq = new LogoutRequest(((RegisterResponse) regRes).authToken());
        var authData = facade.logoutFacade(logReq);
        assertTrue(authData == null);
    }

    @Test
    @DisplayName("Logout Fake User")
    public void logoutFake() throws Exception {
        LogoutRequest logReq = new LogoutRequest(null);
        var authData = facade.logoutFacade(logReq);
        assertTrue(authData instanceof ErrorResponse);

        logReq = new LogoutRequest("null");
        authData = facade.logoutFacade(logReq);
        assertTrue(authData instanceof String);
    }

    @Test
    @DisplayName("Create Game Good")
    public void createGame() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        CreateGameRequest cgReq = new CreateGameRequest(((RegisterResponse) regRes).authToken(), "Christmas Jazz");
        var authData = facade.createGameFacade(cgReq);
        assertTrue(authData instanceof CreateGameResponse);
    }

    @Test
    @DisplayName("Create Game Bad Input")
    public void createGameBad() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        CreateGameRequest cgReq = new CreateGameRequest(((RegisterResponse) regRes).authToken(), null);
        var authData = facade.createGameFacade(cgReq);
        assertTrue(authData instanceof ErrorResponse);
    }

    @Test
    @DisplayName("Create Game No Auth")
    public void createGameFake() throws Exception {
        CreateGameRequest cgReq = new CreateGameRequest(null, "null");
        var authData = facade.createGameFacade(cgReq);
        assertTrue(authData instanceof ErrorResponse);

        cgReq = new CreateGameRequest("null", "null");
        authData = facade.createGameFacade(cgReq);
        assertTrue(authData instanceof String);
    }

    @Test
    @DisplayName("List Games Good")
    public void list() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        ListRequest lsReq = new ListRequest(((RegisterResponse) regRes).authToken());
        var authData = facade.listFacade(lsReq);
        assertTrue(authData instanceof ListResponse);
        assertTrue(((ListResponse) authData).games().isEmpty());

        CreateGameRequest cgReq = new CreateGameRequest(((RegisterResponse) regRes).authToken(), "Christmas Jazz");
        facade.createGameFacade(cgReq);

        authData = facade.listFacade(lsReq);
        assertTrue(authData instanceof ListResponse);
        assertFalse(((ListResponse) authData).games().isEmpty());
    }

    @Test
    @DisplayName("List Games Bad Auth")
    public void listFake() throws Exception {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);
        ListRequest lsReq = new ListRequest(null);

        CreateGameRequest cgReq = new CreateGameRequest(((RegisterResponse) regRes).authToken(), "Christmas Jazz");
        facade.createGameFacade(cgReq);

        var authData = facade.listFacade(lsReq);
        assertTrue(authData instanceof ErrorResponse);

        lsReq = new ListRequest("null");
        authData = facade.listFacade(lsReq);
        assertTrue(authData instanceof String);
    }

    @Test
    @DisplayName("Join Game Good")
    public void join() {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        CreateGameRequest cgReq = new CreateGameRequest(((RegisterResponse) regRes).authToken(), "Christmas Jazz");
        var gameData = facade.createGameFacade(cgReq);

        JoinRequest joinReq = new JoinRequest(((RegisterResponse) regRes).authToken(), "WHITE", ((CreateGameResponse) gameData).gameID());
        var authData = facade.joinFacade(joinReq);
        assertTrue(authData == null);
    }

    @Test
    @DisplayName("Join Game Bad Input")
    public void joinBad() {
        JoinRequest joinReq = new JoinRequest(null, "WHITE", null);
        var authData = facade.joinFacade(joinReq);
        assertTrue(authData instanceof ErrorResponse);
    }

    @Test
    @DisplayName("Join Fake Game")
    public void joinFakeGame() {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        JoinRequest joinReq = new JoinRequest(((RegisterResponse) regRes).authToken(), "WHITE", 1234);
        var authData = facade.joinFacade(joinReq);
        assertTrue(authData instanceof String);
    }

    @Test
    @DisplayName("Join Fake User")
    public void joinFakeUser() {
        RegisterRequest regReq = new RegisterRequest("player1", "password", "p1@email.com");
        var regRes = facade.registerFacade(regReq);

        CreateGameRequest cgReq = new CreateGameRequest(((RegisterResponse) regRes).authToken(), "Christmas Jazz");
        var gameData = facade.createGameFacade(cgReq);

        JoinRequest joinReq = new JoinRequest("((RegisterResponse) regRes).authToken()", "WHITE", ((CreateGameResponse) gameData).gameID());
        var authData = facade.joinFacade(joinReq);
        assertTrue(authData instanceof String);
    }
}
