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
        ChessGame.TeamColor opposingColor;
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            opposingColor = ChessGame.TeamColor.BLACK;
        } else { opposingColor = ChessGame.TeamColor.WHITE;}

        if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return PawnMovesCalculator.pieceMoves(board, position);}
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return BishopMovesCalculator.actuallyMove(board, position, opposingColor);}
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return RookMovesCalculator.actuallyMoving(board, position, opposingColor);}
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return QueenMovesCalculator.pieceMoves(board, position, opposingColor);}
        else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
            return KingMovesCalculator.actuallyMoveKing(board, position, opposingColor);}
        else if(currentPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return KnightMovesCalculator.actuallyMove(board, position, opposingColor);}
        return new ArrayList<>();
    }
}

class PawnMovesCalculator {
    public static ArrayList<ChessMove> checkPromo(ChessPosition startPosition, ChessPosition endPosition, ChessPiece currentPiece) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int endRow;
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            endRow = 8;
        } else {
            endRow = 1;
        }
        if (endPosition.getRow() == endRow) {
            ArrayList<ChessPiece.PieceType> pieceTypes = new ArrayList<>();
            pieceTypes.add(ChessPiece.PieceType.KNIGHT);
            pieceTypes.add(ChessPiece.PieceType.QUEEN);
            pieceTypes.add(ChessPiece.PieceType.BISHOP);
            pieceTypes.add(ChessPiece.PieceType.ROOK);
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
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //WHITE PIECES HERE
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (position.getRow() + 1 <= 8) {
                if (board.getPiece(moveOnePosition) == null) {
                    possibleMoves = checkPromo(position, moveOnePosition, board.getPiece(position));
                }
            }
            if (position.getRow() == 2) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() + 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null && board.getPiece(moveOnePosition) == null) {
                    possibleMoves.addAll(checkPromo(position, moveTwoPosition, board.getPiece(position)));
                }
            }
            if (position.getRow() + 1 <= 8 && position.getColumn() - 1 >= 1) {
                ChessPosition diagLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    possibleMoves.addAll(checkPromo(position, diagLeft, board.getPiece(position)));
                }
            }
            if (position.getRow() + 1 <= 8 && position.getColumn() + 1 <= 8) {
                ChessPosition diagRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    possibleMoves.addAll(checkPromo(position, diagRight, board.getPiece(position)));
                }
            }
        }
        //BLACK PIECES HERE!!!
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition moveOnePosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (moveOnePosition.getRow() >= 1) {
                if (board.getPiece(moveOnePosition) == null) {
                    possibleMoves = checkPromo(position, moveOnePosition, board.getPiece(position));
                }
            }
            if (position.getRow() == 7) {
                ChessPosition moveTwoPosition = new ChessPosition(position.getRow() - 2, position.getColumn());
                if (board.getPiece(moveTwoPosition) == null && board.getPiece(moveOnePosition) == null) {
                    possibleMoves.addAll(checkPromo(position, moveTwoPosition, board.getPiece(position)));
                }
            }
            if (position.getRow() - 1 >= 1 && position.getColumn() - 1 >= 1) {
                ChessPosition diagLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (board.getPiece(diagLeft) != null && board.getPiece(diagLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    possibleMoves.addAll(checkPromo(position, diagLeft, board.getPiece(position)));
                }
            }
            if (position.getRow() - 1 >= 1 && position.getColumn() + 1 <= 8) {
                ChessPosition diagRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (board.getPiece(diagRight) != null && board.getPiece(diagRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    possibleMoves.addAll(checkPromo(position, diagRight, board.getPiece(position)));
                }
            }
        }
        return possibleMoves;
    }
}

