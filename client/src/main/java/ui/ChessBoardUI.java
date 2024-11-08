package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    chess.ChessBoard currentBoard;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawWhiteBoard(out);
//        drawBlackBoard(out);

        out.print(RESET_BG_COLOR);
    }

    private static void drawWhiteBoard(PrintStream out) {

    }
}
