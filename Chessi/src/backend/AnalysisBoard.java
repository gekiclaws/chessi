package backend;

import java.util.LinkedList;

import stockfish.UCI;
import stockfish.UCIResponse;
import stockfish.model.Analysis;

public class AnalysisBoard {
	private String result;
	private String timeline;
	private LinkedList<Move> movesPlayed;
	private ChessBoard theBoard;
	
	private UCI engine;
	
	public AnalysisBoard() {
		// adapted from https://github.com/nomemory/neat-chess
		engine = new UCI();
		movesPlayed = new LinkedList<Move>();
		theBoard = new ChessBoard();
		timeline = "";
		result = "";
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
	
	public ChessBoard getTheBoard() {
		return theBoard;
	}
	
	public void makeMove(Move move, String moveName, char toMove, int moveNo) {
		movesPlayed.add(move);
		
		// If timeline string is empty, add move number and move name. 
		// If white is to move or it is the start of the line, add move number before move name
		if (timeline.equals("")) {
		    timeline += moveNo + ". " + moveName + " ";
		    // If it is black's turn to move, increment the move number
		    moveNo = (toMove == 'b' ? moveNo+1 : moveNo);
		    // Change the player to move
		    toMove = (toMove == 'w' ? 'b' : 'w');
		} else {
		    // If it is white's turn to move, add move number before move name
		    if (toMove == 'w') {
		        timeline += moveNo + ". " + moveName + " ";
		        // Change the player to move
		        toMove = 'b';
		    } else {
		        // If it is black's turn to move, add only the move name
		        timeline += moveName + " ";
		        // Change the player to move
		        toMove = 'w';
		        // Increment the move number
		        moveNo += 1;
		    }
		}

	}

	public void resetAnalysis() {
		movesPlayed = new LinkedList<Move>();
		result = "";
		timeline = "";
		theBoard = new ChessBoard();
	}
	
//	public void back() {
//		
//	}
//	
//	public void forward() {
//		
//	}
	
	public String[] getLines() {
		String[] lines =  new String[3];
		
		// adapted from https://github.com/nomemory/neat-chess
		engine.startStockfish();
		engine.setOption("MultiPV", "3");
		engine.uciNewGame();
		engine.positionFen(theBoard.getBoardFEN());
		UCIResponse<Analysis> response = engine.analysis(10);
		engine.close();
		var analysis = response.getResultOrThrow();

		// get possible continuations
		var moves = analysis.getAllMoves(); // get all possible moves
		moves.forEach((idx, move) -> { // iterate over each move
		    char toMove = theBoard.getColor(); // get current player color
		    int moveNo = theBoard.getMoveNo(); // get current move number

		    // create array of string with length equal to move continuation plus one for the initial move
		    String[] line = new String[move.getContinuation().length+1];

		    line[0] = move.getLan(); // add initial move to the line array
		    for (int i = 0; i < move.getContinuation().length; i++) {
		        line[i+1] = move.getContinuation()[i]; // add each subsequent move to the line array
		    }

		    String[] parsedLine;
		    if (line.length > 2) { // if there are more than two moves in the continuation
		        parsedLine = theBoard.parseContinuation(line); // parse the continuation to create an array of strings
		    } else { // if there are only one or two moves in the continuation
		        parsedLine = new String[1];
		        parsedLine[0] = theBoard.parseMove(line[0]); // parse the initial move to create an array of strings
		    }

		    String lineStr = "";
		    for (int i = 0; i < parsedLine.length; i++) { // iterate over each move in the parsedLine array
		        if (toMove == 'w') { // if it's white's turn
		            lineStr += moveNo + ". " + parsedLine[i] + " "; // add the move number and move to lineStr
		            toMove = 'b'; // change the player color to black
		        } else { // if it's black's turn
		            lineStr += parsedLine[i] + " "; // add the move to lineStr
		            toMove = 'w'; // change the player color to white
		            moveNo += 1; // increment the move number
		        }
		    }

		    if (theBoard.getColor() == 'w') { // if it's white's turn
		        lines[idx-1] = move.getStrength() + " | " + lineStr; // add move strength and lineStr to lines array
		    } else { // if it's black's turn
		        if (move.getStrength().isForcedMate()) { // if the move is a forced mate
		            lines[idx-1] = "-"+move.getStrength() + " | " + lineStr; // add move strength and lineStr to lines array with "-" sign
		        } else { // if the move is not a forced mate
		            lines[idx-1] = move.getStrength().getScore()*-1 + " | " + lineStr; // add move strength score and lineStr to lines array with negative sign
		        }
		    }
		});


		return lines;
	}
	
	public boolean checkFEN(String FEN) {
		boolean valid = false;
		if (isValidFEN(FEN)) {
			theBoard.updateBoard(FEN);
			if (theBoard.isValidChessPosition()) {
				valid = true;
			}
			theBoard.resetBoard();
		}
		return valid;
	}
	
	public static boolean isValidFEN(String fen) {
	    String[] parts = fen.split(" ");
	    
	    if (parts.length != 6) {
	        return false;
	    }

	    // Check the first part of the FEN string, which represents the piece placement on the board.
	    // Split the FEN string into rows using the forward slash character as a delimiter.
		String[] rows = parts[0].split("/");
		// If there are not exactly 8 rows, the FEN string is invalid and the method returns false.
		if (rows.length != 8) {
			return false;
		}
		// Iterate over each row in the FEN string.
		for (int k = 0; k < rows.length; k++) {
			 String row = rows[k];
			 int sum = 0;
			 // Iterate over each character in the current row.
			 for (int i = 0; i < row.length(); i++) {
				 char c = row.charAt(i);
				 // If the character is a digit, add its numeric value to the sum.
				 if (Character.isDigit(c)) {
					 sum += Character.getNumericValue(c);
				 // If the character represents a valid chess piece, add 1 to the sum.
				 } else if ("kKqQrRbBnNpP".indexOf(c) != -1) {
					 sum += 1;
				 // If the character is not a digit or a valid chess piece, the FEN string is invalid and the method returns false.
				 } else {
					 return false;
				 }
			 }
			 // If the sum of the values in the current row is not 8, the FEN string is invalid and the method returns false.
			 if (sum != 8) {
				 return false;
			 }
		}

	    // Check the second part of the FEN string (the active color)
	    if (!parts[1].equals("w") && !parts[1].equals("b")) {
	        return false;
	    }

	    // Check the third part of the FEN string (the castling availability)
	    if (!parts[2].matches("^-|[KQkq]+")) {
	        return false;
	    }

	    // Check the fourth part of the FEN string (the en passant target square)
	    if (!parts[3].matches("^-|[a-h][36]")) {
	        return false;
	    }

	    // Check the fifth part of the FEN string (the halfmove clock)
	    if (!parts[4].matches("\\d+")) {
	        return false;
	    }

	    // Check the sixth part of the FEN string (the fullmove number)
	    if (!parts[5].matches("\\d+")) {
	        return false;
	    }

	    return true;
	}

}
