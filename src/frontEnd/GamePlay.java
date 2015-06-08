package frontEnd;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import leap.LeapMotion;
import parser.CellIsNotEmpty;
import parser.ErrorReadingBoard;
import parser.ErrorReadingBonus;
import parser.ErrorReadingEnemy;
import parser.ErrorReadingPlayer;
import parser.ErrorReadingWall;
import parser.FileException;
import parser.MissingHero;
import parser.Parser;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

import entities.Bonus;
import entities.Directories;
import entities.Enemy;
import entities.GameEvents;
import entities.InvalidLevelException;
import entities.OutOfBoardException;
import entities.Player;
import entities.saveAndLoad.Load;
import entities.saveAndLoad.Save;

/**
 * Contains all methods necessary for playing the game.
 * 
 * @author mpurita, gdelgiud, ppauli
 *
 */
public class GamePlay extends JFrame {

	public final GraphicBoard gb = new GraphicBoard();
	private StatusPanel sp;
	public int prevX = -1, prevY = -1;
	public int x = -1, y = -1;
	public double z = -1;
	private Listener listener;
	private static long now = new Date().getTime();
	
	/**
	 * Generates a new game panel and adds all events for interaction.
	 * 
	 * @param map
	 *            - String
	 * @param player_name
	 *            - String
	 * @param save
	 *            - String
	 * @throws MissingHero
	 */
	public GamePlay(String map, String player_name, String save)
			throws OutOfBoardException, MissingHero {
		/* Window parameters */
		setTitle("DesktopDungeons");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setLayout(null);
		createMenuBar();

		loadMap(map, save);
		gb.getPlayer().setName(player_name);
		setStatusPanel();
		centerScreen();
		refreshScreen();
		setGameEvents();
		handleInput();

		this.listener = new SwipeListener(this, this.gb);
		LeapMotion.getInstance().addLeapListener(this.listener);
	}

	@SuppressWarnings("unused")
	public void mainMenu() {
		MainMenu menuPrincipal = new MainMenu();
		dispose();
	}

	/**
	 * Starts a new game, using the same map
	 */
	public void restartGame() {
		gb.reset();
		refreshScreen();
	}

