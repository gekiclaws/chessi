package backend;

public class Pawn extends Piece {
	private boolean promoted;
	private Piece promotedTo; // maybe string better
	
	public Pawn(String color, int x, int y) {
		super(color, x, y);
		this.promoted = false;
	}
	
	public Pawn(String color, int x, int y, int moves, Piece promotedTo) {
		super(color, x, y, moves);
		this.promotedTo = promotedTo;
		this.promoted = (promotedTo == null) ? false : true;
	}
	
	public boolean checkForPromotion(Square toSquare) {
        if (this.getColor().equals("white") && toSquare.getY() == 0 ||
                this.getColor().equals("black") && toSquare.getY() == 7) {
            return true;
        }
        return false;
    }

    public void promote(Piece piece) {
        this.promotedTo = piece;
        this.promoted = true;
    }

	public boolean checkValidSquare(Piece[][] board, Square s) {
		if (promoted) {
			return promotedTo.checkValidSquare(board, s);
		} else {
			int dx = s.getX() - position.getX();
		    int dy = s.getY() - position.getY();
		    
		    System.out.println(dx);
		    System.out.println(dy);
		    
		    if (color.equals("white")) {
		        if (dy > 0) {
		            return false; // white pawns can only move upwards
		        }
		    } else {
		        if (dy < 0) {
		            return false; // black pawns can only move downwards
		        }
		    }
		    
		    int rowDiff = Math.abs(dy);
		    if (moves > 0) {
		        if (rowDiff != 1) {
		            return false; // pawns can move 1 square if they have moved
		        }
		    } else {
		        if (rowDiff > 2 || rowDiff < 1) {
		            return false; // pawns can move 1 or 2 squares if they have not moved yet
		        } else if (rowDiff == 2) {
		            // check if there is a piece in between the start and end squares
		            int midY = (position.getY() + s.getY()) / 2;
		            if (board[midY][s.getX()] != null) {
		                return false;
		            }
		        }
		    }
		    
		    // check if destination is on a diagonal and there is a piece to capture
		    if (Math.abs(rowDiff) == 1) {
		        if (Math.abs(dx) == 1) {
		            Piece p = board[s.getY()][s.getX()];
		            if (p == null) {
		                // check if this is an en passant capture
		                if (board[position.getY()][s.getX()] != null && board[position.getY()][s.getX()] instanceof Pawn && board[position.getY()][s.getX()].getColor() != color) {
		                    if (canBeCapturedEnPassant(board, color, position, s)) {
		                        return true;
		                    }
		                }
		                return false; // cannot capture an empty square
		            } else if (p.getColor().equals(color)) {
		                return false; // cannot capture own piece
		            }
		        } else if (Math.abs(dx) != 0) {
		            return false; // pawns can only move diagonally if capturing
		        }
		    } else {
		        if (s.getX() != position.getX()) {
		            return false; // pawns can only move straight up/down
		        }
		    }
		    
		     // check if there is a piece in the destination square
			 // if there is, it must be an opposing piece (for a capture move)
			 // if there isn't, the destination square must be empty (for a non-capture move)
			 if (board[s.getY()][s.getX()] == null) {
			     if (rowDiff == 2) {
			         if (moves > 0) {
			             return false; // pawns cannot move 2 squares if they have already moved
			         } else {
			             // check if there is a piece in between the start and end squares
			             int midY = (position.getY() + s.getY()) / 2;
			             if (board[midY][s.getX()] != null) {
			                 return false;
			             }
			         }
			     }
			 } else {
			     if (Math.abs(dx) != 1) {
			         return false;
			     } else if (board[s.getY()][s.getX()].getColor() == color) {
			         return false;
			     }
			 }
		    
		    return true;
		}
	}
	
	public boolean canBeCapturedEnPassant(Piece[][] board, String color, Square capturer, Square captured) {
		int enPassantRow;
		if (color.equals("white")) {
		    enPassantRow = 2;
		} else {
		    enPassantRow = 5;
		}
		
	    int dx = captured.getX() - capturer.getX();
	    
	    System.out.println(Math.abs(dx));
	    System.out.println(captured.getY());
	    System.out.println(board[capturer.getY()][captured.getX()]);
	    
	    if (Math.abs(dx) == 1 && captured.getY() == enPassantRow && board[capturer.getY()][captured.getX()] instanceof Pawn
	            && board[capturer.getY()][captured.getX()].getColor() != color
	            && board[capturer.getY()][captured.getX()].getMoves() == 1) {
	        return true;
	    }
	    return false;
	}

	
	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	public Piece getPromotedTo() {
		return promotedTo;
	}

	public void setPromotedTo(Piece promotedTo) {
		this.promotedTo = promotedTo;
	}

	public String toString() {
		return (color + " pawn at "+position.getName()+" promoted to "+promotedTo+" "+moves);
	}

}
