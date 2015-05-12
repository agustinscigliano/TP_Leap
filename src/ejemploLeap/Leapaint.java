package ejemploLeap;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.leapmotion.leap.Controller;

public class Leapaint extends JFrame
{
	//Static reference to this class.
	private static Leapaint paint;
	//X, Y and Z coordinates of the user's finger. These are set via the LeapaintListener class.
	public int prevX = -1, prevY = -1;
	public int x = -1, y = -1;
	public double z = -1;
	//Current drawing color.
	public Color inkColor = Color.MAGENTA;
	
	//Line data structure used to keep track of the lines we'll bedrawing.
	public class Line
	{
		public int startX, startY, endX, endY;
		public Color color;
		Line(int startX, int startY, int endX, int endY, Color color)
		{
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
			this.color = color;
		}
	}
	//Lines drawn. We need to keep track of these, or they'll be lost every time the screen refreshes.
		
	public List<Line> lines = new ArrayList<Line>();
	//Leap-enabled buttons.
	public LeapButton button1, button2, button3, button4;
	//Panels that we'll be drawing on.
	public JPanel buttonPanel;
	public JPanel paintPanel;
	
	Leapaint() { 
		//Always call the superclass constructor when overriding Java Swing classes.
		super("Leapaint - Place a finger in view to draw!");
		//Configure the button panel.
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBackground(new Color(215, 215, 215));
		//Configure the buttons.
		button1 = new LeapButton("Red", 1.5);
		button1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			inkColor = Color.RED;
			}
			});

		button2 = new LeapButton("Blue", 1.5);
		button2.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
			{
			inkColor = Color.BLUE;
			}
			});
		button3 = new LeapButton("Purple", 1.5);
		button3.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
			{
			inkColor = Color.MAGENTA;
			}
			});
		button4 = new LeapButton("Save", 1.5);
		button4.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
			{
			saveImage("leapaint");
			}
			});
		//Add the buttons to the button panel.
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		//Put a space between the color and save buttons.
		buttonPanel.add(Box.createVerticalStrut(1));
		buttonPanel.add(button4);
		//Configure the paint panel.
		paintPanel = new JPanel()
		{
		public void paintComponent(Graphics g)
		{
		super.paintComponent(g);
		//Setup the graphics.
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		
		//Only start drawing if the user's finger is in view and not on the button panel.
		if (z <= 0.5)
			lines.add(new Line(prevX, prevY, x, y, inkColor));
		//Draw all registered lines.
		for (Line line : lines)
			{
			g2.setColor(line.color);
			g2.drawLine(line.startX, line.startY, line.endX, line.endY);
			}
			//Repaint all the buttons.
		buttonPanel.repaint();
		
		//Draw the cursor if a finger is within in view.
		if (z <= 0.95 && z != -1.0)
		{
			//Set the cursor color to the inkColor if painting, and 		green otherwise.
			g2.setColor((z <= 0.5) ? inkColor : new Color(0, 255, 153));
			//Calculate cursor size based on depth for better feedback.
			int cursorSize = (int) Math.max(20, 100 - z * 100);
			//Create the cursor.
			g2.fillOval(x, y, cursorSize, cursorSize);
			}
		}
		};
		
		//Make sure the paint panel doesn't obscure any other elements.
		paintPanel.setOpaque(false);
		//Add the panels to the primary frame.
		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(paintPanel);
		//Make sure the application exits on close.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set initial frame size and become visible.
		setSize(800, 800);
		setVisible(true);
	}
	
	public void saveImage(String imageName) { 
		
		//Get the location and bounds of this JFrame.
		Point pos = getContentPane().getLocationOnScreen();
		Rectangle screenRect = getContentPane().getBounds();
		screenRect.x = pos.x;
		screenRect.y = pos.y;
		//Attempt to take a screen capture and pipe it to the image file.
		try
		{
		BufferedImage capture = new Robot().createScreenCapture(screenRect);
		ImageIO.write(capture, "bmp", new File(imageName + ".bmp"));
		}
		catch (Exception e) {}
		
	}
	
	//Member Function: main
	public static void main(String args[])
	{
		//Create a new instance of the Leapaint class.
		paint = new Leapaint();
		//Create a new listener and controller for the Leap Motion device.
		LeapaintListener listener = new LeapaintListener(paint);
		Controller controller = new Controller();
		//Start the listener.
		controller.addListener(listener);
		while (true) {}
	}
	
}