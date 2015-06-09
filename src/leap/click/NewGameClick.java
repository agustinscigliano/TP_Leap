package leap.click;

import frontEnd.MainMenu;

public class NewGameClick implements Click{

	private MainMenu menu;
	
	public NewGameClick(final MainMenu menu) {
		this.menu = menu;
	}
	
	public void doClick() {
		this.menu.newGame();
	}

}
