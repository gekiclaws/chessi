package model;

public class Rook extends Piece {
	
	public Rook(String color, int x, int y) {
		super(color, x, y);
	}

	public boolean checkMoveValid(Move move) {
		return true;
	}
	
	public String toString() {
		return (color + " rook at "+position.getName());
	}
}
