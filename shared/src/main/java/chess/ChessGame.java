package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor turnTeam = TeamColor.WHITE;

    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnTeam = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && turnTeam == chessGame.turnTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, turnTeam);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", turnTeam=" + turnTeam +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) != null) {
            Collection<ChessMove> returnMoves = new ArrayList<>();
            TeamColor color = gameBoard.getPiece(startPosition).getTeamColor();
            ChessPiece movingPiece = gameBoard.getPiece(startPosition);
            Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
            for (ChessMove move : possibleMoves) {
                ChessBoard testBoard = new ChessBoard(gameBoard);
                testBoard.addPiece(move.getStartPosition(), null);
                testBoard.addPiece(move.getEndPosition(), movingPiece);
                ChessGame fakeGame = new ChessGame();
                fakeGame.setBoard(testBoard);
                if (!fakeGame.isInCheck(color) && !fakeGame.isInCheckmate(color)) {
                    returnMoves.add(move);
                }
            }
            return returnMoves;
        }
        else {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition moveStartPosition = move.getStartPosition();
        Collection<ChessMove> validMovesList = validMoves(moveStartPosition);
        if (validMovesList != null && validMovesList.contains(move) && getTeamTurn() == gameBoard.getPiece(moveStartPosition).getTeamColor()) {
            ChessPiece movePiece;
            if (move.getPromotionPiece() != null) {
                movePiece = new ChessPiece(gameBoard.getPiece(moveStartPosition).getTeamColor(), move.getPromotionPiece());
            }
            else {
                ChessPiece oldPiece = gameBoard.getPiece(moveStartPosition);
                movePiece = new ChessPiece(oldPiece.getTeamColor(), oldPiece.getPieceType());
            }
            gameBoard.addPiece(moveStartPosition, null);
            gameBoard.addPiece(move.getEndPosition(), movePiece);
            if (getTeamTurn() == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            }
            else {
                setTeamTurn(TeamColor.BLACK);
            }
        }
        else {
            throw new InvalidMoveException();
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j=1; j<=8; j++){
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = gameBoard.getPiece(currPosition);
                Collection<ChessMove> possibleMoves = new ArrayList<>();
                if (currPiece != null && currPiece.getTeamColor() != teamColor) {
                    possibleMoves = currPiece.pieceMoves(gameBoard, currPosition);}
                for (ChessMove moveOption : possibleMoves) {
                    ChessPosition endPosition = moveOption.getEndPosition();
                    ChessPiece endPiece = gameBoard.getPiece(endPosition);
                    if (endPiece != null && (endPiece.getPieceType() == ChessPiece.PieceType.KING && endPiece.getTeamColor() == teamColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {return false;}
        boolean checkMate = true;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = gameBoard.getPiece(currPosition);
                Collection<ChessMove> possibleMoves = new ArrayList<>();
                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    possibleMoves = currPiece.pieceMoves(gameBoard, currPosition);}
                for (ChessMove moveOption : possibleMoves) {
                    ChessBoard testBoard = new ChessBoard(gameBoard);
                    testBoard.addPiece(moveOption.getStartPosition(), null);
                    testBoard.addPiece(moveOption.getEndPosition(), currPiece);
                    ChessGame fakeGame = new ChessGame();
                    fakeGame.setBoard(testBoard);
                    if (!fakeGame.isInCheck(teamColor)) {
                        checkMate = false;
                    }
                }
            }
        }
        return checkMate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheckmate(teamColor)) { return false;}
        boolean stalemate = true;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = gameBoard.getPiece(currPosition);
                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    if (!validMoves(currPosition).isEmpty()) {
                        stalemate = false;
                    }
                }
            }
        }
        return stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
