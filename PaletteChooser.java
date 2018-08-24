package components;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class PaletteChooser extends JFrame {

	private JPanel contentPane;
	private JTextField textField_From;
	private JTextField textField_To;
	private JLabel label_From;
	private JLabel label_To;
	private JLabel label_PalettePreview;
	private JLabel label_NewRangeProperties;
	private JLabel label_Color;
	private JPanel colorPanel;
	private JPanel palettePanel;
	private JButton button_selectButton;
	private JButton button_addRange;
	private JButton button_save;
	private ColorPalette palette;
	private Color currentColor;
	private PaletteManager manager;
	private Map currentMap;
	private int rangeCounter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PaletteChooser frame = new PaletteChooser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PaletteChooser() {
		
		rangeCounter = 0;
		currentColor = Color.white;
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 490, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setVisible(true);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		palettePanel = new JPanel();
		palettePanel.setBackground(Color.WHITE);
		palettePanel.setBounds(20, 61, 450, 114);
		contentPane.add(palettePanel);
		
		textField_From = new JTextField();
		textField_From.setBounds(20, 266, 80, 28);
		contentPane.add(textField_From);
		textField_From.setColumns(10);
		
		textField_To = new JTextField();
		textField_To.setColumns(10);
		textField_To.setBounds(112, 266, 80, 28);
		contentPane.add(textField_To);
		
		label_From = new JLabel("From");
		label_From.setBounds(44, 246, 37, 16);
		contentPane.add(label_From);
		
		label_To = new JLabel("To");
		label_To.setBounds(141, 246, 16, 16);
		contentPane.add(label_To);
		
		label_PalettePreview = new JLabel("Palette Preview");
		label_PalettePreview.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		label_PalettePreview.setBounds(20, 20, 137, 16);
		contentPane.add(label_PalettePreview);
		
		label_NewRangeProperties = new JLabel("New Range Properties");
		label_NewRangeProperties.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		label_NewRangeProperties.setBounds(20, 204, 201, 30);
		contentPane.add(label_NewRangeProperties);
		
		label_Color = new JLabel("Color");
		label_Color.setBounds(219, 246, 37, 16);
		contentPane.add(label_Color);
		
		colorPanel = new JPanel();
		colorPanel.setBackground(Color.WHITE);
		colorPanel.setBounds(290, 266, 37, 28);
		contentPane.add(colorPanel);
		
		button_selectButton = new JButton("Select");
		button_selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentColor = JColorChooser.showDialog(PaletteChooser.this, "Choose Color", Color.white);
				BufferedImage img = new BufferedImage(colorPanel.getWidth(), colorPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
				paintBufferedSolid(currentColor, 0, img.getWidth(), img);
				
				colorPanel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
			}
		});
		button_selectButton.setBounds(204, 266, 74, 29);
		contentPane.add(button_selectButton);
		
		button_addRange = new JButton("Add Range");
		button_addRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				palette.add(Integer.parseInt(textField_From.getText()), Integer.parseInt(textField_To.getText()), currentColor);
				palette.paintHorizontal();
				rangeCounter++;
				if (rangeCounter >= 2){
					button_save.setEnabled(true);
				}
			}
		});
		button_addRange.setBounds(354, 267, 101, 29);
		contentPane.add(button_addRange);
		
		button_save = new JButton("Save Pallete");
		button_save.setEnabled(false);
		button_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PaletteChooser.this.setVisible(false);
				manager.setPalette(palette);
				manager.resetPanels();
				manager.paintGradientCentered();
				manager.paintMap(currentMap);
			}
		});
		button_save.setBounds(173, 334, 117, 29);
		contentPane.add(button_save);
		
		palette = new ColorPalette(palettePanel, null);
			
	}
	
	public void setManager(PaletteManager m){
		manager = m;
	}
	
	public void setMap(Map m){
		currentMap = m;
	}
	
	private int getGradientColor(int r1, int g1, int b1, int r2, int g2, int b2, int xStart, int xFinish, int iterationValue){
		
		int r, g, b;	
		r = (int)(r1 + (r2 - r1)*((float)(iterationValue-xStart)/(xFinish-xStart)));
		g = (int)(g1 + (g2 - g1)*((float)(iterationValue-xStart)/(xFinish-xStart)));	
		b = (int)(b1 + (b2 - b1)*((float)(iterationValue-xStart)/(xFinish-xStart)));
		
		Color c = new Color(r,g,b);

		return c.getRGB();
	}
	
	
	private void paintBufferedGradient(Color color1, Color color2, int xStart, int xFinish, BufferedImage img){
				
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
	
	private void paintBufferedSolid(Color c, int xStart, int xFinish, BufferedImage img){
		for(int i = xStart; i < xFinish; i++){
			for(int y = 0; y < img.getHeight(); y++)
				img.setRGB(i, y, c.getRGB());
		}
	}
}
