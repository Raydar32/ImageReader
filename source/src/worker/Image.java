package worker;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//'---------------------------------------------------------------------------------------
//' Class     : Image
//' Author    : Mini Alessandro (7060381)
//' Purpose   : This class will define an Image item, all images starts "empty". the 
//'				emptiness is implemented with the "mock" image path, if the image path
//				is a real path then the image is real and loaded, otherwise it still 
//				has to be loaded or something has gone wrong.
//				an image implements the Drawable interface.
//'--------------------------------------------------------------------------------------- 



public class Image implements Drawable {
	private String percorso;
	private ImageIcon icon;
	
	// '---------------------------------------------------------------------------------------
	// ' Method  : Image
	// ' Purpose : Constructor of the image class, if no arguments it creates an empty image.
	// '---------------------------------------------------------------------------------------	
	public Image() {		
		this.percorso = "mock";
	}
	
	
	// '---------------------------------------------------------------------------------------
	// ' Method  : Image
	// ' Purpose : Override of costructor, if a valid path is specified, it creates(and loads
	//			   immediatly in memory) the image.
	// '---------------------------------------------------------------------------------------
	public Image(String percorso) {
		this.percorso = percorso;
		this.icon = new ImageIcon(percorso);
	}
	
	
	// '---------------------------------------------------------------------------------------
	// ' Method  : draw
	// ' Purpose : Method implemented from the Drawable interfaces, it describes how the image
	//			   will be displayed.
	// '---------------------------------------------------------------------------------------
	@Override
	public void draw() {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setSize(800,600);
		frame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setTitle(percorso);
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);

	}
	
	
	public String getPercorso() {
		return percorso;
	}

	
	@Override
	public String toString() {
		return this.percorso;
	}



}
