package backend;

public class Pawn extends Piece {
	private boolean promoted;
	private Piece promotedTo;
	
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
        if (color.equals("white") && toSquare.getY() == 0 ||
                color.equals("black") && toSquare.getY() == 7) {
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
		    if (position.getY() != (color.equals("white") ? 6 : 1)) { // check if pawn on starting rank
		        if (rowDiff != 1) {
		            return false; // pawns can only move 1 square forward if they aren't on the starting rank
		        }
		    } else {
		        if (rowDiff > 2 || rowDiff < 1) {
		            return false; // pawns can only move either 1 or 2 squares forward
		        } else if (rowDiff == 2) {
		            // check if there is a piece in between the start and end squares
		            int midY = (position.getY() + s.getY()) / 2;
		            if (board[midY][s.getX()] != null) {
		                return false;
		            }
		        }
		    }
		    
		    // check if destination is on a diagonal and there is a piece to capture
		    Piece p = board[s.getY()][s.getX()];
		    		
		    if (rowDiff == 1) {
		        if (Math.abs(dx) == 1) {
		            if (p == null) {
		                // check if this is an en passant capture
		                if (board[position.getY()][s.getX()] != null && board[position.getY()][s.getX()] instanceof Pawn && board[position.getY()][s.getX()].getColor() != color) {
		                    // only allow if en passant available AND last move was made by the pawn in question
		                	if (canBeCapturedEnPassant(board, color, position, s) && board[position.getY()][s.getX()].isLastMoved()) {
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
		    
		    return true;
		}
	}
	
	public boolean canBeCapturedEnPassant(Piece[][] board, String color, Square capturer, Square captured) {
		int enPassantRow;
		
		// Check if the color of the capturing pawn is white
		if (color.equals("white")) {
			enPassantRow = 2; // set the en passant row to the 3rd row
		} else {
			enPassantRow = 5; // set the en passant row to the 6th row
		}

		// Calculate the horizontal distance between the captured and capturing pawns
		int dx = captured.getX() - capturer.getX();

		// Check if the captured pawn is on the correct row for an en passant capture
		if (Math.abs(dx) == 1 && captured.getY() == enPassantRow && board[capturer.getY()][captured.getX()] instanceof Pawn
			&& board[capturer.getY()][captured.getX()].getColor() != color
			&& board[capturer.getY()][captured.getX()].getMoves() == 1) {
			return true; // en passant capture is valid, return true
		}
		return false; // en passant capture is not valid, return false.
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