class BishopMovesCalculator {
    public static Collection<ChessMove> actuallyMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor opposingTeamColor) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //Moving to the forward left
        int currentRow = position.getRow();
        int currentCol = position.getColumn();
        ChessPosition futurePosition = new ChessPosition(currentRow + 1, currentCol - 1);
        while (futurePosition.getRow() <= 8 && futurePosition.getColumn() >= 1) {
            if  (board.getPiece(futurePosition) == null) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                currentRow += 1;
                currentCol -= 1;
                futurePosition = new ChessPosition(currentRow + 1, currentCol - 1);
            }
            else if (board.getPiece(futurePosition).getTeamColor() == opposingTeamColor) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                break;
            }
            else { break; }
        }
        //Moving to the forward right
        currentRow = position.getRow();
        currentCol = position.getColumn();
        futurePosition = new ChessPosition(currentRow + 1, currentCol + 1);
        while (futurePosition.getRow() <= 8 && futurePosition.getColumn() <= 8) {
            if  (board.getPiece(futurePosition) == null) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                currentRow += 1;
                currentCol += 1;
                futurePosition = new ChessPosition(currentRow + 1, currentCol + 1);
            }
            else if (board.getPiece(futurePosition).getTeamColor() == opposingTeamColor) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                break;
            }
            else { break; }
        }
        //Moving to the backward right
        currentRow = position.getRow();
        currentCol = position.getColumn();
        futurePosition = new ChessPosition(currentRow - 1, currentCol + 1);
        while (futurePosition.getRow() >= 1 && futurePosition.getColumn() <= 8) {
            if  (board.getPiece(futurePosition) == null) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                currentRow -= 1;
                currentCol += 1;
                futurePosition = new ChessPosition(currentRow - 1, currentCol + 1);
            }
            else if (board.getPiece(futurePosition).getTeamColor() == opposingTeamColor) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                break;
            }
            else { break; }
        }
        //Moving to the backward left
        currentRow = position.getRow();
        currentCol = position.getColumn();
        futurePosition = new ChessPosition(currentRow - 1, currentCol - 1);
        while (futurePosition.getRow() >= 1 && futurePosition.getColumn() >= 1) {
            if  (board.getPiece(futurePosition) == null) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                currentRow -= 1;
                currentCol -= 1;
                futurePosition = new ChessPosition(currentRow - 1, currentCol - 1);
            }
            else if (board.getPiece(futurePosition).getTeamColor() == opposingTeamColor) {
                ChessMove moveForeLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForeLeft);
                break;
            }
            else { break; }
        }
        return possibleMoves;
    }
}

class RookMovesCalculator {
    public static Collection<ChessMove> actuallyMoving(ChessBoard board, ChessPosition position, ChessGame.TeamColor opposingTeam) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //Moving forward
        int futureRow = position.getRow() + 1;
        int futureCol = position.getColumn();
        ChessPosition futurePosition = new ChessPosition(futureRow, futureCol);
        while (futurePosition.getRow() <= 8) {
            if (board.getPiece(futurePosition) == null) {
                ChessMove moveForward = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForward);
                futureRow += 1;
                futurePosition = new ChessPosition(futureRow, futureCol);
            } else if (board.getPiece(futurePosition).getTeamColor() == opposingTeam) {
                ChessMove moveForward = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveForward);
                break;
            } else {
                break;
            }
        }
        //Moving backward
        futureRow = position.getRow() - 1;
        futureCol = position.getColumn();
        futurePosition = new ChessPosition(futureRow, futureCol);
        while (futurePosition.getRow() >= 1) {
            if (board.getPiece(futurePosition) == null) {
                ChessMove moveBackward = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveBackward);
                futureRow -= 1;
                futurePosition = new ChessPosition(futureRow, futureCol);
            } else if (board.getPiece(futurePosition).getTeamColor() == opposingTeam) {
                ChessMove moveBackward = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveBackward);
                break;
            } else {
                break;
            }
        }
        //Moving left
        futureRow = position.getRow();
        futureCol = position.getColumn() - 1;
        futurePosition = new ChessPosition(futureRow, futureCol);
        while (futurePosition.getColumn() >= 1) {
            if (board.getPiece(futurePosition) == null) {
                ChessMove moveLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveLeft);
                futureCol -= 1;
                futurePosition = new ChessPosition(futureRow, futureCol);
            } else if (board.getPiece(futurePosition).getTeamColor() == opposingTeam) {
                ChessMove moveLeft = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveLeft);
                break;
            } else {
                break;
            }
        }
        //Moving right
        futureRow = position.getRow();
        futureCol = position.getColumn() + 1;
        futurePosition = new ChessPosition(futureRow, futureCol);
        while (futurePosition.getColumn() <= 8) {
            if (board.getPiece(futurePosition) == null) {
                ChessMove moveRight = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveRight);
                futureCol += 1;
                futurePosition = new ChessPosition(futureRow, futureCol);
            } else if (board.getPiece(futurePosition).getTeamColor() == opposingTeam) {
                ChessMove moveRight = new ChessMove(position, futurePosition, null);
                possibleMoves.add(moveRight);
                break;
            } else {
                break;
            }
        }
        return possibleMoves;
    }
}

