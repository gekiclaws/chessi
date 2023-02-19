package backend;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Random;

import stockfish.UCI;
import stockfish.UCIResponse;
import stockfish.model.Analysis;

import java.sql.*;

public class GuessTheEval {
	private Move moveGuess;
	private ChessBoard theBoard;
	private LinkedList<String> database;
	private int activePos;
	private UCI engine;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		GuessTheEval X = new GuessTheEval();
	}
	
	public GuessTheEval() {
		theBoard = new ChessBoard();
		database = new LinkedList<String>();
		activePos = 2;
		
		engine = new UCI();
		engine.startStockfish();
		engine.setOption("MultiPV", "3");
		
//		Class.forName("com.mysql.cj.jdbc.Driver");
//
////		String url = "jdbc:mysql://localhost:3306/mydatabase";
////		String user = "root";
////		String password = "#GekiRed3663";
////		Connection conn = DriverManager.getConnection(url, user, password);
////
////		Statement stmt = conn.createStatement();
////		
////		String sql = "UPDATE GTE SET attempted = ?, FEN = ?, explanation = ? WHERE id = ?";
////		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
////			pstmt.setInt(1, 0);
////			pstmt.setString(2, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
////		    pstmt.setString(3, "hi");
////		    pstmt.setInt(4, 0);
////		    
////		    int rowsUpdated = pstmt.executeUpdate();
////		    if (rowsUpdated > 0) {
////		        System.out.println("Row updated successfully.");
////		    } else {
////		        System.out.println("No row was updated.");
////		    }
////		} catch (SQLException e) {
////		    System.out.println(e.getMessage());
////		}
////		
////		ResultSet rs = stmt.executeQuery("SELECT * FROM GTE");
////		while (rs.next()) {
////		    int id = rs.getInt("id");
////		    String FEN = rs.getString("FEN");
////		    System.out.println(id);
////		    System.out.println(FEN);
////		}
////		
////		stmt.close();
////		conn.close();
		
		// adapted from https://github.com/nomemory/neat-chess
		
		database.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		database.add("8/5k2/3p4/1p1Pp2p/pP2Pp1P/P4P1K/8/8 w - - 0 1");
		database.add("r1bqr1k1/pp3ppp/1npb4/3pN2n/3P1P1P/2PB1N2/PP3PP1/R2QR1K1 w - - 1 14");
		
	}
	
	public void resetBoard() {
		theBoard.updateBoard(database.get(activePos));
	}

	public void loadNewPosition() {
		Random random = new Random();
        activePos = random.nextInt(3);
        
		theBoard.updateBoard(database.get(activePos));
	}
	
	public String[] checkAnswers(double eval) {
		String[] feedback =  new String[6];
		
		DecimalFormat df = new DecimalFormat("0.##");
		df.setMaximumIntegerDigits(3);
		eval = Double.parseDouble(df.format(eval));
		
		// adapted from https://github.com/nomemory/neat-chess
		engine.uciNewGame();
		engine.positionFen(theBoard.getBoardFEN());
		UCIResponse<Analysis> response = engine.analysis(20);
		var analysis = response.getResultOrThrow();

		// get possible best moves
		var moves = analysis.getAllMoves();
		moves.forEach((idx, move) -> {
		    char toMove = theBoard.getColor();
			int moveNo = theBoard.getMoveNo();
			
			String[] line = new String[move.getContinuation().length+1];
			line[0] = move.getLan();
			for (int i = 0; i < move.getContinuation().length; i++) {
			    line[i+1] = move.getContinuation()[i];
			}
			
			String[] parsedLine = theBoard.parseContinuation(line);
			String lineStr = "";
			
			for (int i = 0; i < parsedLine.length; i++) {
				if (lineStr == "") {
					lineStr += moveNo + ". " + parsedLine[i] + " "; // formatting, only add new move no if white to move or at start of line
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
		    
		    feedback[idx-1] = move.getStrength() + " | " + lineStr;
		});

		String bestMove = theBoard.parseMove(analysis.getBestMove().getLan());
		feedback[3] = "<html>The best move was " + bestMove + "<br>"+theBoard.parseMove(moveGuess)+" was " + (bestMove.equals(theBoard.parseMove(moveGuess)) ? "right!" : "wrong!");
		
		double correctEval = analysis.getBestMove().getStrength().getScore(); // assumes eval isn't mate-in-x
    	feedback[4] = "<html>The correct eval was " + correctEval + "<br>"+eval+" was " + (eval == correctEval ? "right!" : "off by "+Double.parseDouble(df.format(eval-correctEval))) + "<html>";
    	
//    	feedback[5] = "hello";
		
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

	public LinkedList<String> getDatabase() {
		return database;
	}

	public void setDatabase(LinkedList<String> database) {
		this.database = database;
	}
	
	
	
}
