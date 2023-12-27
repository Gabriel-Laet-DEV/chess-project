package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import boardgame.exceptions.PositionNotFoundException;
import chess.exceptions.ChessException;

public abstract class ChessPiece extends Piece {

    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    protected boolean isThereOpponentPiece(Position position) throws PositionNotFoundException {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }

    public ChessPosition getChessPosition() throws ChessException {
        return ChessPosition.fromPosition(position);
    }
}
