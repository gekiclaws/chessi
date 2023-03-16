package backend;

public class Knight extends Piece {
	public Knight(String color, int x, int y) {
		super(color, x, y);
	}
	
	public Knight(String color, int x, int y, int moves) {
		super(color, x, y, moves);
	}
	
	public boolean checkValidSquare(Piece[][] board, Square s) {
        int rowDiff = Math.abs(s.getY() - y);
        int colDiff = Math.abs(s.getX() - x);

        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
        	
            // check if there is a piece in the destination square
            // if there is, it must be an opposing piece (for a capture move)
            // if there isn't, the destination square must be empty (for a non-capture move)
            if (board[s.getY()][s.getX()] == null) {
                return true;
            } else if (board[s.getY()][s.getX()].getColor() != color) {
                return true;
            }
        }

        return false;
    }
	
	public String toString() {
		return (color + " knight at "+position.getName()+" "+moves);
	}
}

