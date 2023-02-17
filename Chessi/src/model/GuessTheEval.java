package model;

import java.util.LinkedList;

public class GuessTheEval {
	private int evalGuess;
	private Move moveGuess;
	private ChessBoard theBoard;
	private LinkedList<String> database;
	
//	public static void main (String[] args) {
//		GuessTheEval X = new GuessTheEval();
//	}
	
	public GuessTheEval() {
		theBoard = new ChessBoard();
		database = new LinkedList<String>();
		
		database.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");
		database.add("8/5k2/3p4/1p1Pp2p/pP2Pp1P/P4P1K/8/8 b");
		database.add("r4rk1/2p1bpp1/p1Np4/1p1P3q/1P2QP2/n1N4p/P1P1PB1K/R4R2 w");
		database.add("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w");
		database.add("8/8/K7/8/8/8/PPPPPPPP/8 w");
		
	}

	public void loadNewPosition(int i) {
		theBoard.updateBoard(database.get(i));
	}
	
	public void makeMove(Move move){
		
	}
	
	public void undoMove(){
		
	}
	
	public void checkAnswers(){
		
	}

	public int getEvalGuess() {
		return evalGuess;
	}

	public void setEvalGuess(int evalGuess) {
		this.evalGuess = evalGuess;
	}

	public Move getMoveGuess() {
		return moveGuess;
	}

	public void setMoveGuess(Move moveGuess) {
		this.moveGuess = moveGuess;
	}

	public ChessBoard getTheBoard() {
		return theBoard;
	}

	public void setTheBoard(ChessBoard theBoard) {
		this.theBoard = theBoard;
	}

	public LinkedList<String> getDatabase() {
		return database;
	}

	public void setDatabase(LinkedList<String> database) {
		this.database = database;
	}
	
	
	
}
