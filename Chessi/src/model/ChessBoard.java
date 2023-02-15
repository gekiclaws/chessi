package model;

import stockfish.Stockfish;

public class ChessBoard {
	public static Stockfish CLIENT = new Stockfish();
	
	private Piece[][] board;
	private String boardFEN;
	
	public static void main (String[] args) {
		ChessBoard X = new ChessBoard();
	}
    
    public ChessBoard() {
        board = new Piece[8][8];
        boardFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w";
        
        updateBoard(boardFEN);
        
        
    }
    
    public ChessBoard(String FEN) {
        board = new Piece[8][8];
        boardFEN = FEN;
        
        updateBoard(boardFEN);
        
        for (int i = 0; i < board.length; i++) {
        	for (int k = 0; k<board[0].length; k++) {
        		System.out.println(board[i][k]);
        	}
        	
        }
        
    }
    
    public void resetBoard() {
		updateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");
	}
	
	public void updateBoard(String FEN) {
		char[] boardItems = FEN.toCharArray();
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < boardItems.length; i++) {
			switch (boardItems[i]) {
				case 'r':
					board[y][x] = new Rook("black", x, y);
					break;
				case 'n':
					board[y][x] = new Knight("black", x, y);
					break;
				case 'b':
					board[y][x] = new Bishop("black", x, y);
					break;
				case 'q':
					board[y][x] = new Queen("black", x, y);
					break;
				case 'k':
					board[y][x] = new King("black", x, y);
					break;
				case 'p':
					board[y][x] = new Pawn("black", x, y);
					break;
				case 'R':
					board[y][x] = new Rook("white", x, y);
					break;
				case 'N':
					board[y][x] = new Knight("white", x, y);
					break;
				case 'B':
					board[y][x] = new Bishop("white", x, y);
					break;
				case 'Q':
					board[y][x] = new Queen("white", x, y);
					break;
				case 'K':
					board[y][x] = new King("white", x, y);
					break;
				case 'P':
					board[y][x] = new Pawn("white", x, y);
					break;
				case '/':
					x = -1;
					y += 1;
					break;
				case ' ':
					i += 1;
				default:
					if (Character.isDigit(boardItems[i])) {
						for (int j = 0; j<Character.getNumericValue(boardItems[i]); j++) {
							board[y][x] = null;
							x += 1;
						}
						x -= 1;
					}
			}
			x += 1;
			
		}
	}
	
	
//	public void checkValidBoard() {
//		
//	}
	
	public void boardStats() {
		
	}
	
	public void showBoard() {
		for (int i = 0; i < board.length; i++) {
        	for (int k = 0; k<board[0].length; k++) {
        		System.out.println(board[i][k]);
        	}
        }
	}
	
	public Piece[][] getBoard() {
		return board;
	}

	public String getBoardFEN() {
		return boardFEN;
	}

	public void setBoardFEN(String boardFEN) {
		this.boardFEN = boardFEN;
	}

	
	
}
