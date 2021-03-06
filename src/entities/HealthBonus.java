package entities;

/**
 * Class for generating and setting Health Bonuses
 * @author mpurita, gdelgiud, ppauli
 *
 */
public class HealthBonus extends Bonus{

    /**
         * Constructs a HealthBonus with the power desired.
         * @param power - Integer
         */
    public HealthBonus(int power){
        super(power);
    }
    
    /**
     * Heals the Player. The ammount is given by the bonus power.
     * @param player - Player
     */
    @Override
    protected void effect(Player player){
        player.heal(getPower());
    }

    public int getFileDescription() {
            return 4;
    }
}