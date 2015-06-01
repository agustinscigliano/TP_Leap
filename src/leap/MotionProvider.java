package leap;

public class MotionProvider implements Runnable{
	
	private static LeapMotion leap = LeapMotion.getInstance();
	
	@Override
	public void run() {
		while(true) {
			leap.update();
		}
	}

}
