package leap.click;

import frontEnd.MainMenu;

public class ExitGameClick implements Click {

	private MainMenu menu;

	public ExitGameClick(final MainMenu menu) {
		this.menu = menu;
	}

	@Override
	public void doClick() {
		System.exit(0);
	}

}
