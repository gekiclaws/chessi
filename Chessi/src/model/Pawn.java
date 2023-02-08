package model;

public class Pawn extends Piece {
	private boolean promoted;
	private Piece promotedTo; // maybe string better
	private boolean moved; // for initial 2 move leap forward
	private String moveDirection;
	
	public boolean checkMoveValid(Move move) {
		return true;
	}

}
