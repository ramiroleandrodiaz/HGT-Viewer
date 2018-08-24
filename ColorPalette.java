package components;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ColorPalette {

	// Class' Attributes
	
	private Vector<Range> ranges;
	private JPanel drawingPanel;
	private JPanel rangesPanel;
	
	// Class' Functions
	
	public ColorPalette(JPanel dPanel, JPanel rPanel){
		ranges = new Vector<Range>();
		drawingPanel = dPanel;
		rangesPanel = rPanel;
	}
	
	public void add(int x, int y, Color c){
		Range r = new Range(x, y, c);
		ranges.add(r);
		
	}
	
	public void setPanels(JPanel dPanel, JPanel rPanel){
		drawingPanel = dPanel;
		rangesPanel = rPanel;
	}
	
	public int getAssociatedRange(int value){
		int i = 0;
		boolean found = false;
		while ((i < ranges.size()) && (!found)){
			if((value >= ranges.elementAt(i).getMin()) && (value <= ranges.elementAt(i).getMax())){
				found = true;
			}
			else{
				i++;
			}
		}
		if (i == ranges.size()){
			i--;
		}
//		if (value < ranges.elementAt(0).getMin()){
//			i = 0;
//		}
		return i;
	}
	
	public void paintMap(JPanel mapPanel, Vector<Integer> heights){
		
		
		int maxX = mapPanel.getWidth();
		int maxY = mapPanel.getHeight();
				
		BufferedImage img = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
		int r1,g1,b1,r2,g2,b2 = 0;
		int percentage = 0;
		int aux;
		for (int i = 0; i < maxX ; i ++){
			for(int j = 0; j < maxY; j++){
				aux = getAssociatedRange(heights.elementAt(i*1201+j));
				if (aux == 0){
					
					percentage = (int) ((heights.elementAt(i*1201+j)-(ranges.elementAt(aux).getMin()))/((ranges.elementAt(aux+1).getMax()-ranges.elementAt(aux).getMin())))*100;

					r1 = ranges.elementAt(aux).getColor().getRed();
					g1 = ranges.elementAt(aux).getColor().getGreen();
					b1 = ranges.elementAt(aux).getColor().getBlue();
					
					r2 = ranges.elementAt(aux+1).getColor().getRed();
					g2 = ranges.elementAt(aux+1).getColor().getGreen();
					b2 = ranges.elementAt(aux+1).getColor().getBlue();

					
				}
				else{
					percentage = (int) ((heights.elementAt(i*1201+j)-(ranges.elementAt(aux-1).getMin()))/((ranges.elementAt(aux).getMax()-ranges.elementAt(aux-1).getMin())))*100;

					r1 = ranges.elementAt(aux-1).getColor().getRed();
					g1 = ranges.elementAt(aux-1).getColor().getGreen();
					b1 = ranges.elementAt(aux-1).getColor().getBlue();
					
					r2 = ranges.elementAt(aux).getColor().getRed();
					g2 = ranges.elementAt(aux).getColor().getGreen();
					b2 = ranges.elementAt(aux).getColor().getBlue();

				}				
				img.setRGB(i, j, getGradientColor(r1, g1, b1, r2, g2, b2, 100, 0, percentage));	// Ac‡ no se est‡ pintando con respecto al panel, sino que se corta hasta el tama–o del panel
			}
		}
		
		mapPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		
	}
	
	
	public void paintGradientCentered(){
	
		// Painting the gradient
		int rangeSize = (int)(drawingPanel.getHeight()/(ranges.size()-1));		// ranges.size()-1 --> If I have 4 colors then I have 3 gradients.

		int i = 1;
		int position = 0;
		BufferedImage img = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		while (i < ranges.size()){
			paintBufferedGradient(ranges.elementAt(ranges.size()-i).getColor(), ranges.elementAt(ranges.size()-1-i).getColor(), position, (position+rangeSize), img);
			position = position + rangeSize;
			i++;
		}
		
		for (int y = position; y < drawingPanel.getHeight(); y++)
			for(int x = 0; x < img.getWidth(); x++)
					img.setRGB(x, y, ranges.elementAt(0).getColor().getRGB());
		
		drawingPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		
		// Painting the ranges
		
		rangesPanel.removeAll();				// Clears rangePanel
		
		JLabel currentRange;
		
		currentRange = new JLabel(Integer.toString(ranges.elementAt(0).getMin()));
		currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
		currentRange.setBounds(35, rangesPanel.getHeight()-16, 60, 16);
		
		rangeSize = (int)(drawingPanel.getHeight()/ranges.size());				// rangeSize now counts all the colors, not gradients.
		
		rangesPanel.add(currentRange);
		int aux = 16;
		for (int j = 0; j < ranges.size(); j++){
			aux = (rangesPanel.getHeight() - (rangeSize)*(j+1));
			currentRange = new JLabel(Integer.toString(ranges.elementAt(j).getMax()));
			currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
			currentRange.setBounds(35, aux, 60, 16);
			rangesPanel.add(currentRange);
		}
		
		rangesPanel.repaint();
	}
	
	public void paintGradientNormalized(){

		// First of all, I calculate the higher and lowest values.
		
		int min = 99999;
		int max = 0;
		
		for (int i=0; i < ranges.size(); i++){
			if ((ranges.elementAt(i).getMin()) < min){
				min = ranges.elementAt(i).getMin();
			}
			if ((ranges.elementAt(i).getMax()) > max){
				max = ranges.elementAt(i).getMax();
			}
		}
		
		// Once I've got them, the paint is simple, because Positioner.getAssociatedPosition() returns
		// the exact pixel where the color has to be painted
		
		Positioner positions = new Positioner(drawingPanel.getHeight(), min, max);
		
		int i = 1;
		BufferedImage img = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
		while (i < ranges.size()){
			paintBufferedGradient(ranges.elementAt(ranges.size()-i).getColor(), ranges.elementAt(ranges.size()-i-1).getColor(), positions.getAssociatedPosition(ranges.elementAt(ranges.size()-i).getMax()) , positions.getAssociatedPosition(ranges.elementAt(ranges.size()-i).getMin()) , img);
			i++;
		}
		paintBufferedGradient(ranges.elementAt(0).getColor(), ranges.elementAt(0).getColor(), positions.getAssociatedPosition(ranges.elementAt(0).getMax()), positions.getAssociatedPosition(ranges.elementAt(0).getMin()), img);

			
		
		drawingPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		
		// Painting the ranges
		
		rangesPanel.removeAll();			// Clears rangePanel
		
		JLabel currentRange;
		
		currentRange = new JLabel(Integer.toString(ranges.elementAt(0).getMin()));
		currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
		currentRange.setBounds(35, rangesPanel.getHeight()-16, 60, 16);
		
		
		rangesPanel.add(currentRange);
		int last = rangesPanel.getHeight()-16;
		int aux;
		for (int j = 0; j < ranges.size(); j++){
			aux = positions.getAssociatedPosition(ranges.elementAt(j).getMax());
			currentRange = new JLabel(Integer.toString(ranges.elementAt(j).getMax()));
			currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
			if (aux+16 < last){
				currentRange.setBounds(35, aux, 60, 16);
				last = aux;
			}
			else{
				currentRange.setBounds(35, last-16, 60, 16);
				last = last - 16;
			}
				
			rangesPanel.add(currentRange);
		}
		
		rangesPanel.repaint();
	}
	
	public void paintCentered(){
		
		// Painting the panel
		
		int rangeSize = (int)(drawingPanel.getHeight()/(ranges.size()));		// ranges.size()-1 --> If I have 4 colors then I have 3 gradients.
		int i = 1;
		int position = 0;
		BufferedImage img = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		while (i <= ranges.size()){
			paintBufferedSolid(ranges.elementAt(ranges.size()-i).getColor(), position, position+rangeSize, img);
			position = position + rangeSize;
			i++;
		}
					
		drawingPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		
		// Painting the ranges
		
		rangesPanel.removeAll();				// Clears rangePanel
				
		JLabel currentRange;
				
		currentRange = new JLabel(Integer.toString(ranges.elementAt(0).getMin()));
		currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
		currentRange.setBounds(35, rangesPanel.getHeight()-16, 60, 16);
				
		rangeSize = (int)(drawingPanel.getHeight()/ranges.size());				// rangeSize now counts all the colors, not gradients.
				
		rangesPanel.add(currentRange);
		int aux = 16;
		for (int j = 0; j < ranges.size(); j++){
			aux = (rangesPanel.getHeight() - (rangeSize)*(j+1));
			currentRange = new JLabel(Integer.toString(ranges.elementAt(j).getMax()));
			currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
			currentRange.setBounds(35, aux, 60, 16);
			rangesPanel.add(currentRange);
		}
				
		rangesPanel.repaint();
	
		
	}
	
	public void paintNormalized(){
		
		// First of all, I calculate the higher and lowest values.
		
		int min = 99999;
		int max = 0;
		
		for (int i=0; i < ranges.size(); i++){
			if ((ranges.elementAt(i).getMin()) < min){
				min = ranges.elementAt(i).getMin();
			}
			if ((ranges.elementAt(i).getMax()) > max){
				max = ranges.elementAt(i).getMax();
			}
		}
		
		// Once I've got them, the paint is simple, because Positioner.getAssociatedPosition() returns
		// the exact pixel where the color has to be painted
		
		Positioner positions = new Positioner(drawingPanel.getHeight(), min, max);
		
		int i = 1;
		BufferedImage img = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
		while (i <= ranges.size()){
			paintBufferedSolid(ranges.elementAt(ranges.size()-i).getColor(),positions.getAssociatedPosition(ranges.elementAt(ranges.size()-i).getMax()) , positions.getAssociatedPosition(ranges.elementAt(ranges.size()-i).getMin()),  img);
			i++;
		}

			
		
		drawingPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		
		// Painting the ranges
		
		rangesPanel.removeAll();			// Clears rangePanel
		
		JLabel currentRange;
		
		currentRange = new JLabel(Integer.toString(ranges.elementAt(0).getMin()));
		currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
		currentRange.setBounds(35, rangesPanel.getHeight()-16, 60, 16);
		
		
		rangesPanel.add(currentRange);
		int last = rangesPanel.getHeight()-16;
		int aux;
		for (int j = 0; j < ranges.size(); j++){
			aux = positions.getAssociatedPosition(ranges.elementAt(j).getMax());
			currentRange = new JLabel(Integer.toString(ranges.elementAt(j).getMax()));
			currentRange.setHorizontalAlignment(SwingConstants.RIGHT);
			if (aux+16 < last){
				currentRange.setBounds(35, aux, 60, 16);
				last = aux;
			}
			else{
				currentRange.setBounds(35, last-16, 60, 16);
				last = last - 16;
			}
				
			rangesPanel.add(currentRange);
		}
		
		rangesPanel.repaint();
	}
	
	public void paintHorizontal(){
		int rangeSize = (int)(drawingPanel.getWidth()/(ranges.size()));		// ranges.size()-1 --> If I have 4 colors then I have 3 gradients.
		int i = 1;
		int position = 0;
		BufferedImage img = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		while (i <= ranges.size()){
			paintBufferedSolidHorizontal(ranges.elementAt(ranges.size()-i).getColor(), position, position+rangeSize, img);
			position = position + rangeSize;
			i++;
		}
		drawingPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	private int getGradientColor(int r1, int g1, int b1, int r2, int g2, int b2, int yStart, int yFinish, int iterationValue){
		
		int r, g, b;	
		r = (int)(r1 + (r2 - r1)*((float)(iterationValue-yStart)/(yFinish-yStart)));
		g = (int)(g1 + (g2 - g1)*((float)(iterationValue-yStart)/(yFinish-yStart)));	
		b = (int)(b1 + (b2 - b1)*((float)(iterationValue-yStart)/(yFinish-yStart)));
		
		Color c = new Color(r,g,b);

		return c.getRGB();
	}
	
	private void paintBufferedGradient(Color color1, Color color2, int yStart, int yFinish, BufferedImage img){
				
		int r1= color1.getRed();
		int r2= color2.getRed();
		int g1= color1.getGreen();
		int g2= color2.getGreen();
		int b1=	color1.getBlue();
		int b2= color2.getBlue();
						
		for (int i = yStart ; i < yFinish ; i++) {
												
				for (int x = 0; x < img.getWidth(); x++){
					img.setRGB(x, i, getGradientColor(r1, g1, b1, r2, g2, b2, yStart, yFinish, i));
				} 
		}
	}
	
	private void paintBufferedSolid(Color c, int yStart, int yFinish, BufferedImage img){
		for(int i = yStart; i < yFinish; i++){
			for(int x = 0; x < img.getWidth(); x++)
				img.setRGB(x, i, c.getRGB());
		}
	}
	
	private void paintBufferedGradientHorizontal(Color color1, Color color2, int xStart, int xFinish, BufferedImage img){
		
		int r1= color1.getRed();
		int r2= color2.getRed();
		int g1= color1.getGreen();
		int g2= color2.getGreen();
		int b1=	color1.getBlue();
		int b2= color2.getBlue();
						
		for (int i = xStart ; i < xFinish ; i++) {
												
				for (int y = 0; y < img.getHeight(); y++){
					img.setRGB(i, y, getGradientColor(r1, g1, b1, r2, g2, b2, xStart, xFinish, i));
				} 
		}
	}
	
	private void paintBufferedSolidHorizontal(Color c, int xStart, int xFinish, BufferedImage img){
		for(int i = xStart; i < xFinish; i++){
			for(int y = 0; y < img.getHeight(); y++)
				img.setRGB(i, y, c.getRGB());
		}
	}
	
}
