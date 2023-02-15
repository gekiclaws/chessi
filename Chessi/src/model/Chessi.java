package model;

public class Chessi {
	private GuessTheEval GTE;
	private AnalysisBoard Analysis;
	
	public static void main (String[] args) {
		Chessi X = new Chessi();
		X.test();
	}
	
	public Chessi() {
		GTE = new GuessTheEval();
		Analysis = new AnalysisBoard();
	}
	
	public void test() {
		
	}
}
