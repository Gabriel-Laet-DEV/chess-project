package application;

import boardgame.exceptions.BoardException;
import boardgame.exceptions.PositionNotFoundException;
import chess.ChessMatch;
import chess.exceptions.ChessException;

public class Program {
    public static void main(String[] args) {

        ChessMatch chessMatch = null;
        try {
            chessMatch = new ChessMatch();
            UI.printBoard(chessMatch.getPieces());
        } catch (BoardException | PositionNotFoundException | ChessException e) {
            System.out.println(e.getMessage());
        }
    }
}
