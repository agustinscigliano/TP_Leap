package entities;

/**
 * Class for generating and setting Strength Bonuses
 * @author mpurita, gdelgiud, ppauli
 *
 */
public class StrengthBonus extends Bonus{

    /**
	 * Constructs a StrengthBonus with the power desired.
	 * @param power - Integer
	 */
    public StrengthBonus(int power){
        super(power);
    }

    /**
     * Increase Player strenght
     * @return player - Player
     */
    @Override
    protected void effect(Player player){
        player.toughen(getPower());
    }

    
    public int getFileDescription() {
        return 5;
    }
}

