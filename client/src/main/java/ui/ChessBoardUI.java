package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    static chess.ChessBoard currentBoard;
    private static final Map<String, String> PIECE_SYMBOLS = new HashMap<>();

    static {
        PIECE_SYMBOLS.put("PAWN", WHITE_PAWN);
        PIECE_SYMBOLS.put("ROOK", WHITE_ROOK);
        PIECE_SYMBOLS.put("KNIGHT", WHITE_KNIGHT);
        PIECE_SYMBOLS.put("BISHOP", WHITE_BISHOP);
        PIECE_SYMBOLS.put("QUEEN", WHITE_QUEEN);
        PIECE_SYMBOLS.put("KING", WHITE_KING);
    }

    public ChessBoardUI (chess.ChessBoard currentBoard){
        ChessBoardUI.currentBoard = currentBoard;
    }

    public void run(String role, Set<ChessPosition> possPos, ChessPosition currPos) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.println();

        if (role == null || role.equals("WHITE")) {
            drawWhiteBoard(out, possPos, currPos);
        }
        else if (role.equals("BLACK")) {
            drawBlackBoard(out, possPos, currPos);
        }

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);

    }

    private static String getPieceSymbol(ChessPiece.PieceType type) {
        return PIECE_SYMBOLS.getOrDefault(type.name(), " ");
    }

    private static void printAlphaText(PrintStream out, ChessGame.TeamColor currColor) {
        String[] alpha = {" a "," b "," c "," d "," e ", " f ", " g ", " h "};
        out.print(SET_BG_COLOR_BLUEBERRY);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(EMPTY);
        if (currColor == ChessGame.TeamColor.WHITE) {
            for (int place=0; place < 8; place++) {
                out.print(alpha[place]);
            }
        }
        else {
            for (int place=7; place >= 0; place--) {
                out.print(alpha[place]);
            }
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawSquare(PrintStream out, int row, int col, boolean blackSquare, boolean highlight, ChessPosition currPos) {
        if (blackSquare) {
            if (highlight) {
                out.print(SET_BG_COLOR_DARK_MAGENTA);
            } else {
                out.print(SET_BG_COLOR_PERSIAN_BLUE);
            }
        }
        else {
            if (highlight) {
                out.print(SET_BG_COLOR_PEARLY_PURPLE);
            } else {
                out.print(SET_BG_COLOR_PALE_BLUE);
            }
        }
        if (currPos != null && row == currPos.getRow() && col == currPos.getColumn()) {
            out.print(SET_BG_COLOR_YELLOW);
        }
        ChessPiece currPiece = currentBoard.getPiece(new ChessPosition(row, col));
        if (currPiece != null) {
            ChessGame.TeamColor currColor = currPiece.getTeamColor();
            if (currColor == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_WHITE);
            }
            else {
                out.print(SET_TEXT_COLOR_BLACK);
            }
            out.print(getPieceSymbol(currPiece.getPieceType()));
        }
        else {
            out.print(EMPTY);
        }
    }

    private static void drawFrontOfRow(PrintStream out, int row) {
        out.print(SET_BG_COLOR_BLUEBERRY);
        out.print(SET_TEXT_COLOR_BLUE);
        out.printf(" %d ", row);
    }

    public static void drawWhiteBoard(PrintStream out, Set<ChessPosition> possPos, ChessPosition currPos) {
        printAlphaText(out, ChessGame.TeamColor.WHITE);
        boolean blackSquare = true;
        for (int row = 8; row >= 1; row--) {
            drawFrontOfRow(out, row);
            blackSquare = !blackSquare;
            for (int col = 1; col <= 8; col++) {
                determineSquareDraw(out, possPos, currPos, row, col, blackSquare);
                blackSquare = !blackSquare;
            }
            drawFrontOfRow(out, row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
        printAlphaText(out, ChessGame.TeamColor.WHITE);
    }

    public static void drawBlackBoard(PrintStream out, Set<ChessPosition> possPos, ChessPosition currPos) {
        printAlphaText(out, ChessGame.TeamColor.BLACK);
        boolean blackSquare = true;
        for (int row = 1; row <= 8; row++) {
            drawFrontOfRow(out, row);
            blackSquare = !blackSquare;
            for (int col = 8; col >= 1; col--) {
                determineSquareDraw(out, possPos, currPos, row, col, blackSquare);
                blackSquare = !blackSquare;
            }
            drawFrontOfRow(out, row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
        printAlphaText(out, ChessGame.TeamColor.BLACK);
    }

    private static void determineSquareDraw(PrintStream out, Set<ChessPosition> possPos,
                                            ChessPosition currPos, int row, int col, boolean blackSquare) {
        if (possPos != null && possPos.contains(new ChessPosition(row, col))) {
            drawSquare(out, row, col, blackSquare, true, currPos);
        }
        else {
            drawSquare(out, row, col, blackSquare, false, currPos);
        }
    }
}
