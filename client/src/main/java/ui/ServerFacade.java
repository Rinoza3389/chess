package ui;

import server.*;

import java.io.IOException;

public class ServerFacade {
    private Integer port = 8080;

    public ServerFacade(Integer port) {
        this.port = port;
    }

    public Object registerFacade(RegisterRequest regReq) {
        if (regReq.username() == null || regReq.password() == null || regReq.email() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var regRes = cliCom.doPost("http://localhost:"+port+"/user", regReq);
            if (regRes instanceof RegisterResponse) {
                return regRes;
            }
            else if (regRes instanceof ErrorResponse) {
                return ((ErrorResponse) regRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Object loginFacade(LoginRequest logReq) {
        if (logReq.username() == null || logReq.password() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var logRes = cliCom.doPost("http://localhost:8080/session", logReq);
            if (logRes instanceof LoginResponse) {
                return logRes;
            }
            else if (logRes instanceof ErrorResponse) {
                return ((ErrorResponse) logRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Object logoutFacade(LogoutRequest logReq) {
        if (logReq.authToken() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var logRes = cliCom.doDelete("http://localhost:"+port+"/session", logReq);
            if (logRes instanceof ErrorResponse) {
                return ((ErrorResponse) logRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    public Object createGameFacade(CreateGameRequest crReq) {
        if (crReq.authToken() == null || crReq.gameName() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var crRes = cliCom.doPost("http://localhost:"+port+"/game", crReq);
            if (crRes instanceof CreateGameResponse) {
                return crRes;
            }
            else if (crRes instanceof ErrorResponse) {
                return ((ErrorResponse) crRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Object listFacade(ListRequest lisReq) {
        if (lisReq.authToken() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }

        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var lisRes = cliCom.doGet("http://localhost:"+port+"/game", lisReq);
            if (lisRes instanceof ListResponse) {
                return lisRes;
            }
            else if (lisRes instanceof ErrorResponse) {
                return ((ErrorResponse) lisRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Object joinFacade(JoinRequest joinReq) {
        if (joinReq.authToken() == null || joinReq.gameID() == null || joinReq.playerColor() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var joinRes = cliCom.doPut("http://localhost:"+port+"/game", joinReq);
            if (joinRes instanceof ErrorResponse) {
                return ((ErrorResponse) joinRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Object clearFacade() {
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var logRes = cliCom.doDelete("http://localhost:"+port+"/db", null);
            if (logRes instanceof ErrorResponse) {
                return ((ErrorResponse) logRes).message();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }
}
