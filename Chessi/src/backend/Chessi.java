package backend;

public class Chessi {
	private GuessTheEval GTE;
	private AnalysisBoard Analysis;
	
	public static void main (String[] args) {
		Chessi X = new Chessi();
	}
	
	public Chessi() {
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
