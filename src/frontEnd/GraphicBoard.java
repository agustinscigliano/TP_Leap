package frontEnd;

import java.awt.Color;
import java.awt.Image;

import entities.Board;
import entities.Directories;
import entities.Enemy;
import entities.Vector2D;
import javax.swing.JFrame;

/**
 * Draws the board and all board components
 *
 *
 * @author mpurita, gdelgiud, ppauli
 *
 */
public class GraphicBoard extends Board{
    
    private GamePanel gp;
    
    Image dragon[] = new Image[4];
    Image snake[] = new Image[4];
    Image golem[] = new Image[4];
    Image wall = null;
    Image blood = null;
    Image attackBoost = null;
    Image healthBoost = null;
    Image floor = null;
    Image hero[][] = new Image[4][4];
    
    Enemy active_enemy = null;

    /**
     * Constructs a GraphicBoard
     */
    public GraphicBoard(){
        super();
        loadImages();
    }

    /**
     * Draws the Board
     */
    public void draw(){
        for(int i = 0; i < getCol(); i++){
            for( int j = 0; j < getRow(); j++){
                drawCell(new Vector2D(j,i));
            }
        }
        drawPlayer();
        gp.repaint();
    }
    
    @Override
    public void reset(){
        super.reset();
        draw();
    }

    /**
     * Draws the component located in that position of the Board
     * @param p - a Position of the Board
     */
    private void drawCell(Vector2D p){
        if( isRevealedCell(p)){
            if( getWall(p) != null){
                gp.put(wall,p.x(),p.y());
            }
            else if( getEnemy(p) != null){
                if( !getEnemy(p).isDead()){
                    switch(getEnemy(p).getEnemyType()) {
                        case 1: gp.put(golem[getEnemy(p).getLevel()], p.x(), p.y()); break;
                        case 2: gp.put(dragon[getEnemy(p).getLevel()], p.x(), p.y()); break;
                        case 3: gp.put(snake[getEnemy(p).getLevel()], p.x(), p.y()); break;
                    }
                }
                else{
                    gp.put(blood, p.x(), p.y());
                }
            }
            else if( getBonus(p) != null && !getBonus(p).wasPicked()) {
                String pow = new Integer(getBonus(p).getPower()).toString();
                if(getBonus(p).getFileDescription() == 4)
                    gp.put(ImageUtils.drawString(healthBoost,pow,Color.red),p.x(),p.y());
                else
                    gp.put(ImageUtils.drawString(attackBoost,pow,Color.red),p.x(),p.y());
            }
            else{
                gp.put(floor,p.x(),p.y());
            }
        }
        else{
            gp.clear(p.x(),p.y());
        }

    }

    /**
     * Generates a GamePanel according with the board size
     */
    @Override
    public void setDimension(Vector2D size){
        super.setDimension(size);
        gp = new GamePanel(size.x(), size.y(), 30, new BaseGamePanelListener(), Color.BLACK);
    }

    private void drawPlayer() {
        Image i = null;
        if(getPlayer().isDead()){
            i = blood;
        }
        else{
            i = hero[getPlayer().getLevel()][(getEnemy(getPlayer().getPosition()) == null)? 0:1];
        }
        gp.put(i, getPlayer().getPosition().x(), getPlayer().getPosition().y());
    }

    private void loadImages(){
        try{
            floor = ImageUtils.loadImage(Directories.resources + "background.png");
            wall = ImageUtils.loadImage(Directories.resources + "wall.png");
            dragon[0] = ImageUtils.loadImage(Directories.resources + "dragon.png");
            snake[0] = ImageUtils.loadImage(Directories.resources + "serpent.png");
            golem[0] = ImageUtils.loadImage(Directories.resources + "golem.png");
            healthBoost = ImageUtils.loadImage(Directories.resources + "healthBoost.png");
            attackBoost = ImageUtils.loadImage(Directories.resources + "attackBoost.png");
            hero[0][0] = ImageUtils.loadImage(Directories.resources + "hero.png");
            blood = ImageUtils.loadImage(Directories.resources + "blood.png");
        }
        catch(Exception e){
        }
        
        prepareImages();
    }


    private void prepareImages(){
        dragon[0] = ImageUtils.overlap(floor, dragon[0]);
        snake[0] = ImageUtils.overlap(floor, snake[0]);
        golem[0] = ImageUtils.overlap(floor, golem[0]);
        healthBoost = ImageUtils.overlap(floor, healthBoost);
        attackBoost = ImageUtils.overlap(floor, attackBoost);
        blood = ImageUtils.overlap(floor, blood);
        hero[0][1] = ImageUtils.overlap(blood, hero[0][0]);
        hero[0][0] = ImageUtils.overlap(floor, hero[0][0]);
        
        for(int i = 1; i < 4; i++){
            dragon[i] = ImageUtils.drawString(dragon[0], new Integer(i).toString(), Color.yellow);
        }
        for(int i = 1; i < 4; i++){
            snake[i] = ImageUtils.drawString(snake[0], new Integer(i).toString(), Color.yellow);
        }
        for(int i = 1; i < 4; i++){
            golem[i] = ImageUtils.drawString(golem[0], new Integer(i).toString(), Color.yellow);
        }
        for(int i = 1; i < 4; i++){
            hero[i][0] = ImageUtils.drawString(hero[0][0], new Integer(i).toString(), Color.yellow);
            hero[i][1] = ImageUtils.drawString(hero[0][1], new Integer(i).toString(), Color.yellow);
        }
    }
    
    public Enemy activeEnemy(){
        return active_enemy;
    }
    
    /*Clase interna para el evento de mover el mouse por el panel*/
    private class BaseGamePanelListener implements GamePanelListener {
        
        public void onMouseMoved(int row, int column) {
            Vector2D p = new Vector2D(row,column);
            if(getEnemy(p) != null && isRevealedCell(p) && !getEnemy(p).isDead()){
                setActiveEnemy(getEnemy(p));
            }
        }
    }

    /**
     * Returns the width of the board in pixels
     */
    public int getWidth(){
        if(gp != null){
            return gp.getWidth();
        }
        else{
            return 0;
        }
    }

    /**
     * Returns the height of the board in pixels
     */
    public int getHeight(){
        if(gp != null){
            return gp.getHeight();
        }
        else{
            return 0;
        }
    }

    /**
     * Adds the GamePanel to the specified JFrame
     */
    public void addTo(JFrame frame){
        if(gp != null){
            frame.getContentPane().add(gp);
        }
    }
}