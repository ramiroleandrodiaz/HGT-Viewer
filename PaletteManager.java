package components;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JPanel;

public class PaletteManager {
	
	private Vector<ColorPalette> defaultPalettes;
	private ColorPalette currentPalette;
	private JPanel drawingPanel;
	private JPanel rangesPanel;
	private int rangePaintingMethod;
	private int palettePaintingMethod;
	public boolean mapLoaded;
	
	public PaletteManager(JPanel dPanel, JPanel rPanel){
		
		defaultPalettes = new Vector<ColorPalette>();
		drawingPanel = dPanel;
		rangesPanel = rPanel;
		rangePaintingMethod = 0;
		mapLoaded = false;		
	}
	
	public void setDefaultPalette(int i){
		currentPalette = defaultPalettes.elementAt(i);
}
	
	public void paintGradientNormalized(){
		if (palettePaintingMethod == 0){
			currentPalette.paintGradientNormalized();
		}
		else{
			currentPalette.paintNormalized();
		}
		rangePaintingMethod = 0;
	}
	
	public void paintGradientCentered(){
		if (palettePaintingMethod == 0){
			currentPalette.paintGradientCentered();
		}
		else{
			currentPalette.paintCentered();
		}
		rangePaintingMethod = 1;
	}
	
	public void paintSolid(){
		if (rangePaintingMethod == 0)
			currentPalette.paintNormalized();
		else
			currentPalette.paintCentered();
		palettePaintingMethod = 1;
	}
	
	public void paintGradient(){
		if (rangePaintingMethod == 0)
			currentPalette.paintGradientNormalized();
		else
			currentPalette.paintGradientCentered();		
		palettePaintingMethod = 0;
	}
	
	public void autoCreatePalettes(Vector<Integer> heights){
		Vector<Integer> aux = new Vector<Integer>(heights);
		int rangeSize = (int)aux.size()/6;
		int i = 0;
		Collections.sort(aux);
		
		defaultPalettes.clear();
		
		currentPalette = new ColorPalette(drawingPanel, rangesPanel);
		Color c1 = new Color(153, 204, 102);
		Color c2 = new Color(204, 255, 204);
		Color c3 = new Color(255, 255, 153);
		Color c4 = new Color(255, 204, 153);
		Color c5 = new Color(204, 153, 102);
		Color c6 = new Color(153, 102, 51);
		
		currentPalette.add(aux.elementAt(i), aux.elementAt(i+rangeSize), c1);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize), c2);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize), c3);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize), c4);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize), c5);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize-1), c6);
		
		defaultPalettes.add(currentPalette);
		
		currentPalette = new ColorPalette(drawingPanel, rangesPanel);
		
		rangeSize = (int)aux.size()/3;
		i = 0;
		c1 = new Color(0, 0, 10);
		c2 = new Color(0, 255, 255);
		c3 = new Color(255, 255, 255);
		
		currentPalette.add(aux.elementAt(i), aux.elementAt(i+rangeSize), c1);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize), c2);
		i = i + rangeSize;
		currentPalette.add(aux.elementAt(i+1), aux.elementAt(i+rangeSize-1), c3);
		
		defaultPalettes.add(currentPalette);
		
		currentPalette = null;		
	}
	
	public void setPalette(ColorPalette p){
		currentPalette = p;
	}
	
	public void createPalette(Map m){
		PaletteChooser chooser = new PaletteChooser();
		chooser.setManager(this);
		chooser.setMap(m);	
		chooser.setVisible(true);	
	}
	
	public void resetPanels(){
		currentPalette.setPanels(drawingPanel, rangesPanel);
	}
	
	public void paintMap(Map m){
		m.paint(currentPalette);
	}
	
}
