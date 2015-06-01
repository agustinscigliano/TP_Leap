package leap;

import com.leapmotion.leap.Config;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

public enum LeapMotion {
	
	INSTANCE;
	private Controller controller;
    private LeapMotion() {
    	this.controller  = new Controller();
    	Config config = this.controller.config();
    	config.setFloat("Gesture.KeyTap.MinDownVelocity", 10);
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
