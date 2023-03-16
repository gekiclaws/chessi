package backend;

import java.text.DecimalFormat;
import java.util.LinkedList;

import stockfish.UCI;
import stockfish.UCIResponse;
import stockfish.model.Analysis;

import java.sql.*;

public class GuessTheEval {
	private UCI engine;
	
	private ChessBoard theBoard;
	private Move moveGuess;
	
	private int activeId;
	private boolean positionAcquired;
	
	private PreparedStatement stmt;
	private String sql;
	private Connection conn;
	
	
	public GuessTheEval() throws SQLException {
		// adapted from https://github.com/nomemory/neat-chess
		engine = new UCI(); // Creating a UCI object to connect to the engine later
				
		theBoard = new ChessBoard();
		positionAcquired = false;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tree", "root", "#GekiRed3663");
		} catch (Exception e) {
			System.out.println(e);
		}
			
		// Create the table if it doesn't exist
		sql = "CREATE TABLE IF NOT EXISTS positions (id INT NOT NULL AUTO_INCREMENT, FEN VARCHAR(90), "
				+ "explanation VARCHAR(1024), attempted TINYINT(1), PRIMARY KEY (id))";
		stmt = conn.prepareStatement(sql);
		stmt.executeUpdate();
		System.out.println("user table created!");
		