class QueenMovesCalculator {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor opposingTeamColor) {
        Collection<ChessMove> possibleMoves;
        possibleMoves = RookMovesCalculator.actuallyMoving(board, position,opposingTeamColor);
        possibleMoves.addAll(BishopMovesCalculator.actuallyMove(board,position,opposingTeamColor));
        return possibleMoves;
    }
}

class KingMovesCalculator {
    public static Collection<ChessMove> actuallyMoveKing(ChessBoard board, ChessPosition position, ChessGame.TeamColor opposingTeam) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //Forward
        if (position.getRow() <= 7) {
            ChessPosition moveForwardPos = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (board.getPiece(moveForwardPos) == null || board.getPiece(moveForwardPos).getTeamColor() == opposingTeam) {
                ChessMove moveForward = new ChessMove(position, moveForwardPos, null);
                possibleMoves.add(moveForward);
            }
        }
        //Backward
        if (position.getRow() >= 2) {
            ChessPosition moveBackwardPos = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (board.getPiece(moveBackwardPos) == null || board.getPiece(moveBackwardPos).getTeamColor() == opposingTeam) {
                ChessMove moveBackward = new ChessMove(position, moveBackwardPos, null);
                possibleMoves.add(moveBackward);
            }
        }
        //Left
        if (position.getColumn() >= 2) {
            ChessPosition moveLeftPos = new ChessPosition(position.getRow(), position.getColumn() - 1);
            if (board.getPiece(moveLeftPos) == null || board.getPiece(moveLeftPos).getTeamColor() == opposingTeam) {
                ChessMove moveLeft = new ChessMove(position, moveLeftPos, null);
                possibleMoves.add(moveLeft);
            }
        }
        //Right
        if (position.getColumn() <= 7) {
            ChessPosition moveRightPos = new ChessPosition(position.getRow(), position.getColumn() + 1);
            if (board.getPiece(moveRightPos) == null || board.getPiece(moveRightPos).getTeamColor() == opposingTeam) {
                ChessMove moveRight = new ChessMove(position, moveRightPos, null);
                possibleMoves.add(moveRight);
            }
        }
        //Forward left
        if (position.getColumn() >= 2 && position.getRow() <= 7) {
            ChessPosition moveForLeftPos = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            if (board.getPiece(moveForLeftPos) == null || board.getPiece(moveForLeftPos).getTeamColor() == opposingTeam) {
                ChessMove moveForLeft = new ChessMove(position, moveForLeftPos, null);
                possibleMoves.add(moveForLeft);
            }
        }
        //Forward right
        if (position.getRow() <= 7 && position.getColumn() <= 7) {
            ChessPosition moveForRightPos = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (board.getPiece(moveForRightPos) == null || board.getPiece(moveForRightPos).getTeamColor() == opposingTeam) {
                ChessMove moveForRight = new ChessMove(position, moveForRightPos, null);
                possibleMoves.add(moveForRight);
            }
        }
        //Backward left
        if (position.getRow() >= 2 && position.getColumn() >= 2) {
            ChessPosition moveBackLeftPos = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (board.getPiece(moveBackLeftPos) == null || board.getPiece(moveBackLeftPos).getTeamColor() == opposingTeam) {
                ChessMove moveBackLeft = new ChessMove(position, moveBackLeftPos, null);
                possibleMoves.add(moveBackLeft);
            }
        }
        //Backward right
        if (position.getRow() >= 2 && position.getColumn() <= 7) {
            ChessPosition moveBackRightPos = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            if (board.getPiece(moveBackRightPos) == null || board.getPiece(moveBackRightPos).getTeamColor() == opposingTeam) {
                ChessMove moveBackRight = new ChessMove(position, moveBackRightPos, null);
                possibleMoves.add(moveBackRight);
            }
        }
        return possibleMoves;
    }
}

