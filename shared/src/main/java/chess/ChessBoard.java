package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
//        resetBoard();
    }

    public ChessBoard(ChessBoard other) {
        ChessPiece[][] squares = new ChessPiece[8][8];
        for (int i=1; i <= 8; i++){
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                addPiece(currentPosition, other.getPiece(currentPosition));
            }
        }
    }

    @Override
    public ChessBoard clone() {
        return new ChessBoard(this);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[8 - position.getRow()][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[8 - position.getRow()][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];

        //WHITE PIECES
        ChessPosition WR1_position = new ChessPosition(1, 1);
        ChessPiece WR1_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(WR1_position, WR1_piece);

        ChessPosition WR2_position = new ChessPosition(1, 8);
        ChessPiece WR2_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(WR2_position, WR2_piece);

        ChessPosition WN1_position = new ChessPosition(1, 2);
        ChessPiece WN1_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(WN1_position, WN1_piece);

        ChessPosition WN2_position = new ChessPosition(1, 7);
        ChessPiece WN2_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(WN2_position, WN2_piece);

        ChessPosition WB1_position = new ChessPosition(1, 3);
        ChessPiece WB1_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(WB1_position, WB1_piece);

        ChessPosition WB2_position = new ChessPosition(1, 6);
        ChessPiece WB2_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(WB2_position, WB2_piece);

        ChessPosition WQ_position = new ChessPosition(1, 4);
        ChessPiece WQ_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(WQ_position, WQ_piece);

        ChessPosition WK_position = new ChessPosition(1, 5);
        ChessPiece WK_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(WK_position, WK_piece);

        ChessPosition WP1_position = new ChessPosition(2,1);
        ChessPiece WP1_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP1_position, WP1_piece);

        ChessPosition WP2_position = new ChessPosition(2,2);
        ChessPiece WP2_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP2_position, WP2_piece);

        ChessPosition WP3_position = new ChessPosition(2,3);
        ChessPiece WP3_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP3_position, WP3_piece);

        ChessPosition WP4_position = new ChessPosition(2,4);
        ChessPiece WP4_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP4_position, WP4_piece);

        ChessPosition WP5_position = new ChessPosition(2,5);
        ChessPiece WP5_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP5_position, WP5_piece);

        ChessPosition WP6_position = new ChessPosition(2,6);
        ChessPiece WP6_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP6_position, WP6_piece);

        ChessPosition WP7_position = new ChessPosition(2,7);
        ChessPiece WP7_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP7_position, WP7_piece);

        ChessPosition WP8_position = new ChessPosition(2,8);
        ChessPiece WP8_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(WP8_position, WP8_piece);

        //BLACK PIECES
        ChessPosition BR1_position = new ChessPosition(8, 1);
        ChessPiece BR1_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(BR1_position, BR1_piece);

        ChessPosition BR2_position = new ChessPosition(8, 8);
        ChessPiece BR2_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(BR2_position, BR2_piece);

        ChessPosition BN1_position = new ChessPosition(8, 2);
        ChessPiece BN1_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(BN1_position, BN1_piece);

        ChessPosition BN2_position = new ChessPosition(8, 7);
        ChessPiece BN2_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(BN2_position, BN2_piece);

        ChessPosition BB1_position = new ChessPosition(8, 3);
        ChessPiece BB1_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(BB1_position, BB1_piece);

        ChessPosition BB2_position = new ChessPosition(8, 6);
        ChessPiece BB2_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(BB2_position, BB2_piece);

        ChessPosition BQ_position = new ChessPosition(8, 4);
        ChessPiece BQ_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(BQ_position, BQ_piece);

        ChessPosition BK_position = new ChessPosition(8, 5);
        ChessPiece BK_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(BK_position, BK_piece);

        ChessPosition BP1_position = new ChessPosition(7,1);
        ChessPiece BP1_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP1_position, BP1_piece);

        ChessPosition BP2_position = new ChessPosition(7,2);
        ChessPiece BP2_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP2_position, BP2_piece);

        ChessPosition BP3_position = new ChessPosition(7,3);
        ChessPiece BP3_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP3_position, BP3_piece);

        ChessPosition BP4_position = new ChessPosition(7,4);
        ChessPiece BP4_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP4_position, BP4_piece);

        ChessPosition BP5_position = new ChessPosition(7,5);
        ChessPiece BP5_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP5_position, BP5_piece);

        ChessPosition BP6_position = new ChessPosition(7,6);
        ChessPiece BP6_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP6_position, BP6_piece);

        ChessPosition BP7_position = new ChessPosition(7,7);
        ChessPiece BP7_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP7_position, BP7_piece);

        ChessPosition BP8_position = new ChessPosition(7,8);
        ChessPiece BP8_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BP8_position, BP8_piece);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
