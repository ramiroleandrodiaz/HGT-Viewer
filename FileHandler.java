package components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.omg.CORBA.DataInputStream;

public class FileHandler {
	
	public boolean loaded;
	
	private Vector<Integer> heights;

	public FileHandler(JFrame frame, int x, int y){
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("HGT files", "hgt");
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filter);
		int returnValue = chooser.showOpenDialog(frame);
		loaded = returnValue == JFileChooser.APPROVE_OPTION;
		Vector<Integer> data = new Vector<Integer>();

		try {
			if (loaded){
				FileInputStream fstream = new FileInputStream(chooser.getSelectedFile().getPath());
				java.io.DataInputStream dstream = new java.io.DataInputStream(fstream);
				for (int i= 0; i < x; i++) {
					for (int j= 0; j < y; j++) {
						try {
							data.add(i*x+j, (int)dstream.readUnsignedShort());
						} catch (IOException e) {
							JOptionPane.showMessageDialog(frame, "There was an error trying to load the file" , "ERROR", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					}
				}
		    }
		} 
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "There was an error trying to load the file" , "ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		heights = data;
	}
	
	public boolean fileLoaded(){
		return loaded;
	}
	
	public Vector<Integer> getHeights(){
		return heights;
	}
}
