package frontEnd;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import leap.LMButton;
import leap.LeapMotion;
import leap.click.LoadGameClick;
import leap.click.NewGameClick;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import ejemploLeap.LeapButton;
import entities.Directories;

public class MainMenu extends JFrame {

	private String path;
	private Listener listener;
	private static int offset = 50;
	private LMButton newGame, loadGame, exitGame;
	private BackGround back;
	
	public MainMenu() {
		setTitle("DesktopDungeons");
		setBounds(1, 1, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		back = new BackGround();

		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
//		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
//				- getHeight() / 2);

		newGame = newGameButton();
		loadGame = loadGameButton();
		exitGame = exitGameButton();

		back.add(newGame);
		back.add(loadGame);
		back.add(exitGame);
		add(back);

		newGame.addMouseListener(new MouseAdapter() {

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

		loadGame.addMouseListener(new MouseAdapter() {
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

		exitGame.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				switch (e.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					System.exit(0);
				}

			}
		});

		// Fuck you garbage collector
		this.listener = new MainMenuListener(this);
		LeapMotion.getInstance().addLeapListener(listener);
	}
	
	public void newGame() {
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
	
	private LMButton newGameButton(){
		LMButton newButton = new LMButton("Nuevo Juego", new NewGameClick(this));
		newButton.setBounds(20, 50, 130, 25);
		newButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return newButton;
	}
	
	private LMButton loadGameButton() {
		LMButton loadButton = new LMButton("Cargar Juego", new LoadGameClick(this));
		loadButton.setBounds(20, 150, 130, 25);
		loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return loadButton;
	}
	
	private LMButton exitGameButton() {
		LMButton exit = new LMButton("Salir", new NewGameClick(this));
		exit.setBounds(20, 250, 130, 25);
		exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return exit;
	}

	
	public static class MainMenuListener extends Listener {

		private MainMenu mainMenu;
		private Frame frame;
		private InteractionBox normalizedBox;
		private Robot robot;
		
		public MainMenuListener(MainMenu mainMenu) {
			this.mainMenu = mainMenu;
		}

		public void onInit(Controller controller) {
			System.out.println("Initialized");
			try {
				this.robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}

		public void onConnect(Controller controller) {
			System.out.println("Connected");
		}

		public void onDisconnect(Controller controller) {
			System.out.println("Disconnected");
		}

		public void onExit(Controller controller) {
			System.out.println("Exited");
		}

		public void onFrame(Controller controller) {

			frame = controller.frame();
			if (!frame.fingers().isEmpty()) {
				Finger frontMost = frame.fingers().frontmost();
				Vector position = frontMost.stabilizedTipPosition();
				normalizedBox = frame.interactionBox();
				
				position.setX(normalizedBox.normalizePoint(frontMost.tipPosition())
						.getX());
				position.setY(normalizedBox.normalizePoint(frontMost.tipPosition())
						.getY());
				position.setZ(normalizedBox.normalizePoint(frontMost.tipPosition())
						.getZ());
				
				// Scale coordinates to the resolution of the painter window.
				position.setX( (position.getX() * mainMenu.getBounds().width));
				position.setY( (position.getY() * mainMenu.getBounds().height));

				// Flip Y axis so that up is actually up, and not down.
				position.setY(position.getY() * -1);
				position.setY(position.getY() + mainMenu.getBounds().height);

				this.robot.mouseMove((int)position.getX(), (int)position.getY());	
				
				int x = (int) position.getX();
				int y = (int) position.getY() - offset;
				System.out.println("[" + "X:" + x + " Y:" + y + "]");
				System.out.println("fingers: " + frame.fingers().count());
				if (mainMenu.newGame.getBigBounds().contains((int) position.getX(),
						(int) position.getY() - offset))
					mainMenu.newGame.doClick();

				if (mainMenu.loadGame.getBigBounds().contains((int) position.getX(),
						(int) position.getY() - offset))
					mainMenu.loadGame.doClick();

				if (mainMenu.exitGame.getBigBounds().contains((int) position.getX(),
						(int) position.getY() - offset))
					mainMenu.exitGame.doClick();
			}
		}

	}
	
}