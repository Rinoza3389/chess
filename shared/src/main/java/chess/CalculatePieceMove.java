package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return BishopMovesCalculator.pieceMoves(board, position);
        }
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return RookMovesCalculator.pieceMoves(board, position);
        }
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return QueenMovesCalculator.pieceMoves(board, position);
        }
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
            return KingMovesCalculator.pieceMoves(board, position);
        }
        else if(currentPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return KnightMovesCalculator.pieceMoves(board, position);
        }




        return new ArrayList<>();
    }
}

class PawnMovesCalculator {

    public PawnMovesCalculator() {}

    public static ArrayList<ChessMove> checkPromoWhite(ChessPosition startPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (endPosition.getRow() == 8) {
            ArrayList<ChessPiece.PieceType> pieceTypes = new ArrayList<ChessPiece.PieceType>(Arrays.asList(ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN));
            for (ChessPiece.PieceType promoType : pieceTypes){
                ChessMove promoMove = new ChessMove(startPosition, endPosition, promoType);
                possibleMoves.add(promoMove);
            }
        }
        else {
            ChessMove notPromoMove = new ChessMove(startPosition, endPosition, null);
            possibleMoves.add(notPromoMove);
        }

        return possibleMoves;
    }

    public static ArrayList<ChessMove> checkPromoBlack(ChessPosition startPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (endPosition.getRow() == 1) {
            ArrayList<ChessPiece.PieceType> pieceTypes = new ArrayList<ChessPiece.PieceType>(Arrays.asList(ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN));
            for (ChessPiece.PieceType promoType : pieceTypes){
                ChessMove promoMove = new ChessMove(startPosition, endPosition, promoType);
                possibleMoves.add(promoMove);
            }
        }
        else {
            ChessMove notPromoMove = new ChessMove(startPosition, endPosition, null);
            possibleMoves.add(notPromoMove);
        }

        return possibleMoves;
    }


    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES HERE
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (position.getRow() + 1 <= 8) {
                if (board.getPiece(moveOnePosition) == null) {
                    possibleMoves = checkPromoWhite(position, moveOnePosition);
                }
            }
            if (position.getRow() == 2) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() + 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null && board.getPiece(moveOnePosition) == null) {
                    possibleMoves.addAll(checkPromoWhite(position, moveTwoPosition));
                }
            }
            if (position.getRow() + 1 <= 8 && position.getColumn() - 1 >= 1) {
                ChessPosition diagLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    possibleMoves.addAll(checkPromoWhite(position, diagLeft));
                }
            }
            if (position.getRow() + 1 <= 8 && position.getColumn() + 1 <= 8) {
                ChessPosition diagRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    possibleMoves.addAll(checkPromoWhite(position, diagRight));
                }
            }
        }

        //BLACK PIECES HERE!!!
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (moveOnePosition.getRow() >= 1) {
                if (board.getPiece(moveOnePosition) == null) {
                    possibleMoves = checkPromoBlack(position, moveOnePosition);
                }
            }
            if (position.getRow() == 7) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() - 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null && board.getPiece(moveOnePosition) == null) {
                    possibleMoves.addAll(checkPromoBlack(position, moveTwoPosition));
                }
            }
            if (position.getRow() - 1 >= 1 && position.getColumn() - 1 >= 1) {
                ChessPosition diagLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    possibleMoves.addAll(checkPromoBlack(position, diagLeft));
                }
            }
            if (position.getRow() - 1 >= 1 && position.getColumn() + 1 <= 8) {
                ChessPosition diagRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    possibleMoves.addAll(checkPromoBlack(position, diagRight));
                }
            }
        }

//        for (ChessMove moveOption : possibleMoves) {
//            System.out.printf("(%d, %d)", moveOption.getEndPosition().getRow(), moveOption.getEndPosition().getColumn());
//        }
        return possibleMoves;

    }
}

class BishopMovesCalculator {
    public BishopMovesCalculator() {}

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES HERE
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //Moving to the forward left
            int current_row = position.getRow();
            int current_col = position.getColumn();
            ChessPosition future_position = new ChessPosition(current_row + 1, current_col - 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row + 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the forward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row + 1, current_col + 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row + 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the backward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col + 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row - 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the backward left
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col - 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row - 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }
        }

        //BLACK PIECES HERE
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            //Moving to the backward left
            int current_row = position.getRow();
            int current_col = position.getColumn();
            ChessPosition future_position = new ChessPosition(current_row + 1, current_col - 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row + 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the backward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row + 1, current_col + 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row + 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the forward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col + 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row - 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the forward left
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col - 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row - 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }
        }

        return possibleMoves;
    }
}

