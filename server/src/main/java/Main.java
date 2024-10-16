import chess.*;
import server.Server;
import spark.Spark;
import dataaccess.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        Server mainServer = new Server();
        mainServer.run(8080);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}

