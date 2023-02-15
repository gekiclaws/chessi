package model;

public class Queen extends Piece {
	public Queen(String color, int x, int y) {
		super(color, x, y);
	}
	
	public boolean checkMoveValid(Move move) {
		return true;
	}
	
	public String toString() {
		return (color + " queen at "+position.getName());
	}
}
