package model;

public class Piece {
	protected String color;
	protected Square position;
	protected int x;
	protected int y;
	
	public Piece(String color, int x, int y) {
		this.setColor(color);
		this.position = new Square(x,y);
	}

	public Square[] validMoves() {
		return null;
	}
	
	public boolean checkMoveValid(Move move) {
		return false;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}