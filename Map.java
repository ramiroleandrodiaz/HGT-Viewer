package components;

import java.util.Vector;

import javax.swing.JPanel;

public class Map {

	private Vector<Integer> heights;
	private JPanel mapPanel;
	
	public Map(JPanel panel, Vector<Integer> parameterHeights){
		heights = parameterHeights;
		mapPanel = panel;
	}
	
	public void paint(ColorPalette palette){
		palette.paintMap(mapPanel, heights);
	}
	
}
