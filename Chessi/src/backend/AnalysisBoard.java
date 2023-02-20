package backend;

public class AnalysisBoard {
	private Game currentGame;
	
	public AnalysisBoard() {
		this.currentGame = new Game();
	}
	
	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public void resetAnalysis() {
		
	}
	
	public void startAnalysis(Piece[][] pieces) {
		
	}
	
	public void makeMove(Move move) {
		
	}
	
	public void back() {
		
	}
	
	public void forward() {
		
	}
	
	public boolean checkFEN(String FEN) {
		currentGame.getTheBoard().updateBoard(FEN);
		return currentGame.getTheBoard().isValidChessPosition();
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
