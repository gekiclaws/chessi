package backend;

import java.util.LinkedList;

public class Game {
	private String activeColor;
	private String result;
	private String timeline;
	private LinkedList<Move> movesPlayed;
	private ChessBoard theBoard;
	
	public Game() {
		this.activeColor = "white";
		this.result = "";
		this.timeline = "";
		this.movesPlayed = new LinkedList<Move>();
		this.theBoard = new ChessBoard();
	}
	
	public void loadNewPosition(String FEN) {
		theBoard.updateBoard(FEN);
	}
	
	public void addMove(Move move) {
		movesPlayed.add(move);
	}
	
	public void updateTimeline() {
		
	}

	public String getActiveColor() {
		return activeColor;
	}

	public void setActiveColor(String activeColor) {
		this.activeColor = activeColor;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public LinkedList<Move> getMovesPlayed() {
		return movesPlayed;
	}

	public void setMovesPlayed(LinkedList<Move> movesPlayed) {
		this.movesPlayed = movesPlayed;
	}

	public ChessBoard getTheBoard() {
		return theBoard;
	}

	public void setTheBoard(ChessBoard theBoard) {
		this.theBoard = theBoard;
	}

	
	
	
	
	
	
	
}
