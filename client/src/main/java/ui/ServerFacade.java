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
        return "urmom";
    }
}