class RookMovesCalculator {
    public RookMovesCalculator() {
    }

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {

            //Moving forward
            int future_row = position.getRow() + 1;
            int future_col = position.getColumn();
            ChessPosition future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    future_row += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    break;
                }
                else { break; }
            }

            //Moving backward
            future_row = position.getRow() - 1;
            future_col = position.getColumn();
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    future_row -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    break;
                }
                else { break; }
            }

            //Moving left
            future_row = position.getRow();
            future_col = position.getColumn() - 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    future_col -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    break;
                }
                else { break; }
            }

            //Moving right
            future_row = position.getRow();
            future_col = position.getColumn() + 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    future_col += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    break;
                }
                else { break; }
            }
        }

        //BLACK PIECES
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {

            //Moving backward
            int future_row = position.getRow() + 1;
            int future_col = position.getColumn();
            ChessPosition future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    future_row += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    break;
                }
                else { break; }
            }

            //Moving forward
            future_row = position.getRow() - 1;
            future_col = position.getColumn();
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    future_row -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    break;
                }
                else { break; }
            }

            //Moving left
            future_row = position.getRow();
            future_col = position.getColumn() - 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    future_col -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    break;
                }
                else { break; }
            }

            //Moving right
            future_row = position.getRow();
            future_col = position.getColumn() + 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    future_col += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    break;
                }
                else { break; }
            }
        }

        return possibleMoves;
    }
}

class QueenMovesCalculator {

    public QueenMovesCalculator() {
    }

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {

            //Moving forward
            int future_row = position.getRow() + 1;
            int future_col = position.getColumn();
            ChessPosition future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    future_row += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    break;
                }
                else { break; }
            }

            //Moving backward
            future_row = position.getRow() - 1;
            future_col = position.getColumn();
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    future_row -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    break;
                }
                else { break; }
            }

            //Moving left
            future_row = position.getRow();
            future_col = position.getColumn() - 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    future_col -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    break;
                }
                else { break; }
            }

            //Moving right
            future_row = position.getRow();
            future_col = position.getColumn() + 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    future_col += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    break;
                }
                else { break; }
            }
            int current_row = position.getRow();
            int current_col = position.getColumn();
            future_position = new ChessPosition(current_row + 1, current_col - 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row + 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the forward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row + 1, current_col + 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row + 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the backward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col + 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row - 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the backward left
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col - 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row - 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }
        }

        //BLACK PIECES
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {

            //Moving backward
            int future_row = position.getRow() + 1;
            int future_col = position.getColumn();
            ChessPosition future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    future_row += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForward);
                    break;
                }
                else { break; }
            }

            //Moving forward
            future_row = position.getRow() - 1;
            future_col = position.getColumn();
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getRow() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    future_row -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveBackward = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveBackward);
                    break;
                }
                else { break; }
            }

            //Moving left
            future_row = position.getRow();
            future_col = position.getColumn() - 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    future_col -= 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveLeft);
                    break;
                }
                else { break; }
            }

            //Moving right
            future_row = position.getRow();
            future_col = position.getColumn() + 1;
            future_position = new ChessPosition(future_row, future_col);
            while (future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    future_col += 1;
                    future_position = new ChessPosition(future_row, future_col);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveRight = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveRight);
                    break;
                }
                else { break; }
            }
            int current_row = position.getRow();
            int current_col = position.getColumn();
            future_position = new ChessPosition(current_row + 1, current_col - 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row + 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the backward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row + 1, current_col + 1);
            while (future_position.getRow() <= 8 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row += 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row + 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the forward right
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col + 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() <= 8) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col += 1;
                    future_position = new ChessPosition(current_row - 1, current_col + 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }

            //Moving to the forward left
            current_row = position.getRow();
            current_col = position.getColumn();
            future_position = new ChessPosition(current_row - 1, current_col - 1);
            while (future_position.getRow() >= 1 && future_position.getColumn() >= 1) {
                if  (board.getPiece(future_position) == null) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    current_row -= 1;
                    current_col -= 1;
                    future_position = new ChessPosition(current_row - 1, current_col - 1);
                }
                else if (board.getPiece(future_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForeLeft = new ChessMove(position, future_position, null);
                    possibleMoves.add(moveForeLeft);
                    break;
                }
                else { break; }
            }
        }

        return possibleMoves;
    }
}

