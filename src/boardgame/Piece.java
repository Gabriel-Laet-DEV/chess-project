package boardgame;

import boardgame.exceptions.PositionNotFoundException;

public abstract class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
    }

    protected Board getBoard(){
        return board;
    }

    public abstract boolean[][] possibleMoves() throws PositionNotFoundException;

    public boolean possibleMove(Position position) throws PositionNotFoundException {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() throws PositionNotFoundException {
        boolean[][] mat = possibleMoves();
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat.length; j++){
                if(mat[i][j]){
                    return true;
                }
            }
        }
        return false;
    }
}
