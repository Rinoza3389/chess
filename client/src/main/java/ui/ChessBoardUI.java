package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    static chess.ChessBoard currentBoard;
    private static final Map<String, String> pieceSymbols = new HashMap<>();

    static {
        pieceSymbols.put("PAWN", WHITE_PAWN);
        pieceSymbols.put("ROOK", WHITE_ROOK);
        pieceSymbols.put("KNIGHT", WHITE_KNIGHT);
        pieceSymbols.put("BISHOP", WHITE_BISHOP);
        pieceSymbols.put("QUEEN", WHITE_QUEEN);
        pieceSymbols.put("KING", WHITE_KING);
    }

    public ChessBoardUI (chess.ChessBoard currentBoard){
        this.currentBoard = currentBoard;
    }

    public static void main() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawWhiteBoard(out);
        out.println();
        drawBlackBoard(out);

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static String getPieceSymbol(ChessPiece.PieceType type) {
        return pieceSymbols.getOrDefault(type.name(), " ");
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

    private static void drawSquare(PrintStream out, int row, int col, boolean blackSquare) {
        if (blackSquare) {
            out.print(SET_BG_COLOR_PERSIAN_BLUE);
        }
        else {
            out.print(SET_BG_COLOR_PALE_BLUE);
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

    private static void drawWhiteBoard(PrintStream out) {
        printAlphaText(out, ChessGame.TeamColor.WHITE);
        boolean blackSquare = true;
        for (int row = 8; row >= 1; row--) {
            drawFrontOfRow(out, row);
            blackSquare = !blackSquare;
            for (int col = 1; col <= 8; col++) {
                drawSquare(out, row, col, blackSquare);
                blackSquare = !blackSquare;
            }
            drawFrontOfRow(out, row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
        printAlphaText(out, ChessGame.TeamColor.WHITE);
    }

    private static void drawBlackBoard(PrintStream out) {
        printAlphaText(out, ChessGame.TeamColor.BLACK);
        boolean blackSquare = true;
        for (int row = 1; row <= 8; row++) {
            drawFrontOfRow(out, row);
            blackSquare = !blackSquare;
            for (int col = 8; col >= 1; col--) {
                drawSquare(out, row, col, blackSquare);
                blackSquare = !blackSquare;
            }
            drawFrontOfRow(out, row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
        printAlphaText(out, ChessGame.TeamColor.BLACK);
    }
}
