package client;

import org.junit.jupiter.api.*;
import server.RegisterRequest;
import server.*;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;


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
        assertTrue(authData instanceof ErrorResponse);
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
    }


}
