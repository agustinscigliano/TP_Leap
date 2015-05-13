package Leap;


import com.leapmotion.leap.*;

import frontEnd.GamePlay;

public class LeapaintListener extends Listener
{
	//Leapaint instance.
	public GamePlay gamePlay;
	//Controller data frame.
	public Frame frame;
	//Leap interaction box.
	private InteractionBox normalizedBox;
	//Constructor.

	
	public LeapaintListener(GamePlay newPaint)
	{
		//Assign the Leapaint instance.
		gamePlay = newPaint;
	}
	
	//Member Function: onInit
	public void onInit(Controller controller)
	{
	System.out.println("Initialized");
	}
	
	//Member Function: onConnect
	public void onConnect(Controller controller)
	{
	System.out.println("Connected");
	}
	
	//Member Function: onDisconnect
	public void onDisconnect(Controller controller)
	{
	System.out.println("Disconnected");
	}
	
	//Member Function: onExit
	public void onExit(Controller controller)
	{
	System.out.println("Exited");
	}
	
	public void onFrame(Controller controller) { 
		
		//Get the most recent frame.
		frame = controller.frame();
		//Detect if fingers are present.
		if (!frame.fingers().isEmpty())
		{
			//Retrieve the front-most finger.
			Finger frontMost = frame.fingers().frontmost();
			//Set up its position.
			Vector position = new Vector(-1, -1, -1);
			//Retrieve an interaction box so we can normalize the Leap's coordinates to match screen size.
			normalizedBox = frame.interactionBox();
			//Retrieve normalized finger coordinates.
			position.setX(normalizedBox.normalizePoint(frontMost.tipPosition()).getX());
			position.setY(normalizedBox.normalizePoint(frontMost.tipPosition()).getY());
			position.setZ(normalizedBox.normalizePoint(frontMost.tipPosition()).getZ());
			
			//Scale coordinates to the resolution of the painter window.
			position.setX(position.getX() * gamePlay.getBounds().width);
			position.setY(position.getY() * gamePlay.getBounds().height);
			
			//Flip Y axis so that up is actually up, and not down.
			position.setY(position.getY() * -1);
			position.setY(position.getY() + gamePlay.getBounds().height);
			
			System.out.println(frontMost.tipPosition().getX());
			
			//Pass the X/Y coordinates to the painter.
			gamePlay.prevX = gamePlay.x;
			gamePlay.prevY = gamePlay.y;
			gamePlay.x = (int) position.getX();
			gamePlay.y = (int) position.getY();
			gamePlay.z = position.getZ();
			
			//Tell the painter to update.
			//gamePlay.gb.draw();
		
	}
	
}}
