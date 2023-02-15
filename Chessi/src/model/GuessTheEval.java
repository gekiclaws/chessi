package model;

import java.util.LinkedList;

public class GuessTheEval {
	private int evalGuess;
	private String moveGuess;
	private ChessBoard theBoard;
	private LinkedList<String> database;
	
	public static void main (String[] args) {
		GuessTheEval X = new GuessTheEval();
	}
	
	public GuessTheEval() {
		evalGuess = 0;
		moveGuess = "";
		theBoard = new ChessBoard();
		database = new LinkedList<String>();
		
		database.add("8/5k2/3p4/1p1Pp2p/pP2Pp1P/P4P1K/8/8 b");
		
		theBoard.updateBoard(database.get(0));
		
		theBoard.showBoard();
		
	}

	public void loadNewPosition(String FEN) {
		theBoard.updateBoard(FEN);
	}
	
	public void makeMove(Move move){
		
	}
	
	public void undoMove(){
		
	}
	
	public void checkAnswers(){
		
	}
	
}
