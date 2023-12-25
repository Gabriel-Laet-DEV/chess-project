package application;

import boardgame.Position;
import boardgame.exceptions.BoardException;
import boardgame.exceptions.PositionNotFoundException;
import chess.ChessMatch;

public class Program {
    public static void main(String[] args) {

        ChessMatch chessMatch = null;
        try {
            chessMatch = new ChessMatch();
            UI.printBoard(chessMatch.getPieces());
        } catch (BoardException | PositionNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
