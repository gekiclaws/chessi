package model;

public class Knight extends Piece {
	public Knight(String color, int x, int y) {
		super(color, x, y);
	}
	
	public boolean checkMoveValid(Move move) {
		return true;
	}
	
	public String toString() {
		return (color + " knight at "+position.getName());
	}
}
