package backend;

public class Square {
	private String name;
	private int x;
	private int y;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		
		// Adapted from https://stackoverflow.com/questions/10813154/how-do-i-convert-a-number-to-a-letter-in-java
		name = x > -1 && x < 26 ? String.valueOf((char)(x + 97)) : null;
		name = name + Integer.toString(8-y);
	}
	
	public Square(String name) {
	    this.name = name;
	    this.x = name.charAt(0) - 'a';  // subtract the ASCII value of 'a' to get the file number (0 to 7)
	    this.y = 8 - (name.charAt(1) - '0');  /* subtract the ASCII value of '0' to get the rank number (1 to 8), 
	    then subtract from 8 to convert to the Cartesian coordinate system */
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String toString() {
		return name + " " + x + " " + y;
	}
	
}