	/**
	 * Shows an input window, asking for the save game file name
	 */
	public void saveGame() {
		String name;
		name = (String) JOptionPane.showInputDialog(null, "Ingrese un nombre",
				"Guardar Partida", JOptionPane.OK_CANCEL_OPTION, new ImageIcon(
						Directories.resources + "save.png"), null, "<nombre>");
		if (name != null) {
			try {

				new Save(name, gb);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Ocurrio un error al "
						+ "cargar el mapa. No se pudieron leer los datos del "
						+ "jugador.", "Error", JOptionPane.ERROR_MESSAGE);
				mainMenu();
			}
		}
	}

	private void loadMap(String map, String save) throws MissingHero {
		int width_adj = 200;
		int height_adj = 47;
		// String extension = map.substring(map.lastIndexOf("."), map.length());

		try {
			Parser p = new Parser(map);
			p.checkFile(gb);

			if (save != null) {
				Load load = new Load(save);
				try {
					load.loadMap(gb);
				} catch (ErrorReadingBonus ex) {
					JOptionPane.showMessageDialog(this,
							"No se pudo cargar los bonus.", "Error",
							JOptionPane.ERROR_MESSAGE);
					mainMenu();
				} catch (InvalidLevelException ex) {
					JOptionPane.showMessageDialog(this,
							"Error al leer el nivel.", "Error",
							JOptionPane.ERROR_MESSAGE);
					mainMenu();
				} catch (ErrorReadingEnemy ex) {
					JOptionPane.showMessageDialog(this,
							"Error al leer el enemigo.", "Error",
							JOptionPane.ERROR_MESSAGE);
					mainMenu();
				}
			}
		} catch (InvalidLevelException ex) {
			JOptionPane.showMessageDialog(this, "Error al leer el nivel.",
					"Error", JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (ErrorReadingEnemy ex) {
			JOptionPane.showMessageDialog(this, "Error al leer el enemigo",
					"Error", JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (ErrorReadingBoard ex) {
			JOptionPane.showMessageDialog(this, "Error al leer board.",
					"Error", JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"No existe el archivo especificado.", "Error",
					JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (CellIsNotEmpty e) {
			JOptionPane.showMessageDialog(this,
					"Ocurri� un error al cargar el mapa. "
							+ "Dos objetos se superponen.", "Error",
					JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (ErrorReadingPlayer e) {

			JOptionPane.showMessageDialog(this,
					"Ocurri� un error al cargar el mapa. "
							+ "Datos de un personaje invalidos.", "Error",
					JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (ErrorReadingWall e) {
			JOptionPane.showMessageDialog(this,
					"Ocurri� un error al cargar el mapa. "
							+ "Datos de una pared invalidos.", "Error",
					JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (OutOfBoardException e) {
			JOptionPane.showMessageDialog(this,
					"Ocurri� un error al cargar el mapa. "
							+ "Un objeto se encuentra fuera del mapa.",
					"Error", JOptionPane.ERROR_MESSAGE);
			mainMenu();
		} catch (FileException e) {
			JOptionPane.showMessageDialog(this,
					"Ocurri� un error al cargar el mapa.", "Error",
					JOptionPane.ERROR_MESSAGE);
			mainMenu();
		}

		setSize(gb.getWidth() + width_adj, gb.getHeight() + height_adj);
		gb.addTo(this);
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Game");
		menuBar.add(menu);

		JMenuItem restart = new JMenuItem("Restart Dungeon");
		JMenuItem gotoMenu = new JMenuItem("Abandon Dungeon");
		JMenuItem save = new JMenuItem("Save game");
		JMenuItem quit = new JMenuItem("Quit");
		gotoMenu.addActionListener(null);
		menu.add(restart);
		menu.add(save);
		menu.add(gotoMenu);
		menu.add(quit);

		restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		gotoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.CTRL_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));

		// Set menu items actions
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveGame();
			}
		});

		gotoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainMenu();
				dispose();
			}
		});

		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gb.reset();
				sp.updateStats();
			}
		});

		setJMenuBar(menuBar);
	}

	private void setGameEvents() {
		gb.addEventHandler(new GameEvents() {
			@Override
			public void onPlayerDeath(Player p) {
				Sound.play(Directories.resources + "Death02.wav");
				lose();
			}

			@Override
			public void onEnemyDeath(Enemy e) {
				sp.showEnemy(null);
				if (e.getLevel() == 3) {
					Sound.play(Directories.resources + "Death01.wav");
					win();
				} else {
					Sound.play(Directories.resources + "Death02.wav");
				}
			}

			@Override
			public void onBonusPickUp(Bonus b) {
				Sound.play(Directories.resources + "Bonus.wav");
			}

			@Override
			public void onEnemyFight(Enemy e) {
				Sound.play(Directories.resources + "Hit.wav");
			}

			@Override
			public void onChangeEnemy(Enemy e) {
				sp.showEnemy(e);
			}

			@Override
			public void onLevelUp(int new_level) {
				Sound.play(Directories.resources + "LevelUp.wav");
			}
		});
	}

	private void setStatusPanel() {
		sp = new StatusPanel(gb.getPlayer());
		sp.setBounds(gb.getWidth(), 0, 200, gb.getHeight());
		add(sp);
	}

	private void centerScreen() {
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(300, 300);
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);
	}

	// Aca hay que agregar los gestos
	private void handleInput() {

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					gb.movePlayerWest();
					break;
				case KeyEvent.VK_RIGHT:
					gb.movePlayerEast();
					break;
				case KeyEvent.VK_UP:
					gb.movePlayerNorth();
					break;
				case KeyEvent.VK_DOWN:
					gb.movePlayerSouth();
					break;
				}

				refreshScreen();
			}
		});
	}

	private void lose() {
		refreshScreen();
		Sound.play(Directories.resources + "Lose.wav");
		JOptionPane.showMessageDialog(this, "���GAME OVER!!!", "Lo siento...",
				JOptionPane.PLAIN_MESSAGE);

		mainMenu();
	}

	private void win() {
		refreshScreen();
		Sound.play(Directories.resources + "Win.wav");
		JOptionPane.showMessageDialog(this, "���GANASTE!!!", "Felicidades",
				JOptionPane.PLAIN_MESSAGE);
		mainMenu();
	}

	public void refreshScreen() {
		gb.draw();
		sp.updateStats();
	}

	private static class SwipeListener extends Listener {

		private final GamePlay gp;
		private final GraphicBoard gb;

		public SwipeListener(final GamePlay gp, final GraphicBoard gb) {
			this.gp = gp;
			this.gb = gb;
		}

		// Controller data frame.
		public Frame frame;
		// Leap interaction box.
		private InteractionBox normalizedBox;

		public void onInit(Controller controller) {
			System.out.println("Initialized");
		}

		public void onConnect(Controller controller) {
			System.out.println("Connected");
		    controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		}

		public void onDisconnect(Controller controller) {
			System.out.println("Disconnected");
		}

		public void onExit(Controller controller) {
			System.out.println("Exited");
		}

		public void onFrame(Controller controller) {
			frame = controller.frame();
			frame.gestures();

			if (!frame.gestures().isEmpty() && (now+1000<new Date().getTime())) {
				now = new Date().getTime();
				for (int i = 0; i < frame.gestures().count(); i++) {
					Gesture gesture = frame.gestures().get(i);
					if (gesture.type() == Gesture.Type.TYPE_SWIPE) {
						SwipeGesture swipeGesture = new SwipeGesture(gesture);
						// Classify swipe as either horizontal or vertical
						boolean isHorizontal = (Math.abs(swipeGesture
								.direction().getX()) > Math.abs(swipeGesture
								.direction().getY()));
						// Classify as right-left or up-down
						if (isHorizontal) {
							if (swipeGesture.direction().getX() > 0) {
								gb.movePlayerEast();
							} else {
								gb.movePlayerWest();
							}
						} else { // vertical
							if (swipeGesture.direction().getY() > 0) {
								gb.movePlayerNorth();
							} else {
								gb.movePlayerSouth();
							}
						}
					}
				}
				this.gp.refreshScreen();
			}
		}
	}
}