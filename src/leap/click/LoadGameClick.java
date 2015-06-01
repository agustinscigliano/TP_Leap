package leap.click;

import javax.swing.JOptionPane;

import entities.Directories;
import frontEnd.GamePlay;
import frontEnd.GameSelection;
import frontEnd.MainMenu;

public class LoadGameClick implements Click {

	private MainMenu menu;

	public LoadGameClick(final MainMenu menu) {
		this.menu = menu;
	}

	@Override
	public void doClick() {
		try {
			GameSelection selectedGame = new GameSelection(
					Directories.savegames);
			if (selectedGame.getInput() != null) {
				final String path = selectedGame.getPath(selectedGame.getInput());
				new GamePlay(selectedGame.getBoardPath(), "ASD", path);
				menu.dispose();
			} else {
				new MainMenu();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Ocurrio un error al cargar la partida guardada.", "Error",
					JOptionPane.ERROR_MESSAGE);

		}
	}

}
