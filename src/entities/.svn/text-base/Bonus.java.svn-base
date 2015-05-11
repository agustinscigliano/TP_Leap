package entities;
/**
 * Contains the bonuses stats and methods to set those stats.
 * @author mpurita, gdelgiud, mpurita
 *
 */
public abstract class Bonus{

    private int power;
    private boolean picked_up;

    /**
     * Construct a Bonus with the power desired.
     * @param power - Integer
     */
    public Bonus(int power){
        this.power = power;
        picked_up = false;
    }

    /**
     * @return True if the bonus was picked up. Otherwise, returns false.
     */
    public boolean wasPicked(){
    	return picked_up;
    }

    /**
     * Bonus is picked up by the Player
     * @param player - Player
     */
    public void pickUp(Player player){
        if(player != null && !wasPicked()){
            effect(player);
        }
        picked_up = true;
    }

     /**
     * Bonus generates an effect. Depends on Bonus type.
     * @return player - Player
     */
    protected abstract void effect(Player player);
    
    public abstract int getFileDescription();

    /*
     * Sends the object to its initial state
     */
    public void reset() {
        picked_up = false;
    }

    public int getPower(){
        return this.power;
    }
}