class KnightMovesCalculator {
    public static Collection<ChessMove> actuallyMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor opposingColor) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //Forward2 Left1
        if (position.getRow() + 2 <= 8 && position.getColumn() - 1 >= 1) {
            ChessPosition for2left1Pos = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
            if (board.getPiece(for2left1Pos) == null || board.getPiece(for2left1Pos).getTeamColor() == opposingColor) {
                ChessMove for2left1 = new ChessMove(position, for2left1Pos, null);
                possibleMoves.add(for2left1);
            }
        }
        //Forward2 Right1
        if (position.getRow() + 2 <= 8 && position.getColumn() + 1 <= 8) {
            ChessPosition for2right1Pos = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
            if (board.getPiece(for2right1Pos) == null || board.getPiece(for2right1Pos).getTeamColor() == opposingColor) {
                ChessMove for2right1 = new ChessMove(position, for2right1Pos, null);
                possibleMoves.add(for2right1);
            }
        }
        //Backward2 left1
        if (position.getRow() - 2 >= 1 && position.getColumn() - 1 >= 1) {
            ChessPosition back2left1Pos = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
            if (board.getPiece(back2left1Pos) == null || board.getPiece(back2left1Pos).getTeamColor() == opposingColor) {
                ChessMove back2left1 = new ChessMove(position, back2left1Pos, null);
                possibleMoves.add(back2left1);
            }
        }
        //Backward2 right1
        if (position.getRow() - 2 >= 1 && position.getColumn() + 1 <= 8) {
            ChessPosition back2right1Pos = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
            if (board.getPiece(back2right1Pos) == null || board.getPiece(back2right1Pos).getTeamColor() == opposingColor) {
                ChessMove back2right1 = new ChessMove(position, back2right1Pos, null);
                possibleMoves.add(back2right1);
            }
        }
        //Forward1 Left2
        if (position.getRow() + 1 <= 8 && position.getColumn() - 2 >= 1) {
            ChessPosition for1left2Pos = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
            if (board.getPiece(for1left2Pos) == null || board.getPiece(for1left2Pos).getTeamColor() == opposingColor) {
                ChessMove for1left2 = new ChessMove(position, for1left2Pos, null);
                possibleMoves.add(for1left2);
            }
        }
        //Forward1 Right2
        if (position.getRow() + 1 <= 8 && position.getColumn() + 2 <= 8) {
            ChessPosition for1right2Pos = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
            if (board.getPiece(for1right2Pos) == null || board.getPiece(for1right2Pos).getTeamColor() == opposingColor) {
                ChessMove for1right2 = new ChessMove(position, for1right2Pos, null);
                possibleMoves.add(for1right2);
            }
        }
        //Backward1 left2
        if (position.getRow() - 1 >= 1 && position.getColumn() - 2 >= 1) {
            ChessPosition back1left2Pos = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
            if (board.getPiece(back1left2Pos) == null || board.getPiece(back1left2Pos).getTeamColor() == opposingColor) {
                ChessMove back1left2 = new ChessMove(position, back1left2Pos, null);
                possibleMoves.add(back1left2);
            }
        }
        //Backward1 right2
        if (position.getRow() - 1 >= 1 && position.getColumn() + 2 <= 8) {
            ChessPosition back1right2Pos = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
            if (board.getPiece(back1right2Pos) == null || board.getPiece(back1right2Pos).getTeamColor() == opposingColor) {
                ChessMove back1right2 = new ChessMove(position, back1right2Pos, null);
                possibleMoves.add(back1right2);
            }
        }
        return possibleMoves;
    }
}