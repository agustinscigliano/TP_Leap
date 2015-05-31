package frontEnd;

import entities.Directories;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class BackGround extends JPanel {

	private static final Image image = Toolkit.getDefaultToolkit().getImage(
			Directories.resources + "dungeonDoor.png");
	
	/**
	 * Construct a BackGround
	 */
	public BackGround() {
		setOpaque(false);
		setLayout(null);
	}

	/**
	 * Draws the BackGround Image, taken from the resources folder
	 */
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, getSize().width, getSize().height, this);
		super.paint(g);
	}

}