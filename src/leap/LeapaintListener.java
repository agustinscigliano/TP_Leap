package leap;

import javax.swing.JPanel;

import com.leapmotion.leap.*;

import frontEnd.BackGround;
import frontEnd.GamePlay;

public class LeapaintListener extends Listener {
	// Leapaint instance.
	public BackGround back;
	// Controller data frame.
	public Frame frame;
	// Leap interaction box.
	private InteractionBox normalizedBox;

	// Constructor.

	public LeapaintListener(BackGround back) {
		// Assign the Leapaint instance.
		this.back = back;
	}

	// Member Function: onInit
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	// Member Function: onConnect
	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	// Member Function: onDisconnect
	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}

	// Member Function: onExit
	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {

		// Get the most recent frame.
		frame = controller.frame();
		// Detect if fingers are present.
		// System.out.println("dasd");

		frame.gestures();

		if (!frame.gestures().isEmpty()) {
			for (int i = 0; i < frame.gestures().count(); i++) {
				Gesture gesture = frame.gestures().get(i);
				System.out.println(gesture.type());
				if (gesture.type() == Gesture.Type.TYPE_SWIPE) {
					SwipeGesture swipeGesture = new SwipeGesture(gesture);

					Vector swipeVector = swipeGesture.direction();
					System.out.println("swipeVector : " + swipeVector);

					float swipeDirection = swipeVector.getX();
					System.out.println(swipeDirection);

					// Classify swipe as either horizontal or vertical
					boolean isHorizontal = (Math.abs(swipeGesture.direction()
							.getX()) > Math
							.abs(swipeGesture.direction().getY()));
					// Classify as right-left or up-down
					if (isHorizontal) {
						if (swipeGesture.direction().getX() > 0) {
							System.out.println("right");
						} else {
							System.out.println("left");
						}
					} else { // vertical
						if (swipeGesture.direction().getY() > 0) {
							System.out.println("up");
						} else {
							System.out.println("down");
						}
					}
				}
			}
		}

		if (!frame.fingers().isEmpty()) {
			// Retrieve the front-most finger.
			Finger frontMost = frame.fingers().frontmost();
			// Set up its position.
			Vector position = new Vector(-1, -1, -1);
			// Retrieve an interaction box so we can normalize the Leap's
			// coordinates to match screen size.
			normalizedBox = frame.interactionBox();
			// Retrieve normalized finger coordinates.
			position.setX(normalizedBox.normalizePoint(frontMost.tipPosition())
					.getX());
			position.setY(normalizedBox.normalizePoint(frontMost.tipPosition())
					.getY());
			position.setZ(normalizedBox.normalizePoint(frontMost.tipPosition())
					.getZ());

			// Scale coordinates to the resolution of the painter window.
			position.setX(position.getX() * back.getBounds().width);
			position.setY(position.getY() * back.getBounds().height);

			// Flip Y axis so that up is actually up, and not down.
			position.setY(position.getY() * -1);
			position.setY(position.getY() + back.getBounds().height);

			// System.out.println(frontMost.tipPosition().getX());

			// Pass the X/Y coordinates to the painter.
			// gamePlay.prevX = gamePlay.x;
			// gamePlay.prevY = gamePlay.y;
			// gamePlay.x = (int) position.getX();
			// gamePlay.y = (int) position.getY();
			// gamePlay.z = position.getZ();

			// Tell the painter to update.
			// gamePlay.gb.draw();
			back.repaint();

		}

	}
}
