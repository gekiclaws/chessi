package backend;

import javax.swing.JOptionPane;

public class ChessBoard {
	
	private Piece[][] board;
	private String boardFEN;
    
	public ChessBoard(String FEN) {
        board = new Piece[8][8];
        boardFEN = FEN;
        
        updateBoard(boardFEN);
    }
	
	public ChessBoard() {
    	this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
	
	public ChessBoard(Piece[][] board, String FEN) {
		this.board = board;
		this.boardFEN = FEN;
	}
    
    public void resetBoard() {
		updateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	}
	
    // generates board from FEN input
	public void updateBoard(String FEN) {
		boardFEN = FEN;
		String[] arFEN = FEN.split(" ");
		
		char[] boardItems = arFEN[0].toCharArray(); // get the board configuration from the FEN array
		int rank = 0; // initialize the rank to 0
		int file = 0; // initialize the file to 0

		// loop through each character in the board configuration
		for (int i = 0; i < boardItems.length; i++) {
		    switch (boardItems[i]) {
		        // if the character represents a black rook, knight, bishop, queen, king, or pawn, 
		        // create a new instance of the corresponding piece and add it to the board array at the current rank and file
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
		        // if the character represents a white rook, knight, bishop, queen, king, or pawn, 
		        // create a new instance of the corresponding piece and add it to the board array at the current rank and file
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
		        // if the character represents a slash (/), set the file to -1 and increment the rank
		        case '/':
		            file = -1;
		            rank += 1;
		            break;
		        // if the character represents a space, increment i by 1 (skipping the next character in the loop)
		        case ' ':
		            i += 1;
		        // if the character is not a recognized piece or a special character, assume it represents an empty space and add null to the board array at the current rank and file
		        default:
		            if (Character.isDigit(boardItems[i])) {
		                // if the character is a digit, add null to the board array for the specified number of spaces and decrement file accordingly
		                for (int j = 0; j < Character.getNumericValue(boardItems[i]); j++) {
		                    board[rank][file] = null;
		                    file += 1;
		                }
		                file -= 1;
		            }
		    }
		    file += 1; // increment the file after each iteration of the loop
		}

	}
	
	// This method takes a 2D array representing the board and returns the first part of the FEN string
	// which represents the current state of the board.
	public String toFEN(Piece[][] board) {
		StringBuilder fen = new StringBuilder();

		// Loop through each rank of the board (from 1 to 8).
		for (int rank = 0; rank < 8; rank++) {
		    // Initialize a counter for the number of empty squares in the current rank.
		    int emptySquares = 0;
		    // Loop through each file of the board (from a to h).
		    for (int file = 0; file < 8; file++) {
		        // Get the piece object at the current square of the board.
		        Piece piece = board[rank][file];
		        // Check if the current square is empty.
		        if (piece == null) {
		            // If it is, increment the empty square counter.
		            emptySquares++;
		        } else {
		            // If it is not empty, append the count of empty squares to the FEN string if there are any.
		            if (emptySquares > 0) {
		                fen.append(emptySquares);
		                emptySquares = 0;
		            }
		            // Determine the type of the piece (P, N, B, R, Q, or K) and append it to the FEN string.
		            char type = 'P';
		            if (piece.getClass().getSimpleName().equals("Knight")) {
		                type = 'N';
		            } else if (piece.getClass().getSimpleName().equals("Pawn")) {
		                // If the piece is a pawn, check if it has been promoted to a different piece.
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
		    // Append the count of empty squares to the FEN string if there are any.
		    if (emptySquares > 0) {
		        fen.append(emptySquares);
		    }
		    // If this is not the last rank, append a slash to the FEN string to separate it from the next rank.
		    if (rank < 7) {
		        fen.append('/');
		    }
		}
		// Return the FEN string.
		return fen.toString();
	}
	
	public boolean makeMove(Square s1, Square s2) {
		return makeMove(s1, s2, false);
	}
	
	public boolean makeMove(Square s1, Square s2, boolean test) {
		/* the 'test' variable is used to prevent the board from asking for user input when makeMove() 
		 is called when parsing moves from an engine line continuation */
		
		if (checkValidMove(s1, s2, test)) {
	        Piece piece = board[s1.getY()][s1.getX()];
	        Piece piece2 = board[s2.getY()][s2.getX()];
	        
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
	        
	        // Update position attributes of piece to the new square, and add a move
	        piece.setPosition(s2);
	        piece.addMove();
	        
	        clearLastMoved(); // Function to set lastMoved attribute of all pieces on board to false
	        piece.setLastMoved(true);

	        if (piece instanceof Pawn) {
	            Pawn pawn = (Pawn) piece;
	            if (!pawn.isPromoted()) {
	            	// remove enemy pawn from board if en passant
	            	if (piece2 == null && pawn.canBeCapturedEnPassant(board, pawn.getColor(), s1, s2)) {
		                board[s1.getY()][s2.getX()] = null;
		            }
		            
	            	// ask which piece to promote to if pawn promotion
		            if (pawn.checkForPromotion(s2)) {
		            	Piece type = new Queen(pawn.getColor(), s2.getX(), s2.getY());
		            	char promoteTo = 'q';
		            	
		            	if (!test) {
		            		// receive user input if active board
			            	String input = JOptionPane.showInputDialog(null, "Promote to: (default is queen)", 
			            			"Pawn promotion", JOptionPane.QUESTION_MESSAGE);		  
			            	if (!input.equals("")) {
			            		// trying to account for all potential spelling errors
				            	promoteTo = Character.toLowerCase(input.charAt(0));
			            	}
		            	}
		            	
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
		          				type = new Queen(pawn.getColor(), s2.getX(), s2.getY());
	            	      }
	            	    } catch (Exception e) {
	            	    }
		            	
		            	pawn.promote(type);
		            	pawn.getPromotedTo().setPosition(pawn.getPosition());
		            	pawn.getPromotedTo().setMoves(pawn.getMoves());
		            }
	            } else { // update attributes of promoted piece as well
	            	pawn.getPromotedTo().setPosition(pawn.getPosition());
	            	pawn.getPromotedTo().addMove();
	            	pawn.getPromotedTo().setLastMoved(true);
	            }
	        }
	        
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
		            
		         // retrieve the selected rook from board
		            Piece rook = board[s2.getY()][rookX]; 
		         // update position attributes of rook
		            rook.setPosition(new Square(rookDestX, s2.getY())); 
		         // move rook on the board
		            board[s2.getY()][rookDestX] = rook;
		            board[s2.getY()][rookX] = null;
	        	}
	        }
	        
	        // Move piece to its new square on the board as well
	        board[s2.getY()][s2.getX()] = piece;
	        board[s1.getY()][s1.getX()] = null;
	        
	        // if black moves, increase fullmove number in FEN by 1 and set white to move
	        if (piece.getColor().equals("black")) { 
	        	updateFEN(Integer.toString(Integer.parseInt(boardFEN.split(" ")[5])+1), 5);
	        	updateFEN("w", 1);
	        } else {
	        	updateFEN("b", 1);
	        }
	        
	     // update board FEN after every move
	     /* toFEN() converts the board into the first part of a FEN string and updateFEN() will replace the 
	      * first part of boardFEN with the string from toFEN() */
	        updateFEN(toFEN(board), 0); 
	        
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public void makeMove(String move, boolean test) {
		Square s1 = new Square(move.substring(0, 2));
		Square s2 = new Square(move.substring(2, 4));
		
		makeMove(s1, s2, test);
	}
	
	public boolean checkValidMove(Square s1, Square s2, boolean test) {
		Piece piece = board[s1.getY()][s1.getX()];
		
		if (piece == null) {
			return false;
		}
		
		// Verify that the move is allowed for the piece being moved
		if (piece instanceof King) {
			King king = (King) piece;
			if (!king.checkValidSquare(board, s2, boardFEN.split(" ")[2])) {
				if (!test) {
					JOptionPane.showMessageDialog(null, "Please make a legal move!", "Illegal move", JOptionPane.WARNING_MESSAGE);
				}
		    	return false;
		    }
		} else {
			if (!piece.checkValidSquare(board, s2)) {
				if (!test) {
					JOptionPane.showMessageDialog(null, "Please make a legal move!", "Illegal move", JOptionPane.WARNING_MESSAGE);
				}
				return false;
		    }
		}
		
		// Make a copy of the board to simulate the move and verify that the move does not put the moving player's king in check
	    Piece[][] copyBoard = copyBoard();
	    copyBoard[s2.getY()][s2.getX()] = copyBoard[s1.getY()][s1.getX()];
	    copyBoard[s2.getY()][s2.getX()].setPosition(s2);
	    copyBoard[s1.getY()][s1.getX()] = null;
	    
	    if (isKingInCheck(copyBoard[s2.getY()][s2.getX()].getColor(), copyBoard)) {	 
	    	if (!test) {
	    		JOptionPane.showMessageDialog(null, "Can't leave your king in check!", "Invalid move", JOptionPane.WARNING_MESSAGE);        	
	    	}
    		return false;
	    }
	    
	    // The move is legal
	    return true;
	}

	public boolean isKingInCheck(String color, Piece[][] board) {
		
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
	    if (kingPos != null) {
	    	for (int y = 0; y < 8; y++) {
		        for (int x = 0; x < 8; x++) {
		            Piece p = board[y][x];
		            if (p instanceof King) {
		    			King king = (King) p;
		    			if (!king.getColor().equals(color) && king.checkValidSquare(board, kingPos, "")) {
			            	return true;
			            }
		    		} else if (p != null){
		    			if (!p.getColor().equals(color) && p.checkValidSquare(board, kingPos)) {
			            	return true;
			            }
		    		}
		        }
		    }
	    } else { // shouldn't happen but just in case
	    	System.out.println("no king found");
	    }
	    
	    return false;
	}
	
	private void clearLastMoved() {
		for (int y = 0; y < 8; y++) {
	        for (int x = 0; x < 8; x++) {
	            Piece p = board[y][x];
	            if (p != null) {
	            	p.setLastMoved(false);
	            	if (p.getClass().getSimpleName().equals("Pawn")){
	            		Pawn pawn = (Pawn) p;
	            		if (pawn.isPromoted()) {
	            			pawn.getPromotedTo().setLastMoved(false);
	            		}
	            	}
	            }
	        }
	    }
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
	    
	    // more checks that could be implemented:
	    // check that kings are at least one square apart
	    // active color checked less than 3 times

	    return true;
	}
	
	// make a Piece[][] copy of the existing board
	public Piece[][] copyBoard(){
		Piece[][] copyBoard = new Piece[8][8];
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (board[i][j] != null) {
	            	Piece piece = board[i][j];
	                if (piece instanceof Pawn) {
	                	Pawn pawn = (Pawn) piece;
	                	copyBoard[i][j] = new Pawn(piece.getColor(), pawn.getX(), pawn.getY(), pawn.getMoves(), pawn.getPromotedTo());
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
	
	// takes a string and replaces the selected section of the FEN with the string
	public void updateFEN(String update, int section) {
		String[] FEN = boardFEN.split(" ");
		FEN[section] = update;
		boardFEN = String.join(" ", FEN);
	}
	
	// convert a Stockfish line of continuation to algebraic notation
	public String[] parseContinuation(String[] line) {
		String[] parsedLine = new String[line.length];
		
		ChessBoard copy = new ChessBoard(boardFEN);
		
		for (int i=0;i<line.length;i++) {
			parsedLine[i] = copy.parseMove(line[i]);
			copy.makeMove(line[i], true);
		}
		
		return parsedLine;
	}
	
	// convert a move from square-square to algebraic notation
	public String parseMove(Move move) {
		String moveName = "";
		Square s1 = move.getStartSquare();
		Square s2 = move.getEndSquare();
		
		Piece piece1 = board[s1.getY()][s1.getX()];
		Piece piece2 = board[s2.getY()][s2.getX()];
		
		if (piece1 != null) { // move is only possible if piece1 is a piece
			if (piece1.getClass().getSimpleName().equals("Knight")) {
				moveName = "N";
			} else if (!piece1.getClass().getSimpleName().equals("Pawn")){
				moveName = Character.toString(piece1.getClass().getSimpleName().charAt(0));
			} else {
				// if pawn is promoted use the class name of the promoted piece
				Pawn pawn = (Pawn) piece1;
				if (pawn.getPromotedTo() != null) {
					if (pawn.getPromotedTo().getClass().getSimpleName().equals("Knight")) {
    					moveName += "N";
    				} else {
    					moveName += Character.toString(pawn.getPromotedTo().getClass().getSimpleName().charAt(0));
    				}
				}
			}
			
			if (piece2 != null) {
				// checking if pawn takes
				if (piece1.getClass().getSimpleName().equals("Pawn")){
					Pawn pawn = (Pawn) piece1;
					if (pawn.getPromotedTo() == null) {
						moveName += Character.toString(piece1.getPosition().getName().charAt(0));
					}
				}
				moveName += "x";
			} else {
				// checking for en passant
				if (piece1.getClass().getSimpleName().equals("Pawn")){
					Pawn pawn = (Pawn) piece1;
					if (pawn.canBeCapturedEnPassant(board, pawn.getColor(), s1, s2)) {
						moveName += Character.toString(piece1.getPosition().getName().charAt(0))+"x";
					}
				}
			}
			
			moveName += s2.getName();
			
			// Make a copy of the board to simulate the move and see if the move causes pawn promotion / puts the opponent's king in check
		    ChessBoard copy = new ChessBoard(boardFEN);
		    if(copy.makeMove(s1, s2, true)) {
		    	Piece pc = copy.getBoard()[s2.getY()][s2.getX()];
		    	if (pc.getClass().getSimpleName().equals("Pawn")) {
		    		Pawn pawn = (Pawn) pc;
		    		if (pawn.getPromotedTo() != null) {
		    			if (pawn.getPromotedTo().getMoves() == 0) {
		    				moveName += "=Q"; // if promotion on real board, timeline will be updated from UI to get specific piece promoted to
		    			}
		    		}
		    	}
		    	if (copy.isKingInCheck(getColor() == 'w' ? "black" : "white", copy.getBoard())) {
			    	moveName += "+";
			    }
		    }
		}
		
		return moveName;
	}
	
	public String parseMove(String move) {
		Square s1 = new Square(move.substring(0, 2));
		Square s2 = new Square(move.substring(2, 4));
		
		return parseMove(new Move(s1, s2));
	}
	
//	public boolean isStalemate(Color color) {
//	    // Check if the player's king is not in check and there are no legal moves left
//	    if (!isInCheck(color) && !hasLegalMoves(color)) {
//	        return true;
//	    }
//	    return false;
//	}
//
//	public boolean isCheckmate(Color color) {
//	    // Check if the player's king is in check and there are no legal moves left
//	    if (isInCheck(color) && !hasLegalMoves(color)) {
//	        return true;
//	    }
//	    return false;
//	}
	
	// print the board to console
	public void showBoard() {
		for (int i = 0; i < board.length; i++) {
        	for (int k = 0; k<board[0].length; k++) {
        		System.out.println(board[i][k]+" "+i+k);
        	}
        }
	}
	
	public String getSquareColor(Square s) {
		Piece piece = board[s.getY()][s.getX()];
		return piece.getColor();
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
