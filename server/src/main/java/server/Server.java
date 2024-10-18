package server;

import com.google.gson.Gson;
import spark.*;
import dataaccess.DataAccessException;
import service.*;



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

    private Object registerHandler(Request req, Response res) throws DataAccessException {
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
            ErrorResponse failedRequest = new ErrorResponse(400, "Error: bad request");
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }

    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
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

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
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
        } catch (DataAccessException e) {
            ErrorResponse failedRequest = new ErrorResponse(500, e.getMessage());
            res.status(failedRequest.status());
            return new Gson().toJson(failedRequest);
        }
    }
}

