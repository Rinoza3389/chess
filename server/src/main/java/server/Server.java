package server;

import com.google.gson.Gson;
import spark.*;
import dataaccess.DataAccessException;
import service.*;

import javax.xml.crypto.Data;

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
}