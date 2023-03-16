package backend;

public class King extends Piece {
	public King(String color, int x, int y) {
		super(color, x, y);
	}
	
	public King(String color, int x, int y, int moves) {
		super(color, x, y, moves);
	}
	
	public boolean checkValidSquare(Piece[][] board, Square s, String castling) {
	    int dRow = Math.abs(s.getY() - position.getY());
	    int dCol = s.getX() - position.getX();
	
	 // ensure the move is only one square away in any direction, or a valid castling move
	    if ((dRow > 1 || dCol > 1) && !(dRow == 0 && Math.abs(dCol) == 2)) {
	        return false;
	    }
	    
	    // check if destination square is occupied by a friendly piece
	    if (board[s.getY()][s.getX()] != null && board[s.getY()][s.getX()].getColor() == color) {
	        return false;
	    }

	    // check if this is a valid castling move
	    if (dRow == 0 && Math.abs(dCol) == 2) {
	    	// Queenside castling
	        if (dCol == -2) {
	        	// Check if squares between king and rook are unoccupied
	            if (board[position.getY()][1] != null || board[position.getY()][2] != null || board[position.getY()][3] != null) {
	                return false;
	            }

	            // Check if king passes through check
	            if (isAttacked(board, new Square(position.getX() - 1, position.getY()), color, castling) || isAttacked(board, new Square(position.getX() - 2, position.getY()), color, castling)) {
	                return false;
	            }

	            // Check for castling rights
	            if (castling.contains("Q") && color.equals("white") || castling.contains("q") && color.equals("black")) {
	                return true;
	            } else {
	            	return false;
	            }
	        }
	        // Kingside castling
	        else {
	        	// Check if squares between king and rook are unoccupied
	            if (board[position.getY()][5] != null || board[position.getY()][6] != null) {
	                return false;
	            }
	            
	            // Check if king passes through check
	            if (isAttacked(board, new Square(position.getX() + 1, position.getY()), color, castling) || isAttacked(board, new Square(position.getX() + 2, position.getY()), color, castling)) {
	                return false;
	            }

	            // Check for castling rights
	            if (castling.contains("K") && color.equals("white") || castling.contains("k") && color.equals("black")) {
	                return true;
	            } else {
	            	return false;
	            }
	        }
	    }

	    // normal move is valid
	    return true;
	}
	
	public boolean isAttacked(Piece[][] board, Square square, String color, String castling) {
	    // check if any of the opponent's pieces can attack the specified square
	    for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            Piece piece = board[row][col];
	            if (piece != null && piece.getColor() != color) {
	            	if (piece instanceof King) {
	            		King king = (King) piece;
	            		if (king.checkValidSquare(board, square, castling)) {
		                	System.out.println(square);
		                	System.out.println(piece);
		                    return true;
		                }
	            	} else {
		                if (piece.checkValidSquare(board, square)) {
		                	System.out.println(square);
		                	System.out.println(piece);
		                    return true;
		                }
	            	}
	            }
	        }
	    }

	    return false;
	}
	
	public String toString() {
		return (color + " king at "+position.getName()+" "+moves);
	}

}

