package entities;

/**
 * Contains the Snake specifications.
 * @author mpurita, gdelgiud, ppauli
 *
 */
public class Snake extends Enemy{

    /**
	 * Constructs a Snake and sets its attributes according to its level.
	 * @param level - Integer
	 */
    public Snake(int level) throws InvalidLevelException{
        super(level);
        setName("Snake");
        setAttributeConstants(1.0, 1.0);
        reset();
    }
    
    @Override
    public int getFileDescription() {
            return super.getFileDescription();
    }

    public int getEnemyType() {
            return 3;
    }
    public int getLevel(){
    	return this.level;
    }
}
