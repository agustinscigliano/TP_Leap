package leap;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

public enum LeapMotion {
	
	INSTANCE;
	private Controller controller;
    private LeapMotion() {
    	this.controller  = new Controller();
    };

    public static LeapMotion getInstance() {
    	return INSTANCE;
    }

    public void addLeapListener(Listener listener) {
    	controller.addListener(listener);
    }
    
    public void removeLeapListener(Listener listener) {
    	controller.removeListener(listener);
    }
    
}
