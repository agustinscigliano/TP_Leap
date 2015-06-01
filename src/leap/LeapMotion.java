package leap;

import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

public enum LeapMotion {
	
	INSTANCE;
    private LeapMotion() {};

    public static LeapMotion getInstance() {
    	return INSTANCE;
    }
    
    private Controller controller = new Controller();
    private List<LeapListener> listeners = new ArrayList<LeapListener>();
    private int lastId;

    public void addLeapListener(LeapListener listener) {
    	listeners.add(listener);
    }
    
    public void removeLeapListener(LeapListener listener) {
    	listeners.remove(listener);
    }
    
    public Frame getCurrentFrame() {
        return controller.frame(lastId-((int)controller.frame().id()));
    }

    public void update() {
        Frame frame = controller.frame();
        if(lastId != frame.id()) {
            lastId = (int) frame.id();
            for(LeapListener listener: listeners) {
            	listener.update(frame);
            }
        }
    }
}
