package dungeon.desktop;

import javax.swing.SwingUtilities;

import leap.LeapMotion;
import frontEnd.MainMenu;

public class LeapMotionApp {

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainMenu m = new MainMenu();
				m.setVisible(true);
			}
		});
		// Start the listener.
		LeapMotion.getInstance();
		while (true) {
		}
	}
}
