package model;

public class Square {
	private String name;
	private int x;
	private int y;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		
		// https://stackoverflow.com/questions/10813154/how-do-i-convert-a-number-to-a-letter-in-java
		name = x > -1 && x < 26 ? String.valueOf((char)(x + 97)) : null;
		name = name + Integer.toString(8-y);
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
	
}
