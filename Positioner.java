package components;


public class Positioner {

	private float panelHeight;
	private float minHeight;
	private float maxHeight;
	
	public Positioner(int h, int min, int max){
		panelHeight = h;
		minHeight = min;
		maxHeight = max;
	}
	
	public int getAssociatedPosition(int i){
		float percentage = (((i-minHeight))/(maxHeight-minHeight));
		return (int)(panelHeight-(panelHeight*percentage));
	}
	
}
