package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawWhiteBoard(out);
//        drawBlackBoard(out);

        out.print(RESET_BG_COLOR);
    }

    private static String getPieceSymbol(ChessPiece.PieceType type) {
        return pieceSymbols.getOrDefault(type.name(), " ");
    }

    private static void printAlphaText(PrintStream out, ChessGame.TeamColor currColor) {
        String[] alpha = {" a "," b "," c "," d "," e ", " f ", " g ", " h "};
        out.print(SET_BG_COLOR_BLUEBERRY);
        out.print(SET_TEXT_COLOR_BLACK);
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

    private static void drawWhiteBoard(PrintStream out) {
        printAlphaText(out, ChessGame.TeamColor.WHITE);
        boolean blackSquare = true;
        for (int row = 8; row >= 1; row--) {
            out.print(SET_BG_COLOR_BLUEBERRY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %d ", row);
            blackSquare = !blackSquare;
            for (int col = 1; col <= 8; col++) {
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
                blackSquare = !blackSquare;
            }
            out.print(SET_BG_COLOR_BLUEBERRY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %d ", row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
        printAlphaText(out, ChessGame.TeamColor.WHITE);
    }
}