class KingMovesCalculator {

    public KingMovesCalculator() {
    }

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES HERE
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //Forward
            if (position.getRow() <= 7) {
                ChessPosition moveForwardPos = new ChessPosition(position.getRow() + 1, position.getColumn());
                if (board.getPiece(moveForwardPos) == null || board.getPiece(moveForwardPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForward = new ChessMove(position, moveForwardPos, null);
                    possibleMoves.add(moveForward);
                }
            }

            //Backward
            if (position.getRow() >= 2) {
                ChessPosition moveBackwardPos = new ChessPosition(position.getRow() - 1, position.getColumn());
                if (board.getPiece(moveBackwardPos) == null || board.getPiece(moveBackwardPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveBackward = new ChessMove(position, moveBackwardPos, null);
                    possibleMoves.add(moveBackward);
                }
            }

            //Left
            if (position.getColumn() >= 2) {
                ChessPosition moveLeftPos = new ChessPosition(position.getRow(), position.getColumn() - 1);
                if (board.getPiece(moveLeftPos) == null || board.getPiece(moveLeftPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveLeft = new ChessMove(position, moveLeftPos, null);
                    possibleMoves.add(moveLeft);
                }
            }

            //Right
            if (position.getColumn() <= 7) {
                ChessPosition moveRightPos = new ChessPosition(position.getRow(), position.getColumn() + 1);
                if (board.getPiece(moveRightPos) == null || board.getPiece(moveRightPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveRight = new ChessMove(position, moveRightPos, null);
                    possibleMoves.add(moveRight);
                }
            }

            //Forward left
            if (position.getColumn() >= 2 && position.getRow() <= 7) {
                ChessPosition moveForLeftPos = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                if (board.getPiece(moveForLeftPos) == null || board.getPiece(moveForLeftPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForLeft = new ChessMove(position, moveForLeftPos, null);
                    possibleMoves.add(moveForLeft);
                }
            }

            //Forward right
            if (position.getRow() <= 7 && position.getColumn() <= 7) {
                ChessPosition moveForRightPos = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                if (board.getPiece(moveForRightPos) == null || board.getPiece(moveForRightPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveForRight = new ChessMove(position, moveForRightPos, null);
                    possibleMoves.add(moveForRight);
                }
            }

            //Backward left
            if (position.getRow() >= 2 && position.getColumn() >= 2) {
                ChessPosition moveBackLeftPos = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (board.getPiece(moveBackLeftPos) == null || board.getPiece(moveBackLeftPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveBackLeft = new ChessMove(position, moveBackLeftPos, null);
                    possibleMoves.add(moveBackLeft);
                }
            }

            //Backward right
            if (position.getRow() >= 2 && position.getColumn() <= 7) {
                ChessPosition moveBackRightPos = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (board.getPiece(moveBackRightPos) == null || board.getPiece(moveBackRightPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove moveBackRight = new ChessMove(position, moveBackRightPos, null);
                    possibleMoves.add(moveBackRight);
                }
            }
        }

        //BLACK PIECES HERE
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            //Forward
            if (position.getRow() >= 2) {
                ChessPosition moveForwardPos = new ChessPosition(position.getRow() - 1, position.getColumn());
                if (board.getPiece(moveForwardPos) == null || board.getPiece(moveForwardPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForward = new ChessMove(position, moveForwardPos, null);
                    possibleMoves.add(moveForward);
                }
            }

            //Backward
            if (position.getRow() <= 7) {
                ChessPosition moveBackwardPos = new ChessPosition(position.getRow() + 1, position.getColumn());
                if (board.getPiece(moveBackwardPos) == null || board.getPiece(moveBackwardPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveBackward = new ChessMove(position, moveBackwardPos, null);
                    possibleMoves.add(moveBackward);
                }
            }

            //Left
            if (position.getColumn() >= 2) {
                ChessPosition moveLeftPos = new ChessPosition(position.getRow(), position.getColumn() - 1);
                if (board.getPiece(moveLeftPos) == null || board.getPiece(moveLeftPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveLeft = new ChessMove(position, moveLeftPos, null);
                    possibleMoves.add(moveLeft);
                }
            }

            //Right
            if (position.getColumn() <= 7) {
                ChessPosition moveRightPos = new ChessPosition(position.getRow(), position.getColumn() + 1);
                if (board.getPiece(moveRightPos) == null || board.getPiece(moveRightPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveRight = new ChessMove(position, moveRightPos, null);
                    possibleMoves.add(moveRight);
                }
            }

            //Forward left
            if (position.getRow() >= 2 && position.getColumn() >= 2) {
                ChessPosition moveForLeftPos = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (board.getPiece(moveForLeftPos) == null || board.getPiece(moveForLeftPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForLeft = new ChessMove(position, moveForLeftPos, null);
                    possibleMoves.add(moveForLeft);
                }
            }

            //Forward right
            if (position.getRow() >= 2 && position.getColumn() <= 7) {
                ChessPosition moveForRightPos = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (board.getPiece(moveForRightPos) == null || board.getPiece(moveForRightPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveForRight = new ChessMove(position, moveForRightPos, null);
                    possibleMoves.add(moveForRight);
                }
            }

            //Backward left
            if (position.getRow() <= 7 && position.getColumn() >= 2) {
                ChessPosition moveBackLeftPos = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                if (board.getPiece(moveBackLeftPos) == null || board.getPiece(moveBackLeftPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveBackLeft = new ChessMove(position, moveBackLeftPos, null);
                    possibleMoves.add(moveBackLeft);
                }
            }

            //Backward right
            if (position.getRow() <= 7 && position.getColumn() <= 7) {
                ChessPosition moveBackRightPos = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                if (board.getPiece(moveBackRightPos) == null || board.getPiece(moveBackRightPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveBackRight = new ChessMove(position, moveBackRightPos, null);
                    possibleMoves.add(moveBackRight);
                }
            }
        }
        return possibleMoves;
    }
}

class KnightMovesCalculator {
    public KnightMovesCalculator() {}

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);

        //WHITE PIECES HERE
        if (board.getPiece(position).getTeamColor()==ChessGame.TeamColor.WHITE) {
            //Forward2 Left1
            if (position.getRow() + 2 <= 8 && position.getColumn() - 1 >= 1) {
                ChessPosition for2left1Pos = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
                if (board.getPiece(for2left1Pos) == null || board.getPiece(for2left1Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove for2left1 = new ChessMove(position, for2left1Pos, null);
                    possibleMoves.add(for2left1);
                }
            }

            //Forward2 Right1
            if (position.getRow() + 2 <= 8 && position.getColumn() + 1 <= 8) {
                ChessPosition for2right1Pos = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
                if (board.getPiece(for2right1Pos) == null || board.getPiece(for2right1Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove for2right1 = new ChessMove(position, for2right1Pos, null);
                    possibleMoves.add(for2right1);
                }
            }

            //Backward2 left1
            if (position.getRow() - 2 >= 1 && position.getColumn() - 1 >= 1) {
                ChessPosition back2left1Pos = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
                if (board.getPiece(back2left1Pos) == null || board.getPiece(back2left1Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove back2left1 = new ChessMove(position, back2left1Pos, null);
                    possibleMoves.add(back2left1);
                }
            }

            //Backward2 right1
            if (position.getRow() - 2 >= 1 && position.getColumn() + 1 <= 8) {
                ChessPosition back2right1Pos = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
                if (board.getPiece(back2right1Pos) == null || board.getPiece(back2right1Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove back2right1 = new ChessMove(position, back2right1Pos, null);
                    possibleMoves.add(back2right1);
                }
            }

            //Forward1 Left2
            if (position.getRow() + 1 <= 8 && position.getColumn() - 2 >= 1) {
                ChessPosition for1left2Pos = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
                if (board.getPiece(for1left2Pos) == null || board.getPiece(for1left2Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove for1left2 = new ChessMove(position, for1left2Pos, null);
                    possibleMoves.add(for1left2);
                }
            }

            //Forward1 Right2
            if (position.getRow() + 1 <= 8 && position.getColumn() + 2 <= 8) {
                ChessPosition for1right2Pos = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
                if (board.getPiece(for1right2Pos) == null || board.getPiece(for1right2Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove for1right2 = new ChessMove(position, for1right2Pos, null);
                    possibleMoves.add(for1right2);
                }
            }

            //Backward1 left2
            if (position.getRow() - 1 >= 1 && position.getColumn() - 2 >= 1) {
                ChessPosition back1left2Pos = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
                if (board.getPiece(back1left2Pos) == null || board.getPiece(back1left2Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove back1left2 = new ChessMove(position, back1left2Pos, null);
                    possibleMoves.add(back1left2);
                }
            }

            //Backward1 right2
            if (position.getRow() - 1 >= 1 && position.getColumn() + 2 <= 8) {
                ChessPosition back1right2Pos = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
                if (board.getPiece(back1right2Pos) == null || board.getPiece(back1right2Pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove back1right2 = new ChessMove(position, back1right2Pos, null);
                    possibleMoves.add(back1right2);
                }
            }
        }

        //BLACK PIECES HERE
        if (board.getPiece(position).getTeamColor()==ChessGame.TeamColor.BLACK) {
            //Forward2 Left1
            if (position.getRow() - 2 >= 1 && position.getColumn() - 1 >= 1) {
                ChessPosition for2left1Pos = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
                if (board.getPiece(for2left1Pos) == null || board.getPiece(for2left1Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove for2left1 = new ChessMove(position, for2left1Pos, null);
                    possibleMoves.add(for2left1);
                }
            }

            //Forward2 Right1
            if (position.getRow() - 2 >= 1 && position.getColumn() + 1 <= 8) {
                ChessPosition for2right1Pos = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
                if (board.getPiece(for2right1Pos) == null || board.getPiece(for2right1Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove for2right1 = new ChessMove(position, for2right1Pos, null);
                    possibleMoves.add(for2right1);
                }
            }

            //Backward2 left1
            if (position.getRow() + 2 <= 8 && position.getColumn() - 1 >= 1) {
                ChessPosition back2left1Pos = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
                if (board.getPiece(back2left1Pos) == null || board.getPiece(back2left1Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove back2left1 = new ChessMove(position, back2left1Pos, null);
                    possibleMoves.add(back2left1);
                }
            }

            //Backward2 right1
            if (position.getRow() + 2 <= 8 && position.getColumn() + 1 <= 8) {
                ChessPosition back2right1Pos = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
                if (board.getPiece(back2right1Pos) == null || board.getPiece(back2right1Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove back2right1 = new ChessMove(position, back2right1Pos, null);
                    possibleMoves.add(back2right1);
                }
            }

            //Forward1 Left2
            if (position.getRow() - 1 >= 1 && position.getColumn() - 2 >= 1) {
                ChessPosition for1left2Pos = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
                if (board.getPiece(for1left2Pos) == null || board.getPiece(for1left2Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove for1left2 = new ChessMove(position, for1left2Pos, null);
                    possibleMoves.add(for1left2);
                }
            }

            //Forward1 Right2
            if (position.getRow() - 1 >= 1 && position.getColumn() + 2 <= 8) {
                ChessPosition for1right2Pos = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
                if (board.getPiece(for1right2Pos) == null || board.getPiece(for1right2Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove for1right2 = new ChessMove(position, for1right2Pos, null);
                    possibleMoves.add(for1right2);
                }
            }

            //Backward1 left2
            if (position.getRow() + 1 <= 8 && position.getColumn() - 2 >= 1) {
                ChessPosition back1left2Pos = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
                if (board.getPiece(back1left2Pos) == null || board.getPiece(back1left2Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove back1left2 = new ChessMove(position, back1left2Pos, null);
                    possibleMoves.add(back1left2);
                }
            }

            //Backward1 right2
            if (position.getRow() + 1 <= 8 && position.getColumn() + 2 <= 8) {
                ChessPosition back1right2Pos = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
                if (board.getPiece(back1right2Pos) == null || board.getPiece(back1right2Pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove back1right2 = new ChessMove(position, back1right2Pos, null);
                    possibleMoves.add(back1right2);
                }
            }
        }

        return possibleMoves;
    }
}