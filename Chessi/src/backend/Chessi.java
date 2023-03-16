package backend;

import java.sql.SQLException;

public class Chessi {
	private GuessTheEval GTE;
	private AnalysisBoard Analysis;
	
	public Chessi() throws SQLException {
		GTE = new GuessTheEval();
		Analysis = new AnalysisBoard();
	}

	public GuessTheEval getGTE() {
		return GTE;
	}

	public AnalysisBoard getAnalysis() {
		return Analysis;
	}
}