		// Code to re-initialize the database (comment out this section after adding positions and testing)
		for (int i = getPositions().size(); i >= 1; i--) {
		    deletePosition(i);
		}
		addPosition("5b1r/ppk2ppp/2p1p1b1/4P3/Q3P3/2N5/PP3qPP/3R1B1K w - - 0 20", "In this position, Nb5+ is the best and only move. This move gives a significant edge to white since after the exchange there is no way to stop Rd7 and Qb7.");
		addPosition("5b1r/ppk2ppp/2p1p1b1/4P3/Q3P3/2N5/PP3qPP/3R1B1K w - - 0 20", "In this position, Nb5+ is the best and only move. This move gives a significant edge to white since after the exchange there is no way to stop Rd7 and Qb7.");
		addPosition("8/5k2/3p4/1p1Pp2p/pP2Pp1P/P4P1K/8/8 w - - 0 1", "This position is a draw, as neither side can make progress. Kg2 or Kh2 are both valid moves here.");
		addPosition("r1bqr1k1/pp3ppp/1npb4/3pN2n/3P1P1P/2PB1N2/PP3PP1/R2QR1K1 w - - 1 14", "In this position, Bxh7+ is the best move and gives an edge to white, as it is an example of a typical greek gift sacrifice on h7.");
		addPosition("8/4R3/5kpp/p1pP4/r3P3/6PP/4K3/8 w - - 2 43", "In this position, d6 is best and gives a significant edge to white as white intends promotion via d6-d7-d8 and black cannot stop this plan.");
		
	}
	
	public void resetBoard() throws SQLException {
		theBoard.updateBoard((String) getPositions().get(activeId-1)[1]);
	}

	public boolean loadNewPosition() throws SQLException {
		if (positionAcquired) {
			return true;
		} else {
			LinkedList<Object[]> positions = getPositions();
			
			// find a position that's not been attempted
			
			// look for an id that hasn't been attempted
			LinkedList<Integer> ids = new LinkedList<Integer>();
	        for (int i = 1; i < positions.size()+1; i++) {
	            ids.add(i);
	        }
	        
	        /* keep going until all positions in database have been checked as to 
	         * whether they have been attempted / usable position has been found */
	        
	        while (!ids.isEmpty() && !positionAcquired) {
	            int randomIndex = (int) (Math.random() * ids.size());
	            int randomId = ids.get(randomIndex);
	            if((int) positions.get(randomId-1)[3] == 0) { // check if attempted
	            	positionAcquired = true;
	            	activeId = randomId;
	            	theBoard.updateBoard((String) positions.get(activeId-1)[1]);
	            } else {
	            	ids.remove(randomIndex);
	            }
	        }
	        
	        // prevent the position from loading in GUI if no positions left
	        return positionAcquired ? true : false;
		}
	}
	
	public String[] checkAnswers(double eval) throws SQLException {
		String[] feedback =  new String[6];
		
		// To reduce the eval to 3 significant figures and a maximum of 2 decimal places
		DecimalFormat df = new DecimalFormat("0.##");
		df.setMaximumIntegerDigits(3);
		eval = Double.parseDouble(df.format(eval));
		
		// adapted from https://github.com/nomemory/neat-chess
		engine.startStockfish();
		engine.setOption("MultiPV", "3");
		engine.uciNewGame();
		engine.positionFen((String) getPositions().get(activeId-1)[1]);
		UCIResponse<Analysis> response = engine.analysis(30);
		engine.close();
		var analysis = response.getResultOrThrow();

		// get possible continuations
		// Get all available moves and iterate through them
		var moves = analysis.getAllMoves();
		moves.forEach((idx, move) -> {
		    // Get the current color and move number on the board
		    char toMove = theBoard.getColor();
		    int moveNo = theBoard.getMoveNo();

		    // Create an array to hold the current move and its continuation
		    String[] line = new String[move.getContinuation().length+1];
		    line[0] = move.getLan();
		    System.out.println(line[0]);
		    for (int i = 0; i < move.getContinuation().length; i++) {
		        line[i+1] = move.getContinuation()[i];
		    }

		    // Parse the move and continuation to get a string representation of the move line
		    String[] parsedLine;
		    if (line.length > 2) {
		        parsedLine = theBoard.parseContinuation(line);
		    } else {
		        parsedLine = new String[1];
		        parsedLine[0] = theBoard.parseMove(line[0]);
		    }

		    // Build the move line string
		    String lineStr = "";
		    for (int i = 0; i < parsedLine.length; i++) {
		        if (lineStr == "") {
		            // Add move number if white to move or at start of line
		            lineStr += moveNo + ". " + parsedLine[i] + " ";
		            moveNo = (toMove == 'b' ? moveNo+1 : moveNo);
		            toMove = (toMove == 'w' ? 'b' : 'w');
		        } else {
		            if (toMove == 'w') {
		                lineStr += moveNo + ". " + parsedLine[i] + " ";
		                toMove = 'b';
		            } else {
		                lineStr += parsedLine[i] + " ";
		                toMove = 'w';
		                moveNo += 1;
		            }
		        }
		    }

		    // Build the feedback string for this move
		    if (theBoard.getColor() == 'w') {
		        feedback[idx-1] = move.getStrength() + " | " + lineStr;
		    } else {
		        if (move.getStrength().isForcedMate()) {
		            feedback[idx-1] = "-"+move.getStrength() + " | " + lineStr;
		        } else {
		            feedback[idx-1] = move.getStrength().getScore()*-1 + " | " + lineStr;
		        }
		    }
		});


		// get best move
		String bestMove = theBoard.parseMove(analysis.getBestMove().getLan());
		feedback[3] = "<html>The best move was " + bestMove + "<br>"+theBoard.parseMove(moveGuess)+" was " 
		+ (bestMove.equals(theBoard.parseMove(moveGuess)) ? "right!" : "wrong!");
		
		// get eval for top move
		double correctEval = analysis.getBestMove().getStrength().getScore(); // assumes eval isn't mate-in-x
    	feedback[4] = "<html>The correct eval was " + correctEval + "<br>"+eval+" was " 
		+ (eval == correctEval ? "right!" : "off by "+Double.parseDouble(df.format(eval-correctEval))) + "<html>";
    	
    	// get explanation from database
    	feedback[5] = (String) getPositions().get(activeId-1)[2];
		
    	updatePosition(activeId); // mark position as 'attempted' in database
    	
		return feedback;
	}

	public Move getMoveGuess() {
		return moveGuess;
	}

	public void setMoveGuess(Move moveGuess) {
		this.moveGuess = moveGuess;
	}
	
	public void resetMoveGuess() {
		this.moveGuess = null;
	}

	public ChessBoard getTheBoard() {
		return theBoard;
	}

	public void setTheBoard(ChessBoard theBoard) {
		this.theBoard = theBoard;
	}
	
	public void updatePositionAcquired() {
		this.positionAcquired = false;
	}

	// MySQL CRUD functions
	
	// CREATE
	// not currently used when program is running, just for adding to database before program use
	public void addPosition(String FEN, String explanation) throws SQLException { 
		sql = "INSERT INTO positions (FEN, explanation, attempted) VALUES (?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        
        stmt.setString(1, FEN);
        stmt.setString(2, explanation);
        stmt.setInt(3, 0);
        stmt.executeUpdate();
	}
	
	// READ
	public LinkedList<Object[]> getPositions() throws SQLException{
		LinkedList<Object[]> positions = new LinkedList<Object[]>();
		
		// Prepare a statement to select data from the table (READ)
        sql = "SELECT * FROM positions";
        stmt = conn.prepareStatement(sql);

        // Execute the query and print the results
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          int id = rs.getInt("id");
          String FEN = rs.getString("FEN");
          String explanation = rs.getString("explanation");
          int attempted = rs.getInt("attempted");
          
          Object[] position = {id, FEN, explanation, attempted};
          positions.add(position);
        }
		
        return positions;
	}
	
	// UPDATE
	/* only for updating whether position has been attempted for now, can be generalized to updating any field but
	 * not necessary at the moment */
	public void updatePosition(int id) throws SQLException {
		sql = "UPDATE positions SET attempted=? WHERE id=?";
        stmt = conn.prepareStatement(sql);

        // Set the parameter values and execute
        stmt.setInt(1, 1);
        stmt.setInt(2, id);
        stmt.executeUpdate();
	}
	
	// DELETE
	public void deletePosition(int id) throws SQLException {
		sql = "DELETE FROM positions WHERE id=?";
        stmt = conn.prepareStatement(sql);

        // Set the parameter value and execute
        stmt.setInt(1, id);
        stmt.executeUpdate();
        
        // Update ids of rows after deleted row
        sql = "UPDATE positions SET id = id - 1 WHERE id > ?;";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();

        sql = "ALTER TABLE positions AUTO_INCREMENT = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, getMaxId() + 1);
        stmt.executeUpdate();

	}
	
	private int getMaxId() throws SQLException {
	    String sql = "SELECT MAX(id) FROM positions";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();
	    if (rs.next()) {
	        return rs.getInt(1);
	    } else {
	        return 0;
	    }
	}

	
}
