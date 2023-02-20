package backend;

import javax.swing.JOptionPane;


public class ChessBoard {
	
	private Piece[][] board;
	private String boardFEN;
    
    public ChessBoard() {
        board = new Piece[8][8];
        boardFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        
        updateBoard(boardFEN);
    }
    
    public ChessBoard(String FEN) {
        board = new Piece[8][8];
        boardFEN = FEN;
        
        updateBoard(boardFEN);
    }
    
    public void resetBoard() {
		updateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	}
	
    // generates board from FEN input
	public void updateBoard(String FEN) {
		boardFEN = FEN;
		String[] arFEN = FEN.split(" ");
		
		char[] boardItems = arFEN[0].toCharArray();
		int rank = 0;
		int file = 0;

		for (int i = 0; i < boardItems.length; i++) {
		    switch (boardItems[i]) {
		        case 'r':
		            board[rank][file] = new Rook("black", file, rank);
		            break;
		        case 'n':
		            board[rank][file] = new Knight("black", file, rank);
		            break;
		        case 'b':
		            board[rank][file] = new Bishop("black", file, rank);
		            break;
		        case 'q':
		            board[rank][file] = new Queen("black", file, rank);
		            break;
		        case 'k':
		            board[rank][file] = new King("black", file, rank);
		            break;
		        case 'p':
		            board[rank][file] = new Pawn("black", file, rank);
		            break;
		        case 'R':
		            board[rank][file] = new Rook("white", file, rank);
		            break;
		        case 'N':
		            board[rank][file] = new Knight("white", file, rank);
		            break;
		        case 'B':
		            board[rank][file] = new Bishop("white", file, rank);
		            break;
		        case 'Q':
		            board[rank][file] = new Queen("white", file, rank);
		            break;
		        case 'K':
		            board[rank][file] = new King("white", file, rank);
		            break;
		        case 'P':
		            board[rank][file] = new Pawn("white", file, rank);
		            break;
		        case '/':
		            file = -1;
		            rank += 1;
		            break;
		        case ' ':
		            i += 1;
		        default:
		            if (Character.isDigit(boardItems[i])) {
		                for (int j = 0; j < Character.getNumericValue(boardItems[i]); j++) {
		                    board[rank][file] = null;
		                    file += 1;
		                }
		                file -= 1;
		            }
		    }
		    file += 1;

		}

	}
	
	// generates first section of FEN from piece locations on board
	public String toFEN(Piece[][] board) {
		StringBuilder fen = new StringBuilder();

		for (int rank = 0; rank < 8; rank++) {
		    int emptySquares = 0;
		    for (int file = 0; file < 8; file++) {
		        Piece piece = board[rank][file];
		        if (piece == null) {
		            emptySquares++;
		        } else {
		            char type = 'P';

		            if (emptySquares > 0) {
		                fen.append(emptySquares);
		                emptySquares = 0;
		            }

		            if (piece.getClass().getSimpleName().equals("Knight")) {
		                type = 'N';
		            } else if (piece.getClass().getSimpleName().equals("Pawn")) {
		                Pawn pawn = (Pawn) piece;
		                if (pawn.isPromoted()) {
		                    if (pawn.getPromotedTo().getClass().getSimpleName().equals("Knight")) {
		                        type = 'N';
		                    } else {
		                        type = pawn.getPromotedTo().getClass().getSimpleName().charAt(0);
		                    }
		                }

		            } else {
		                type = piece.getClass().getSimpleName().charAt(0);
		            }

		            fen.append(piece.getColor().equals("white") ? type : Character.toLowerCase(type));
		        }
		    }
		    if (emptySquares > 0) {
		        fen.append(emptySquares);
		    }
		    if (rank < 7) {
		        fen.append('/');
		    }
		}

		return fen.toString();
	}
	
