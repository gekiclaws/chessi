package backend;

import java.sql.SQLException;
import java.text.DecimalFormat;
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
		
		if (timeline.equals("")) {
			timeline += moveNo + ". " + moveName + " "; // formatting - only add new move no. if white to move or at start of line
			moveNo = (toMove == 'b' ? moveNo+1 : moveNo);
			toMove = (toMove == 'w' ? 'b' : 'w');
		} else {
			if (toMove == 'w') {
				timeline += moveNo + ". " + moveName + " ";
		    	toMove = 'b';
		    } else {
		    	timeline += moveName + " ";
		    	toMove = 'w';
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
		UCIResponse<Analysis> response = engine.analysis(18);
		engine.close();
		var analysis = response.getResultOrThrow();

		// get possible continuations
		var moves = analysis.getAllMoves();
		moves.forEach((idx, move) -> {
			char toMove = theBoard.getColor();
			int moveNo = theBoard.getMoveNo();
			
			String[] line = new String[move.getContinuation().length+1]; // trim move.getContinuation().length to 5 moves
			line[0] = move.getLan();
			for (int i = 0; i < move.getContinuation().length; i++) {
			    line[i+1] = move.getContinuation()[i];
			}
			
			String[] parsedLine;
			if (line.length > 2) {
				parsedLine = theBoard.parseContinuation(line);
			} else {
				parsedLine = new String[1];
				parsedLine[0] = theBoard.parseMove(line[0]);
			}
			
			String lineStr = "";
			
			for (int i = 0; i < parsedLine.length; i++) {
				if (toMove == 'w') {
			    	lineStr += moveNo + ". " + parsedLine[i] + " ";
			    	toMove = 'b';
			    } else {
			    	lineStr += parsedLine[i] + " ";
			    	toMove = 'w';
			    	moveNo += 1;
			    }
			}
		    
			if (theBoard.getColor() == 'w') {
				lines[idx-1] = move.getStrength() + " | " + lineStr;
			} else {
				if (move.getStrength().isForcedMate()) {
					lines[idx-1] = "-"+move.getStrength() + " | " + lineStr;
				} else {
					lines[idx-1] = move.getStrength().getScore()*-1 + " | " + lineStr;
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

	    // Check the first part of the FEN string (the piece placement)
	    String[] rows = parts[0].split("/");
	    if (rows.length != 8) {
	        return false;
	    }
	    for (int k = 0; k < rows.length; k++) {
	        String row = rows[k];
	        int sum = 0;
	        for (int i = 0; i < row.length(); i++) {
	            char c = row.charAt(i);
	            if (Character.isDigit(c)) {
	                sum += Character.getNumericValue(c);
	            } else if ("kKqQrRbBnNpP".indexOf(c) != -1) {
	                sum += 1;
	            } else {
	                return false;
	            }
	        }
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
