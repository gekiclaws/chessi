package model;

public class King extends Piece {
	public King(String color, int x, int y) {
		super(color, x, y);
	}
	
	public boolean checkMoveValid(Move move) {
		return true;
	}
	
	public String toString() {
		return (color + " king at "+position.getName());
	}

}