	public boolean checkValidMove(Square s1, Square s2) {
		Piece piece = board[s1.getY()][s1.getX()];
		
		// Verify that the move is allowed for the piece being moved
		if (piece instanceof King) {
			King king = (King) piece;
			if (!king.checkValidSquare(board, s2, boardFEN.split(" ")[2])) {
		    	System.out.println("illegal move");
		    	return false;
		    }
		} else {
			if (!piece.checkValidSquare(board, s2)) {
				System.out.println("illegal move");
		    	return false;
		    }
		}
		
		// Make a copy of the board to simulate the move and verify that the move does not put the moving player's king in check
	    Piece[][] copyBoard = copyBoard();
	    copyBoard[s2.getY()][s2.getX()] = copyBoard[s1.getY()][s1.getX()];
	    copyBoard[s2.getY()][s2.getX()].setPosition(s2);
	    copyBoard[s1.getY()][s1.getX()] = null;
	    
	    if (isKingInCheck(copyBoard[s2.getY()][s2.getX()].getColor(), copyBoard)) {	 
	    	JOptionPane.showMessageDialog(null, "Can't leave your king in check!", "Invalid move", JOptionPane.WARNING_MESSAGE);        	
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
	            if (p instanceof King) {
	    			King king = (King) p;
	    			if (!king.getColor().equals(color) && king.checkValidSquare(board, kingPos, "")) {
		            	return true;
		            }
	    		} else {
	    			if (p != null && !p.getColor().equals(color) && p.checkValidSquare(board, kingPos)) {
		            	return true;
		            }
	    		}
	        }
	    }
	    return false;
	}
	
	public boolean makeMove(Square s1, Square s2) {
		if (checkValidMove(s1, s2)) {
	        Piece piece = board[s1.getY()][s1.getX()];
	        
	        if (piece instanceof Rook) {
	        	// removing castling rights on the side that the rook is moving from
	        	if (piece.getColor().equals("white")) {
	        		if (piece.getPosition().getName().equals("a1")) {
	        			updateFEN(boardFEN.split(" ")[2].replace("Q", ""), 2);
	        		} else if (piece.getPosition().getName().equals("h1")){
	        			updateFEN(boardFEN.split(" ")[2].replace("K", ""), 2);
	        		}
	            	
	            } else {
	            	if (piece.getPosition().getName().equals("a8")) {
	        			updateFEN(boardFEN.split(" ")[2].replace("q", ""), 2);
	        		} else if (piece.getPosition().getName().equals("h8")){
	        			updateFEN(boardFEN.split(" ")[2].replace("k", ""), 2);
	        		}
	            }
	        }
	        
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
		            	String input = JOptionPane.showInputDialog(null, "Promote to: (default is queen)", "Pawn promotion", JOptionPane.QUESTION_MESSAGE);
		            	
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

	        
	        if (piece instanceof King) {
	        	// remove castling rights
	        	if (piece.getColor().equals("white")) {
	            	updateFEN(boardFEN.split(" ")[2].replace("K", "").replace("Q", ""), 2);
	            } else {
	            	updateFEN(boardFEN.split(" ")[2].replace("k", "").replace("q", ""), 2);
	            }
	        	
	        	// move rook if castling
	        	if (Math.abs(s2.getX() - s1.getX()) == 2) {
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
	        }
	        
	        // if black moves, increase fullmove number in FEN by 1 and set white to move
	        if (piece.getColor().equals("black")) { 
	        	updateFEN(Integer.toString(Integer.parseInt(boardFEN.split(" ")[5])+1), 5);
	        	updateFEN("w", 1);
	        } else {
	        	updateFEN("b", 1);
	        }
	        
	        updateFEN(toFEN(board), 0); // update board FEN after every move
	        
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public void makeMove(String move) {
		Square s1 = new Square(move.substring(0, 2));
		Square s2 = new Square(move.substring(2, 4));
		
		makeMove(s1, s2);
	}

	public boolean isValidChessPosition() {
	    // check that there is exactly one white and one black king
	    int numWhiteKings = 0;
	    int numBlackKings = 0;
	    for (int i = 0; i < board.length; i++) {
	        for (int j = 0; j < board[i].length; j++) {
	            if (board[i][j] != null) {
	                if (board[i][j].getClass().getSimpleName().equals("King")) {
	                    if (board[i][j].getColor().equals("white")) {
	                        numWhiteKings++;
	                    } else if (board[i][j].getColor().equals("black")) {
	                        numBlackKings++;
	                    }
	                }
	            }
	        }
	    }
	    if (numWhiteKings != 1 || numBlackKings != 1) {
	        return false;
	    }

	    // check that there are no pawns on the first or last rank
	    for (int i = 0; i < board[0].length; i++) {
	        if (board[0][i] != null && board[0][i].getClass().getSimpleName().equals("Pawn")) {
	            return false;
	        }
	        if (board[7][i] != null && board[7][i].getClass().getSimpleName().equals("Pawn")) {
	            return false;
	        }
	    }

	    return true;
	}
	
	public Piece[][] copyBoard(){
		Piece[][] copyBoard = new Piece[8][8];
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (board[i][j] != null) {
	            	Piece piece = board[i][j];
	                if (piece instanceof Pawn) {
	                	Pawn pawn = (Pawn) piece;
	                	if (piece.getColor().equals("black")) {
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
	
	// takes a string and replaces the selected section of the FEN with the string
	public void updateFEN(String update, int section) {
		String[] FEN = boardFEN.split(" ");
		FEN[section] = update;
		boardFEN = String.join(" ", FEN);
	}
	
	public String getSquareColor(Square s) {
		Piece piece = board[s.getY()][s.getX()];
		return piece.getColor();
	}
	
	public String parseMove(Move move) {
		String moveName = "";
		Square s1 = move.getStartSquare();
		Square s2 = move.getEndSquare();
		
		Piece piece1 = board[s1.getY()][s1.getX()];
		Piece piece2 = board[s2.getY()][s2.getX()];
		
		if (piece1.getClass().getSimpleName().equals("Knight")) {
			moveName = "N";
		} else if (!piece1.getClass().getSimpleName().equals("Pawn")){
			moveName = Character.toString(piece1.getClass().getSimpleName().charAt(0));
		}
		
		if (piece2 != null) {
			moveName += "x";
		}
		moveName += s2.getName();
		
		// Make a copy of the board to simulate the move and see if the move puts the opponent's king in check
	    ChessBoard copy = new ChessBoard(boardFEN);
	    copy.makeMove(s1, s2);
	    if (copy.isKingInCheck(getColor() == 'w' ? "black" : "white", copy.getBoard())) {
	    	moveName += "+";
	    }
		
		return moveName;
	}
	
	public String parseMove(String move) {
		Square s1 = new Square(move.substring(0, 2));
		Square s2 = new Square(move.substring(2, 4));
		
		return parseMove(new Move(s1, s2));
	}
	
	public String[] parseContinuation(String[] line) {
		String[] parsedLine = new String[line.length];
		String FEN = boardFEN;
		
		for (int i=0;i<line.length;i++) {
			parsedLine[i] = parseMove(line[i]);
			makeMove(line[i]);
		}
		
		updateBoard(FEN);
		
		return parsedLine;
	}
	
	
	public Piece[][] getBoard(){
		return board;
	}
	
	public char getColor() {
		return boardFEN.split(" ")[1].charAt(0);
	}
	
	public int getMoveNo() {
		return Integer.parseInt(boardFEN.split(" ")[5]);
	}

	public String getBoardFEN() {
		return boardFEN;
	}

	public void setBoardFEN(String boardFEN) {
		this.boardFEN = boardFEN;
	}

	
	
}
