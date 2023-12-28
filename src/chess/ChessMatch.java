package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import boardgame.exceptions.BoardException;
import boardgame.exceptions.PositionNotFoundException;
import chess.exceptions.ChessException;
import chess.pieces.King;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {


    private int turn;
    private Color currentPlayer;

    private Board board;
    private boolean check;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
    private boolean checkMate;

    public boolean getCheckMate(){
        return checkMate;
    }

    private void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    public ChessMatch() throws BoardException, PositionNotFoundException, ChessException {
        board = new Board(8, 8);
        setTurn(1);
        setCurrentPlayer(Color.WHITE);
        inicialSetup();
    }

    public boolean getCheck(){
        return check;
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

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) throws PositionNotFoundException, BoardException, ChessException {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if(testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }
        check = (testCheck(opponent(currentPlayer))) ? true : false;
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else
            nextTurn();

        return (ChessPiece) capturedPiece;
    }

    private void validateTargetPosition(Position source, Position target) throws PositionNotFoundException {
        if(!board.piece(source).possibleMove(target)){
            throw new PositionNotFoundException("The chosen piece can't move to target position");
        }
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) throws PositionNotFoundException {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }


    private void undoMove(Position source, Position target, Piece capturedPiece) throws PositionNotFoundException, BoardException {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if(capturedPiece != null){
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }



    private Piece makeMove(Position source, Position target) throws PositionNotFoundException, BoardException {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if(capturedPiece != null){
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        return capturedPiece;
    }



    private void validateSourcePosition(Position position) throws PositionNotFoundException {
        if(!board.thereIsAPiece(position)){
            throw new PositionNotFoundException("There is no piece on source position");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new PositionNotFoundException("The chosen piece is not yours");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new PositionNotFoundException("There is no possible moves for the chosen piece");
        }
    }

    private void nextTurn() {
        setTurn(turn + 1);
        setCurrentPlayer((currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE);
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color){
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list){
            if(p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalArgumentException("There is no " + color + " king on the board");
    }

    private boolean testCheckMate(Color color) throws ChessException, PositionNotFoundException, BoardException {
        if(!testCheck(color)){
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list){
            boolean[][] mat = p.possibleMoves();
            for(int i = 0; i < board.getRows();i++){
                for(int j = 0; j < board.getColumns(); j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean testCheck(Color color) throws ChessException, PositionNotFoundException {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for(Piece p : opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) throws ChessException, PositionNotFoundException, BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public int getTurn() {
        return turn;
    }

    private void setTurn(int turn) {
        this.turn = turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }


    private void inicialSetup() throws BoardException, PositionNotFoundException, ChessException {
        placeNewPiece('h', 7, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.WHITE));
        placeNewPiece('b', 8, new King(board, Color.BLACK));

    }

}
