package backend;

public class Queen extends Piece {
	public Queen(String color, int x, int y) {
		super(color, x, y);
	}
	
	public Queen(String color, int x, int y, int moves) {
		super(color, x, y, moves);
	}
	
	public boolean checkValidSquare(Piece[][] board, Square s) {
	    int dRow = s.getY() - position.getY();
	    int dCol = s.getX() - position.getX();

	    // Ensure the move is in a diagonal, horizontal, or vertical direction
	    if (dRow != 0 && dCol != 0 && Math.abs(dRow) != Math.abs(dCol)) {
	        return false;
	    }

	    // Check if there is a clear path to the destination
	    if (dRow == 0) {
	        // Moving horizontally
	        int startCol = Math.min(position.getX(), s.getX()) + 1;
	        int endCol = Math.max(position.getX(), s.getX());
	        for (int col = startCol; col < endCol; col++) {
	            if (board[position.getY()][col] != null) {
	                return false;
	            }
	        }
	    } else if (dCol == 0) {
	        // Moving vertically
	        int startRow = Math.min(position.getY(), s.getY()) + 1;
	        int endRow = Math.max(position.getY(), s.getY());
	        for (int row = startRow; row < endRow; row++) {
	            if (board[row][position.getX()] != null) {
	                return false;
	            }
	        }
	    } else {
	        // Moving diagonally
	    	int rowDirection = Integer.compare(dRow, 0);
	        int colDirection = Integer.compare(dCol, 0);
	        int curRow = position.getY() + rowDirection;
	        int curCol = position.getX() + colDirection;
	        while (curRow != s.getY() || curCol != s.getX()) {
	            if (board[curRow][curCol] != null) {
	                return false;
	            }
	            curRow += rowDirection;
	            curCol += colDirection;
	        }
	    }
	    
	    

	    // Check if destination square is not occupied by a friendly piece
	    if (board[s.getY()][s.getX()] != null && board[s.getY()][s.getX()].getColor() == color) {
	        return false;
	    }

	    return true;
	}

	public String toString() {
		return (color + " queen at "+position.getName()+" "+moves);
	}
}

