package chess;

import java.util.ArrayList;
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
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (board.getPiece(moveOnePosition) == null) {
                ChessMove moveOneSpace = new ChessMove(position, moveOnePosition, null);
                possibleMoves.add(moveOneSpace);
            }
            if (position.getRow() == 2) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() + 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null && board.getPiece(moveOnePosition) == null) {
                    ChessMove moveTwoSpace = new ChessMove(position, moveTwoPosition, null);
                    possibleMoves.add(moveTwoSpace);
                }
            }
            ChessPosition diagLeft = new ChessPosition(position.getRow() + 1, position.getColumn() -1);
            if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                ChessMove moveDiagLeft = new ChessMove(position, diagLeft, null);
                possibleMoves.add(moveDiagLeft);
            }
            ChessPosition diagRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                ChessMove moveDiagRight = new ChessMove(position, diagRight, null);
                possibleMoves.add(moveDiagRight);
            }
        }

        //BLACK PIECES HERE!!!
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (board.getPiece(moveOnePosition) == null) {
                ChessMove moveOneSpace = new ChessMove(position, moveOnePosition, null);
                possibleMoves.add(moveOneSpace);
            }
            if (position.getRow() == 7) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() - 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null && board.getPiece(moveOnePosition) == null) {
                    ChessMove moveTwoSpace = new ChessMove(position, moveTwoPosition, null);
                    possibleMoves.add(moveTwoSpace);
                }
            }
            if (position.getRow() - 1 >= 1) {
                ChessPosition diagLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveDiagLeft = new ChessMove(position, diagLeft, null);
                    possibleMoves.add(moveDiagLeft);
                }
            }
            if (position.getRow() + 1 <= 8) {
                ChessPosition diagRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove moveDiagRight = new ChessMove(position, diagRight, null);
                    possibleMoves.add(moveDiagRight);
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