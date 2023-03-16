package backend;

import java.util.LinkedList;

public class Piece {
	protected String color;
	protected Square position;
	protected int x;
	protected int y;
	protected int moves;
	protected boolean lastMoved;
	
	// Constructors
	public Piece(String color, int x, int y) {
		this.setColor(color);
		this.position = new Square(x,y);
		this.x = x;
		this.y = y;
		this.moves = 0;
		this.lastMoved = false;
	}
	
	public Piece(String color, int x, int y, int moves) {
		this(color, x, y); // calls above constructor to set attributes
		this.moves = moves; // then updates the moves attribute with the input value
	}
	
	// Updates position of piece
		public void setPosition(Square position) {
			this.position = position;
			this.x = position.getX();
			this.y = position.getY();
		}

	// Checks if a square is valid given a Piece[][] board
	public boolean checkValidSquare(Piece[][] board, Square s) {
		return true;
	}
	
	public boolean isLastMoved() {
		return lastMoved;
	}
	
	// Get a LinkedList of all valid squares a piece can move to
	public LinkedList<Square> getValidSquares(Piece[][] board) {
		LinkedList<Square> validSquares = new LinkedList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (checkValidSquare(board, new Square(i, j))) {
                    validSquares.add(new Square(i, j));
                }
            }
        }

        return validSquares;
	}
	
		
	public Square getPosition() {
		return position;
	}

	public void setLastMoved(boolean lastMoved) {
		this.lastMoved = lastMoved;
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

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}
	
	public void addMove() {
		this.moves += 1;
	}

}

