package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Used to actually calculate the moves available for each piece and holds a lot of subclasses
 */

public class CalculatePieceMove {

    public CalculatePieceMove() {}

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece currentPiece = board.getPiece(position);
        if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return PawnMovesCalculator.pieceMoves(board, position);
        }




        return new ArrayList<>();
    }
}

class PawnMovesCalculator {

    public PawnMovesCalculator() {}

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES HERE
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() == 1) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() + 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null) {
                    ChessMove moveTwoSpace = new ChessMove(position, moveTwoPosition, currentPiece.getPieceType());
                    possibleMoves.add(moveTwoSpace);
                }
            }
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (board.getPiece(moveOnePosition) == null) {
                ChessMove moveOneSpace = new ChessMove(position, moveOnePosition, currentPiece.getPieceType());
                possibleMoves.add(moveOneSpace);
            }
            ChessPosition diagLeft = new ChessPosition(position.getRow() + 1, position.getColumn() -1);
            if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                ChessMove moveDiagLeft = new ChessMove(position, diagLeft, currentPiece.getPieceType());
                possibleMoves.add(moveDiagLeft);
            }
            ChessPosition diagRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                ChessMove moveDiagRight = new ChessMove(position, diagRight, currentPiece.getPieceType());
                possibleMoves.add(moveDiagRight);
            }
        }

        //BLACK PIECES HERE!!!
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (position.getRow() == 6) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() - 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null) {
                    ChessMove moveTwoSpace = new ChessMove(position, moveTwoPosition, currentPiece.getPieceType());
                    possibleMoves.add(moveTwoSpace);
                }
            }
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (board.getPiece(moveOnePosition) == null) {
                ChessMove moveOneSpace = new ChessMove(position, moveOnePosition, currentPiece.getPieceType());
                possibleMoves.add(moveOneSpace);
            }
            if (position.getRow() - 1 >= 1) {
                ChessPosition diagLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveDiagLeft = new ChessMove(position, diagLeft, currentPiece.getPieceType());
                    possibleMoves.add(moveDiagLeft);
                }
            }
            if (position.getRow() + 1 <= 8) {
                ChessPosition diagRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveDiagRight = new ChessMove(position, diagRight, currentPiece.getPieceType());
                    possibleMoves.add(moveDiagRight);
                }
            }
        }

        return possibleMoves;

    }
}