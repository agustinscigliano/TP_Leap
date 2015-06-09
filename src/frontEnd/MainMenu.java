package frontEnd;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import leap.LMButton;
import leap.LeapMotion;
import leap.click.ExitGameClick;
import leap.click.NewGameClick;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import entities.Directories;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {

	private String path;
	private Listener listener;
	private static int offset = 50;
	private LMButton newGame, exitGame;
	private BackGround back;
	
	public MainMenu() {
		setTitle("DesktopDungeons");
		setBounds(1, 1, 600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		back = new BackGround();

		newGame = newGameButton();
		exitGame = exitGameButton();

		back.add(newGame);
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
								path = selectedMap.getPath(selectedMap
										.getInput());
								LeapMotion.getInstance().removeLeapListener(listener);
								new GamePlay(path, "Player", null);
								dispose(); 
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

		exitGame.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				switch (e.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					System.exit(0);
				}

			}
		});


		this.listener = new MainMenuListener(this);
		LeapMotion.getInstance().addLeapListener(listener);
	}
	
	public void newGame() {
		setVisible(false);
		try {
			MapSelection selectedMap = new MapSelection(
					Directories.boards);
			if (selectedMap.getInput() != null) {
				
					path = selectedMap.getPath(selectedMap
							.getInput());
					LeapMotion.getInstance().removeLeapListener(this.listener);
					new GamePlay(path,"Player", null);
					dispose();
				}
			else {
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
		newButton.setBounds(30, 150, 180, 100);
		newButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return newButton;
	}
	
	private LMButton exitGameButton() {
		LMButton exit = new LMButton("Salir", new ExitGameClick(this));
		exit.setBounds(30, 350, 180, 100);
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
			controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
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

				
				for (Gesture g : frame.gestures()) {
					System.out.println(g.type());
					if (g.type().equals(Gesture.Type.TYPE_SCREEN_TAP)) {
						if (mainMenu.newGame.getBigBounds().contains((int) position.getX(),
								(int) position.getY())) {
							mainMenu.newGame.doClick();
						}
						if (mainMenu.exitGame.getBigBounds().contains((int) position.getX(),
								(int) position.getY())) {
							mainMenu.exitGame.doClick();
						}
					}
				}
				
				this.robot.mouseMove((int)position.getX(), (int)position.getY());	
			}
		}

	}
	
}