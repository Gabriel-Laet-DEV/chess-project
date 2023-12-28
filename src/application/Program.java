package application;

import boardgame.exceptions.BoardException;
import boardgame.exceptions.PositionNotFoundException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.exceptions.ChessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = null;
        List<ChessPiece> captured = new ArrayList<>();

        try {
            chessMatch = new ChessMatch();
        }catch (ChessException | BoardException | PositionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        while(!chessMatch.getCheckMate()){
            try{
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);
                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
                if(capturedPiece != null){
                    captured.add(capturedPiece);
                }
            }catch (PositionNotFoundException | BoardException | ChessException e) {
                System.out.println(e.getMessage());
                System.out.println("Press any key to continue");
                sc.nextLine();
            }
        }
        UI.clearScreen();
        try {
            UI.printMatch(chessMatch, captured);
        } catch (PositionNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
