package chess;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
            ChessGame.TeamColor color = gameBoard.getPiece(startPosition).getTeamColor();
            ChessPiece movingPiece = gameBoard.getPiece(startPosition);
            Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
            for (ChessMove move : possibleMoves) {
                ChessBoard testBoard = new ChessBoard(gameBoard);
                testBoard.addPiece(move.getStartPosition(), null);
                testBoard.addPiece(move.getEndPosition(), movingPiece);
                ChessGame fakeGame = new ChessGame();
                fakeGame.setBoard(testBoard);
                if (fakeGame.isInCheck(color) || fakeGame.isInCheckmate(color)) {
                    possibleMoves.remove(move);
                }
            }
            return possibleMoves;
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
        if (validMovesList.contains(move) && getTeamTurn() == gameBoard.getPiece(moveStartPosition).getTeamColor()) {
            if (move.getPromotionPiece() != null) {
                ChessPiece movePiece = gameBoard.getPiece(moveStartPosition);
                gameBoard.addPiece(moveStartPosition, null);
                gameBoard.addPiece(move.getEndPosition(), movePiece);
            }
            else {
                ChessPiece movePiece = new ChessPiece(gameBoard.getPiece(moveStartPosition).getTeamColor(), move.getPromotionPiece());
                gameBoard.addPiece(moveStartPosition, null);
                gameBoard.addPiece(move.getEndPosition(), movePiece);
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
                if (currPiece != null && currPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleMoves = currPiece.pieceMoves(gameBoard, currPosition);
                    for (ChessMove moveOption : possibleMoves) {
                        if (gameBoard.getPiece(moveOption.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && gameBoard.getPiece(moveOption.getEndPosition()).getTeamColor() == teamColor) {
                            return true;
                        }
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
