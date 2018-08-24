package components;

import java.awt.Color;

public class Range {

	private int min;
	private int max;
	private Color c;
	
	public Range(int x, int y, Color c1){
		min = x;
		max = y;
		c = c1;
	}
	
	public int getMin(){
		return min;
	}
	
	public int getMax(){
		return max;
	}
	
	public Color getColor(){
		return c;
	}
	
	public void changeMin(int x){
		min = x;
	}
	
	public void changeMax(int x){
		max = x;
	}
	
	public void changeColor(Color c1){
		c = c1;
	}
	
}
