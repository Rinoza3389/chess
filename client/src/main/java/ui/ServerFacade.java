package ui;

import server.*;

import java.io.IOException;

public class ServerFacade {

    public Object registerFacade(RegisterRequest regReq) {
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var regRes = cliCom.doPost("http://localhost:8080/user", regReq);
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
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var logRes = cliCom.doDelete("http://localhost:8080/session", logReq);
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
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var crRes = cliCom.doPost("http://localhost:8080/game", crReq);
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
        ClientCommunicator cliCom = new ClientCommunicator();
        try {
            var lisRes = cliCom.doGet("http://localhost:8080/game", lisReq);
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
}
