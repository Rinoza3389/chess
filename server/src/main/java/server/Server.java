package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccess;
import spark.*;
import dataaccess.DataAccessException;
import service.*;

import java.sql.SQLException;


public class Server {

    private final Services mainService  = new Services();

    public Server() {

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinHandler);
        Spark.get("/game", this::listHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearHandler(Request req, Response res) throws DataAccessException {
        var errorMaybe = mainService.clear();
        if (errorMaybe instanceof ErrorResponse){
            res.status(((ErrorResponse) errorMaybe).status());
            return new Gson().toJson(errorMaybe);
        }
        else {
            res.status(200);
            return "";
        }
    }

    private Object registerHandler(Request req, Response res){
        var regRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        if (regRequest.username() == null || regRequest.password() == null || regRequest.email() == null) {
            ErrorResponse failedRequest = new ErrorResponse(400, "Error: bad request");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
        try {
            var regResponse = mainService.registerUser(regRequest);
            if (regResponse instanceof RegisterResponse) {
                res.status(200);
                return new Gson().toJson(regResponse);
            } else if (regResponse instanceof ErrorResponse) {
                res.status(((ErrorResponse) regResponse).status());
                return new Gson().toJson(regResponse);
            } else {
                ErrorResponse failedRequest = new ErrorResponse(400, "Error: bad request");
                res.status(failedRequest.status());
                return new Gson().toJson(failedRequest);
            }
        } catch (DataAccessException e) {
            ErrorResponse failedRequest = new ErrorResponse(500, "Error: " + e.getMessage());
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }

    }

    private Object loginHandler(Request req, Response res){
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        if (loginRequest.username() == null || loginRequest.password() == null) {
            ErrorResponse failedRequest = new ErrorResponse(500, "Error: bad request");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
        try {
            var loginResponse = mainService.loginUser(loginRequest);
            if (loginResponse instanceof LoginResponse) {
                res.status(200);
                return new Gson().toJson(loginResponse);
            } else if (loginResponse instanceof ErrorResponse) {
                res.status(((ErrorResponse) loginResponse).status());
                return new Gson().toJson(loginResponse);
            } else {
                ErrorResponse failedRequest = new ErrorResponse(500, "Error: issue getting correct Response");
                res.status(failedRequest.status());
                return new Gson().toJson(failedRequest);
            }
        } catch (DataAccessException e) {
            ErrorResponse failedRequest = new ErrorResponse(500, e.getMessage());
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
    }

    private Object logoutHandler(Request req, Response res){
        var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        var logoutResponse = mainService.logoutUser(logoutRequest);
        if (logoutResponse == null) {
            res.status(200);
            return "";
        } else if (logoutResponse instanceof ErrorResponse) {
            res.status(((ErrorResponse) logoutResponse).status());
            return new Gson().toJson(logoutResponse);
        } else {
            ErrorResponse failedRequest = new ErrorResponse(500, "Error: issue getting correct Response");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
    }

    private Object createGameHandler(Request req, Response res) {
        var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        var jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
        String gameName = jsonObject.get("gameName").getAsString();
        CreateGameRequest cgRequest = new CreateGameRequest(authToken, gameName);
        var cgResponse = mainService.createNewGame(cgRequest);
        if (cgResponse instanceof CreateGameResponse) {
            res.status(200);
            return new Gson().toJson(cgResponse);
        } else if (cgResponse instanceof ErrorResponse) {
            res.status(((ErrorResponse) cgResponse).status());
            return new Gson().toJson(cgResponse);
        } else {
            ErrorResponse failedRequest = new ErrorResponse(500, "Error: issue getting correct Response");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
    }

    private Object joinHandler(Request req, Response res){
        String authToken;
        try {
            authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        } catch (Exception e) {
            ErrorResponse failedRequest = new ErrorResponse(401, "Error: unauthorized");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
        var jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
        String playerColor;
        int gameID;
        try {
            playerColor = jsonObject.get("playerColor").getAsString();
            gameID = jsonObject.get("gameID").getAsInt();
        } catch (NullPointerException e) {
            ErrorResponse failedRequest = new ErrorResponse(400, "Error: bad request");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
        JoinRequest joinRequest = new JoinRequest(authToken, playerColor, gameID);
        var joinResponse = mainService.joinGame(joinRequest);
        if (joinResponse instanceof JoinResponse) {
            res.status(200);
            return "";
        }
        else if (joinResponse instanceof ErrorResponse){
            res.status(((ErrorResponse) joinResponse).status());
            return new Gson().toJson(joinResponse);
        }
        else {
            ErrorResponse failedRequest = new ErrorResponse(500, "Error: issue getting correct Response");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
    }

    private Object listHandler(Request req, Response res) {
        var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        ListRequest lisReq = new ListRequest(authToken);
        var lisRes = mainService.listAllGames(lisReq);
        if (lisRes instanceof ListResponse) {
            res.status(200);
            return new Gson().toJson(lisRes);
        }
        if (lisRes instanceof ErrorResponse) {
            res.status(((ErrorResponse) lisRes).status());
            return new Gson().toJson(lisRes);
        }
        ErrorResponse failedRequest = new ErrorResponse(500, "Error: issue getting correct Response");
        res.status(failedRequest.status());
        return new Gson().toJson(failedRequest);
    }
}

