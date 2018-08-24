package components;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	private JMenuBar menuBar;
	private JMenu menuButton_File;
	private JMenu menuButton_Options;
	private JMenu menuButton_Help;
	private JMenu subMenu_DefaultPalettes;
	private JMenu subMenu_DrawingRange;
	private JMenu subMenu_DrawingPalette;
	private JMenuItem menuSubButton_Open;
	private JMenuItem menuSubButton_About;
	private JMenuItem menuSubButton_CreatePalette;
	private JMenuItem menuSubButton_RealWorld;
	private JMenuItem menuSubButton_Cyanogen;
	private JMenuItem menuSubButton_Normalized;
	private JMenuItem menuSubButton_Centered;
	private JMenuItem menuSubButton_Solid;
	private JMenuItem menuSubButton_Gradient;
	
	private JLabel label_paletteProperties;
	
	private JPanel mapPanel;
	private JPanel palettePanel;
	private JPanel rangesPanel;
	
	private JLabel signatureLabel;
	
	private PaletteManager manager;

	
	private Map currentMap;
		
	
	 // Launch the application.
	
	 public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Create the frame.
	 
	public MainWindow() {
		
		this.setResizable(false);
		
		setTitle("SRTMV v1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
				
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuButton_File = new JMenu("File");
		menuBar.add(menuButton_File);
		
		menuSubButton_Open = new JMenuItem("Open...");
		menuButton_File.add(menuSubButton_Open);
		menuSubButton_Open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileHandler f = new FileHandler(MainWindow.this, 1201, 1201);
				if (f.fileLoaded()){
					manager.autoCreatePalettes(f.getHeights());
					manager.setDefaultPalette(0);
					manager.paintGradientCentered();
					currentMap = new Map(mapPanel, f.getHeights());
					manager.paintMap(currentMap);
					menuSubButton_RealWorld.setEnabled(true);
					menuSubButton_Cyanogen.setEnabled(true);
					menuSubButton_Centered.setEnabled(true);
					menuSubButton_Normalized.setEnabled(true);
					menuSubButton_Gradient.setEnabled(true);
					menuSubButton_Solid.setEnabled(true);
					menuSubButton_CreatePalette.setEnabled(true);
				}
			}
		});
		
		menuButton_Options = new JMenu("Options");
		menuBar.add(menuButton_Options);
				
		menuSubButton_CreatePalette = new JMenuItem("Create Palette...");
		menuButton_Options.add(menuSubButton_CreatePalette);
		menuSubButton_CreatePalette.setEnabled(false);
		menuSubButton_CreatePalette.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.createPalette(currentMap);
			}
		});
		
		subMenu_DefaultPalettes = new JMenu("Select default palette");
		menuButton_Options.add(subMenu_DefaultPalettes);
		
		menuSubButton_RealWorld = new JMenuItem("Real Word");
		menuSubButton_RealWorld.setEnabled(false);
		subMenu_DefaultPalettes.add(menuSubButton_RealWorld);
		menuSubButton_RealWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.setDefaultPalette(0);
				manager.paintGradientCentered();	
				manager.paintMap(currentMap);
			}
		});
		
		menuSubButton_Cyanogen = new JMenuItem("Cyanogen Style");
		menuSubButton_Cyanogen.setEnabled(false);
		subMenu_DefaultPalettes.add(menuSubButton_Cyanogen);
		menuSubButton_Cyanogen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.setDefaultPalette(1);
				manager.paintGradientCentered();
				manager.paintMap(currentMap);
			}
		});
		
		subMenu_DrawingRange = new JMenu("Set range drawing style");
		menuButton_Options.add(subMenu_DrawingRange);
		
		menuSubButton_Normalized = new JMenuItem("Normalized");
		menuSubButton_Normalized.setEnabled(false);
		subMenu_DrawingRange.add(menuSubButton_Normalized);
		menuSubButton_Normalized.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					manager.paintGradientNormalized();
			}
		});
		
		menuSubButton_Centered = new JMenuItem("Centered");
		menuSubButton_Centered.setEnabled(false);
		subMenu_DrawingRange.add(menuSubButton_Centered);
		menuSubButton_Centered.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.paintGradientCentered();
			}
		});		
		
		subMenu_DrawingPalette = new JMenu("Set palette drawing style");
		menuButton_Options.add(subMenu_DrawingPalette);
		
		menuSubButton_Solid = new JMenuItem("Solid");
		menuSubButton_Solid.setEnabled(false);
		subMenu_DrawingPalette.add(menuSubButton_Solid);
		menuSubButton_Solid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.paintSolid();
			}
		});		
		
		menuSubButton_Gradient = new JMenuItem("Gradient");
		menuSubButton_Gradient.setEnabled(false);
		subMenu_DrawingPalette.add(menuSubButton_Gradient);
		menuSubButton_Gradient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.paintGradient();
			}
		});		
		
		menuButton_Help = new JMenu("Help");
		menuBar.add(menuButton_Help);
		
		menuSubButton_About = new JMenuItem("About...");
		menuButton_Help.add(menuSubButton_About);
		menuSubButton_About.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = new String();
				message = "SMRTMV is an HGT viewer based on the files obtained by NASA's Shuttle Radar Topography Mission." + "\n"+"Created and designed by Ramiro Diaz";
				JOptionPane.showMessageDialog(MainWindow.this, message , "About", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mapPanel = new JPanel();
		mapPanel.setBackground(Color.DARK_GRAY);
		mapPanel.setBounds(6, 6, 745, 712);
		contentPane.add(mapPanel);
		
		label_paletteProperties = new JLabel("Palette Properties");
		label_paletteProperties.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		label_paletteProperties.setBounds(822, 6, 157, 37);
		contentPane.add(label_paletteProperties);
		
		palettePanel = new JPanel();
		palettePanel.setBounds(894, 55, 102, 638);
		contentPane.add(palettePanel);
		
		rangesPanel = new JPanel();
		rangesPanel.setBounds(787, 55, 102, 638);
		contentPane.add(rangesPanel);
		rangesPanel.setLayout(null);
		
		signatureLabel = new JLabel("Ramiro Diaz");
		signatureLabel.setForeground(Color.GRAY);
		signatureLabel.setBounds(942, 702, 76, 16);
		contentPane.add(signatureLabel);
		
		// Creating the PaletteManager

		manager = new PaletteManager(palettePanel, rangesPanel);
	}
}
