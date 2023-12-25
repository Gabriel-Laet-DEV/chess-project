package boardgame.exceptions;

public class PositionNotFoundException extends Exception{

    public String getMessage(){
        return "Position not on the board";
    }
}
