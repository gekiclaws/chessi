package backend;

public class Move {
	private Square startSquare;
	private Square endSquare;
	
	public Move(Square startSquare, Square endSquare) {
		this.startSquare = startSquare;
		this.endSquare = endSquare;
	}
	public Square getStartSquare() {
		return startSquare;
	}
	public void setStartSquare(Square startSquare) {
		this.startSquare = startSquare;
	}
	public Square getEndSquare() {
		return endSquare;
	}
	public void setEndSquare(Square endSquare) {
		this.endSquare = endSquare;
	}
	
	public String toString() {
		return startSquare.getName()+" to "+endSquare.getName();
	}
}

