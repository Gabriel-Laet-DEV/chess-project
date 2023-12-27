package application;

import boardgame.exceptions.BoardException;
import boardgame.exceptions.PositionNotFoundException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.exceptions.ChessException;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = null;
        try {
            chessMatch = new ChessMatch();
        }catch (ChessException | BoardException | PositionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        while(true){
            try{
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);
                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            }catch (PositionNotFoundException | BoardException | ChessException e) {
                System.out.println(e.getMessage());
                System.out.println("Aperta qualquer teclara para continuar");
                sc.nextLine();
            }
        }
    }
}
