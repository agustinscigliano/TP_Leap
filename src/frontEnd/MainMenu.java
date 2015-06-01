package frontEnd;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import leap.LeapaintListener;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture;

import entities.Directories;

public class MainMenu extends JFrame {

	private String path;

	public MainMenu() {
		setTitle("DesktopDungeons");
		setBounds(1, 1, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		BackGround back = new BackGround();

		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);

		JButton nuevo = newGameButton();
		JButton cargar = loadGameButton();
		JButton salir = exitGameButton();

		back.add(nuevo);
		back.add(cargar);
		back.add(salir);
		add(back);
		setVisible(true);

		LeapaintListener listener = new LeapaintListener(back);
		Controller controller = new Controller();
		// Start the listener.
		controller.addListener(listener);
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);

		nuevo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				switch (e.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					setVisible(false);
					try {
						MapSelection selectedMap = new MapSelection(
								Directories.boards);
						if (selectedMap.getInput() != null) {
							NameMenu nameMenu = new NameMenu();
							if (nameMenu.getName() != null) {
								path = selectedMap.getPath(selectedMap
										.getInput());
								new GamePlay(path, nameMenu.getName(), null);
								dispose();
							} else {
								new MainMenu();
							}

						} else {
							new MainMenu();
						}

					} catch (Exception ex) {
						JOptionPane
								.showMessageDialog(
										null,
										"Ocurrio un error al cargar el mapa. "
												+ "No se pudieron leer los datos del jugador.",
										"Error", JOptionPane.ERROR_MESSAGE);

					}
					dispose();
				}
			}
		});

		cargar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switch (e.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					try {
						GameSelection selectedGame = new GameSelection(
								Directories.savegames);
						if (selectedGame.getInput() != null) {
							path = selectedGame.getPath(selectedGame.getInput());
							new GamePlay(selectedGame.getBoardPath(), "ASD",
									path);
							dispose();
						} else {
							new MainMenu();
						}

					} catch (Exception ex) {
						JOptionPane
								.showMessageDialog(
										null,
										"Ocurrio un error al cargar la partida guardada.",
										"Error", JOptionPane.ERROR_MESSAGE);

					}
				}
			}
		});

		salir.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				switch (e.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					System.exit(0);
				}

			}
		});

	
	}
	
	private static JButton newGameButton(){
		JButton newButton = new JButton("Nuevo Juego");
		newButton.setBounds(20, 150, 130, 25);
		newButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return newButton;
	}
	
	private static JButton loadGameButton() {
		JButton loadButton = new JButton("Cargar Juego");
		loadButton.setBounds(20, 190, 130, 25);
		loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return loadButton;
	}
	
	private static JButton exitGameButton() {
		JButton exit = new JButton("Salir");
		exit.setBounds(20, 230, 130, 25);
		exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return exit;
	}

}