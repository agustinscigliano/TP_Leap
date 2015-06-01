package dungeon.desktop;

import leap.MotionProvider;
import frontEnd.MainMenu;

public class LeapMotionApp {
	
	public static void main(String[] args) throws Exception {
		MainMenu mainMenu = new MainMenu();
		new Thread(new MotionProvider());
	}
}
