package backend;

public class Rook extends Piece {
	
	public Rook(String color, int x, int y) {
		super(color, x, y);
	}
	
	public Rook(String color, int x, int y, int moves) {
		super(color, x, y, moves);
	}

	public boolean checkValidSquare(Piece[][] board, Square s) {
	    int dRow = s.getY() - position.getY();
	    int dCol = s.getX() - position.getX();

	    // ensure the move is in a horizontal or vertical direction
	    if (dRow != 0 && dCol != 0) {
	        return false;
	    }

	    // check if there is a clear path to the destination
	    if (dRow == 0) {
	        int startCol = Math.min(position.getX(), s.getX()) + 1;
	        int endCol = Math.max(position.getX(), s.getX());
	        for (int col = startCol; col < endCol; col++) {
	            if (board[position.getY()][col] != null) {
	                return false;
	            }
	        }
	    } else {
	        int startRow = Math.min(position.getY(), s.getY()) + 1;
	        int endRow = Math.max(position.getY(), s.getY());
	        for (int row = startRow; row < endRow; row++) {
	            if (board[row][position.getX()] != null) {
	                return false;
	            }
	        }
	    }

	    // check if destination square is not occupied by a friendly piece
	    if (board[s.getY()][s.getX()] != null && board[s.getY()][s.getX()].getColor() == color) {
	        return false;
	    }

	    return true;
	}

	
	public String toString() {
		return (color + " rook at "+position.getName()+" "+moves);
	}
}

