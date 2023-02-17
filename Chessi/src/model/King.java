package model;

import java.util.LinkedList;

public class King extends Piece {
	public King(String color, int x, int y) {
		super(color, x, y);
	}
	
	public King(String color, int x, int y, int moves) {
		super(color, x, y, moves);
	}
	
	public boolean checkValidSquare(Piece[][] board, Square s) {
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
	        	System.out.println("bug1");
	            // Check if king and chosen rook have not previously moved
	            if (moves != 0 || board[position.getY()][0].getMoves() != 0) {
	                return false;
	            }

	            System.out.println("bug2");
	            // Check if squares between king and rook are unoccupied
	            if (board[position.getY()][1] != null || board[position.getY()][2] != null || board[position.getY()][3] != null) {
	                return false;
	            }

	            System.out.println("bug3");
	            // Check if king passes through check
	            if (isAttacked(board, new Square(position.getX() - 1, position.getY()), color) || isAttacked(board, new Square(position.getX() - 2, position.getY()), color)) {
	                return false;
	            }
	            System.out.println("bug4");

	            // Return true if all conditions are met
	            return true;
	        }
	        // Kingside castling
	        else {
	        	
	            // Check if king and chosen rook have not previously moved
	            if (moves != 0 || board[position.getY()][7].getMoves() != 0) {
	                return false;
	            }

	            // Check if squares between king and rook are unoccupied
	            if (board[position.getY()][5] != null || board[position.getY()][6] != null) {
	                return false;
	            }

	            // Check if king passes through check
	            if (isAttacked(board, new Square(position.getX() + 1, position.getY()), color) || isAttacked(board, new Square(position.getX() + 2, position.getY()), color)) {
	                return false;
	            }

	            // Return true if all conditions are met
	            return true;
	        }
	    }

	    // normal move is valid
	    return true;
	}
	
	public boolean canCastle(Piece[][] board, Rook rook) {
	    // Check if king or rook has moved
	    if (moves < 0 || rook.getMoves() < 0) {
	        return false;
	    }

	    // Check if there are any pieces between the king and rook
	    int x1 = position.getX();
	    int x2 = rook.getPosition().getX();
	    int y = position.getY();
	    int startX = Math.min(x1, x2);
	    int endX = Math.max(x1, x2);
	    for (int x = startX + 1; x < endX; x++) {
	        if (board[y][x] != null) {
	            return false;
	        }
	    }

	    // Check if the squares the king passes through or ends up in are attacked
	    String color = getColor();
	    if (isAttacked(board, new Square(x1 + 1, y), color) || isAttacked(board, new Square(x1 + 2, y), color)) {
	        return false;
	    }

	    return true;
	}

	
	public boolean isAttacked(Piece[][] board, Square square, String color) {
	    // check if any of the opponent's pieces can attack the specified square
	    for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            Piece piece = board[row][col];
	            if (piece != null && piece.getColor() != color) {
	                if (piece.checkValidSquare(board, square)) {
	                	System.out.println(square);
	                	System.out.println(piece);
	                    return true;
	                }
	            }
	        }
	    }

	    return false;
	}
	
	public String toString() {
		return (color + " king at "+position.getName()+x+y);
	}

}

