package frontEnd;

import entities.Enemy;
import javax.swing.*;
import java.awt.*;
import entities.Player;

public class StatusPanel extends JPanel{
    private Player hero;
    
    private JLabel hp;
    private JLabel name;
    private JLabel exp;
    private JLabel strength;
    private JLabel level;

    private JLabel ehp;
    private JLabel ename;
    private JLabel estrength;
    private JLabel elevel;
    
    public StatusPanel(Player hero){
	setLayout(null);
	this.hero = hero;
	
	hp = new JLabel();
	name = new JLabel();
	exp = new JLabel();
	strength = new JLabel();
	level = new JLabel();

	ehp = new JLabel();
	ename = new JLabel();
	estrength = new JLabel();
	elevel = new JLabel();
	
	exp.setBounds(10,90,200,16);
	level.setBounds(10,70,200,16);
	strength.setBounds(10,50,200,16);
	hp.setBounds(10,30,200,16);
	name.setBounds(10,10,200,16);

	elevel.setBounds(10,180,200,16);
	estrength.setBounds(10,160,200,16);
	ehp.setBounds(10,140,200,16);
	ename.setBounds(10,120,200,16);
	
	add(name, BorderLayout.NORTH);
	add(hp, BorderLayout.NORTH);
	add(strength, BorderLayout.NORTH);
	add(level, BorderLayout.NORTH);
	add(exp, BorderLayout.NORTH);

        add(ename, BorderLayout.NORTH);
	add(ehp, BorderLayout.NORTH);
	add(estrength, BorderLayout.NORTH);
	add(elevel, BorderLayout.NORTH);
    }
    
    public void updateStats(){
	exp.setText("Exp: " + hero.getExp() + "/" + hero.getLevel() * 5);
	level.setText("Level: " + hero.getLevel());
	strength.setText("Strength: " + hero.getStrength());
	hp.setText("Health: " + hero.getHealth() + "/" + hero.getMaxHealth());
	name.setText(hero.getName());

        if(hero.getHealth() <= 2){
            hp.setForeground(Color.red);
        }
        else if(hero.getHealth() == hero.getMaxHealth()){
            hp.setForeground(Color.blue);
        }
        else{
            hp.setForeground(Color.black);
        }
    }
    
    public void showEnemy(Enemy enemy){
        if(enemy == null){
            elevel.setVisible(false);
            estrength.setVisible(false);
            ehp.setVisible(false);
            ename.setVisible(false);
        }
        else{
            elevel.setVisible(true);
            estrength.setVisible(true);
            ehp.setVisible(true);
            ename.setVisible(true);
            elevel.setText("Level: " + enemy.getLevel());
            estrength.setText("Strength: " + enemy.getStrength());
            ehp.setText("Health: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
            ename.setText(enemy.getName());

            if(enemy.getHealth() <= 2){
                ehp.setForeground(Color.red);
            }
            else if(enemy.getHealth() == enemy.getMaxHealth()){
                ehp.setForeground(Color.blue);
            }
            else{
                ehp.setForeground(Color.black);
            }
        }
	
    }
}
