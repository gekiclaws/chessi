package backend;

import javax.swing.JOptionPane;

import stockfish.Stockfish;

public class ChessBoard {
	public static Stockfish CLIENT = new Stockfish();
	
	private Piece[][] board;
	private String boardFEN;
	
//	public static void main (String[] args) {
//		ChessBoard X = new ChessBoard();
//	}
    
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
	
	public boolean checkValidMove(Square s1, Square s2) {
		showBoard();
		
		// Verify that the move is allowed for the piece being moved
	    if (!board[s1.getY()][s1.getX()].checkValidSquare(board, s2)) {
	    	System.out.println("illegal move");
	    	return false;
	    }
	    
	    // Make a copy of the board to simulate the move and verify that the move does not put the moving player's king in check
	    Piece[][] copyBoard = copyBoard();
	    copyBoard[s2.getY()][s2.getX()] = copyBoard[s1.getY()][s1.getX()];
	    copyBoard[s1.getY()][s1.getX()] = null;
	    
	    if (isKingInCheck(copyBoard[s2.getY()][s2.getX()].getColor(), copyBoard)) {
	    	System.out.println("bad bug");
	    	return false;
	    }
	    
	    // The move is legal
	    return true;
	}

	private boolean isKingInCheck(String color, Piece[][] board) {
		// Find the king's position
	    Square kingPos = null;
	    for (int y = 0; y < 8; y++) {
	        for (int x = 0; x < 8; x++) {
	            Piece p = board[y][x];
	            if (p instanceof King && p.getColor().equals(color)) {
	                kingPos = new Square(x, y);
	                break;
	            }
	        }
	        if (kingPos != null) {
	            break;
	        }
	    }

	    // Check if the king is attacked by any enemy pieces
	    for (int y = 0; y < 8; y++) {
	        for (int x = 0; x < 8; x++) {
	            Piece p = board[y][x];
	            if (p != null && !p.getColor().equals(color) && p.checkValidSquare(board, kingPos)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	public boolean makeMove(Square s1, Square s2) {
	    if (checkValidMove(s1, s2)) {
	        Piece piece = board[s1.getY()][s1.getX()];
	        piece.setPosition(s2);
	        piece.setX(s2.getX());
	        piece.setY(s2.getY());

	        
	        if (piece instanceof Pawn) {
	            Pawn pawn = (Pawn) piece;
	            if (!pawn.isPromoted()) {
	            	// remove enemy pawn from board if en passant
	            	if (pawn.canBeCapturedEnPassant(board, pawn.getColor(), s1, s2)) {
		                board[s1.getY()][s2.getX()] = null;
		            }
		            
	            	// ask which piece to promote to if pawn promotion
		            if (pawn.checkForPromotion(s2)) {
		            	Piece type = new Queen(pawn.getColor(), s2.getX(), s2.getY());
		            	
		            	// receive user input
		            	String input = JOptionPane.showInputDialog(null, "Promote pawn to: (default is queen)");
		            	
		            	// trying to account for all potential spelling errors
		            	char promoteTo = Character.toLowerCase(input.charAt(0));
		            	
		            	try {
	            	      switch (promoteTo) {
		          			case 'r':
		          				type = new Rook(pawn.getColor(), s2.getX(), s2.getY());
		          				break;
		          			case 'k':
		          				type = new Knight(pawn.getColor(), s2.getX(), s2.getY());
		          				break;
		          			case 'b':
		          				type = new Bishop(pawn.getColor(), s2.getX(), s2.getY());
		          				break;
		          			default:
	            	      }
	            	    } catch (Exception e) {
	            	    }
		            	
		            	pawn.promote(type);
		            	pawn.getPromotedTo().setPosition(pawn.getPosition());
		            }
	            } else { // update position of piece pawn is promoted to also
	            	pawn.getPromotedTo().setPosition(pawn.getPosition());
	            }
	        }

	        board[s2.getY()][s2.getX()] = piece;
	        piece.addMove();
	        board[s1.getY()][s1.getX()] = null;

	        // move rook if castling
	        if (piece instanceof King && Math.abs(s2.getX() - s1.getX()) == 2) {
	            int rookX;
	            int rookDestX;
	            if (s2.getX() > s1.getX()) { // kingside castling
	                rookX = 7;
	                rookDestX = 5;
	            } else { // queenside castling
	                rookX = 0;
	                rookDestX = 3;
	            }
	            Piece rook = board[s2.getY()][rookX];
	            rook.setPosition(new Square(rookDestX, s2.getY()));
	            rook.setX(rookDestX);
	            rook.setY(s2.getY());
	            board[s2.getY()][rookDestX] = rook;
	            board[s2.getY()][rookX] = null;
	        }
	        
	        showBoard();

	        return true;
	    } else {
	        return false;
	    }
	}

	
//	public void checkValidBoard() {
//		
//	}
	
	public void boardStats() {
		
	}
	
	public Piece[][] copyBoard(){
		Piece[][] copyBoard = new Piece[8][8];
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (board[i][j] != null) {
	            	Piece piece = board[i][j];
	                if (piece instanceof Pawn) {
	                	Pawn pawn = (Pawn) piece;
	                	if (piece.getColor() == "black") {
	                		if (i == 1) {
	                			copyBoard[i][j] = new Pawn("black", pawn.getX(), pawn.getY(), pawn.getMoves(), pawn.getPromotedTo());
							} else {
								copyBoard[i][j] = new Pawn("black", pawn.getX(), piece.getY(), pawn.getMoves(), pawn.getPromotedTo());
							}
	                	} else {
	                		if (i == 6) {
								copyBoard[i][j] = new Pawn("white", pawn.getX(), pawn.getY(), pawn.getMoves(), pawn.getPromotedTo());
							} else {
								copyBoard[i][j] = new Pawn("white", pawn.getX(), pawn.getY(), pawn.getMoves(), pawn.getPromotedTo());
							}
	                	}
	                } else if (piece instanceof Knight) {
	                    copyBoard[i][j] = new Knight(piece.getColor(), piece.getX(), piece.getY(), piece.getMoves());
	                } else if (piece instanceof Bishop) {
	                    copyBoard[i][j] = new Bishop(piece.getColor(), piece.getX(), piece.getY(), piece.getMoves());
	                } else if (piece instanceof Rook) {
	                    copyBoard[i][j] = new Rook(piece.getColor(), piece.getX(), piece.getY(), piece.getMoves());
	                } else if (piece instanceof Queen) {
	                    copyBoard[i][j] = new Queen(piece.getColor(), piece.getX(), piece.getY(), piece.getMoves());
	                } else if (piece instanceof King) {
	                    copyBoard[i][j] = new King(piece.getColor(), piece.getX(), piece.getY(), piece.getMoves());
	                }
	            }
	        }
	    }
	    
	    return copyBoard;
	}
	
	public void showBoard() {
		for (int i = 0; i < board.length; i++) {
        	for (int k = 0; k<board[0].length; k++) {
        		System.out.println(board[i][k]+" "+i+k);
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
