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
        mainService.clear();
        res.status(200);
        return "";
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), model.UserData.class);
        mainService.registerUser(user);
        res.status(200);
        return "I need to have it output user & authtoken";
    }
}