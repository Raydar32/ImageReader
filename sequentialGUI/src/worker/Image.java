package worker;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Image implements Drawable {

	private String percorso;
	private ImageIcon icon;
	
	public Image() {	//Costruttore per definire oggetto vuoto
		
		this.percorso = "mock";
	}
	
	
	public Image(String percorso) {
		this.percorso = percorso;
		this.icon = new ImageIcon(percorso);
	}

	public String getPercorso() {
		return percorso;
	}

	@Override
	public String toString() {
		return this.percorso;
	}

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

}
