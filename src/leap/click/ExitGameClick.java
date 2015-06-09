package leap.click;

import frontEnd.MainMenu;

public class ExitGameClick implements Click {

	@SuppressWarnings("unused")
	private MainMenu menu;

	public ExitGameClick(final MainMenu menu) {
		this.menu = menu;
	}

	public void doClick() {
		System.exit(0);
	}

}
