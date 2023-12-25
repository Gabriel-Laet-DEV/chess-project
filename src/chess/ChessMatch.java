package chess;

import boardgame.Board;
import boardgame.exceptions.BoardException;
import boardgame.exceptions.PositionNotFoundException;
import chess.exceptions.ChessException;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() throws BoardException, PositionNotFoundException, ChessException {
        board = new Board(8, 8);
        inicialSetup();
    }

    public ChessPiece[][] getPieces() throws PositionNotFoundException {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns();j++){
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    private void placeNewPeace(char column, int row, ChessPiece piece) throws ChessException, PositionNotFoundException, BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void inicialSetup() throws BoardException, PositionNotFoundException, ChessException {
        placeNewPeace('b', 6, new Rook(board, Color.WHITE));
        placeNewPeace('e', 8, new King(board, Color.BLACK));
        placeNewPeace('e', 1, new King(board, Color.WHITE));
    }
}