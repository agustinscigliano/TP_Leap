package parser;
import entities.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TestPlayer {
    Player player;
    StrengthBonus sb;
    HealthBonus hb;
    Dragon dragon;
    Golem golem;
    Snake snake;

    @Before
    public void setUp() {
        try{
            player = new Player(3,3);
            sb = new StrengthBonus(3);
            hb = new HealthBonus(5);
            player.setLevel(2);
            player.heal(30);
            dragon = new Dragon(3);
            golem = new Golem(2);
            snake = new Snake(1);
        }
        catch(Exception e){

        }
    }

    @Test
    public void pickUpStrengthBonus(){
        player.pickUp(sb);
        assertEquals(player.getStrength(), 13);
    }

    @Test
    public void pickUpHealthBonus(){
        player.damage(13);
        player.pickUp(hb);
        assertEquals(player.getHealth(), 12);
    }

    @Test
    public void pickUpHealthBonusGetsCapped(){
        player.damage(3);
        player.pickUp(hb);
        assertEquals(player.getHealth(), 20);
    }

    @Test
    public void pickUpHealthBonusWhileDead(){
        player.kill();
        player.pickUp(hb);
        assertEquals(player.getHealth(), 0);
    }

    @Test
    public void levelUpWithExtraExperience(){
        player.earnExperience(13);
        assertEquals(player.getExp(), 3);
    }

    @Test
    public void experienceCap(){
        player.earnExperience(999);
        assertEquals(player.getExp(),15);
    }

    @Test
    public void damageOverkill(){
        player.damage(99999);
        assertEquals(player.getHealth(),0);
    }

    @Test
    public void playerDies(){
        player.damage(99999);
        assertTrue(player.isDead());
    }

}