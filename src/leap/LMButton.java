package leap;

import java.awt.Rectangle;

import javax.swing.JButton;

import leap.click.Click;

public class LMButton extends JButton {

	private Click click;

	public LMButton(final String string, final Click click) {
		super(string);
		this.click = click;
	}

	public void doClick() {
		this.click.doClick();
	}

	public Rectangle getBigBounds() {

		// Retrieve original bounds.
		Rectangle rect = getBounds();

		// Increase height and width of the button.
		rect.width = rect.width - 20;
		rect.height = rect.height - 20;

		// Reposition the button so that its central coordinates more or less
		// remain the same.
		rect.x = rect.x + 15;
		rect.y = rect.y + 15;

		// Return the new button size.
		return rect;

	}
}
