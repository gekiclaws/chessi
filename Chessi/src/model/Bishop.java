package model;

public class Bishop extends Piece {
	public Bishop(String color, int x, int y) {
		super(color, x, y);
	}
	
	public boolean checkMoveValid(Move move) {
		return true;
	}
	
	public String toString() {
		return (color + " bishop at "+position.getName());
	}

}
