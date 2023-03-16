package backend;

public class Bishop extends Piece {
	public Bishop(String color, int x, int y) {
		super(color, x, y);
	}
	
	public Bishop(String color, int x, int y, int moves) {
		super(color, x, y, moves);
	}
	
	public boolean checkValidSquare(Piece[][] board, Square s) {
	    int dRow = s.getY() - position.getY();
	    int dCol = s.getX() - position.getX();

	    // Ensure the move is in a diagonal direction
	    if (Math.abs(dRow) != Math.abs(dCol)) {
	        return false;
	    }

	    // Check if there is a clear path to the destination
	    int rowDirection = dRow > 0 ? 1 : -1;
	    int colDirection = dCol > 0 ? 1 : -1;
	    int row = position.getY() + rowDirection;
	    int col = position.getX() + colDirection;
	    while (row != s.getY() && col != s.getX()) {
	        if (board[row][col] != null) {
	            return false;
	        }
	        row += rowDirection;
	        col += colDirection;
	    }

	    // Check if destination square is not occupied by a friendly piece
	    if (board[s.getY()][s.getX()] != null && board[s.getY()][s.getX()].getColor() == color) {
	        return false;
	    }

	    return true;
	}

	
	public String toString() {
		return (color + " bishop at "+position.getName()+" "+moves);
	}

}

