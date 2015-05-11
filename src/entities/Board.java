package entities;

import java.util.HashMap;
import java.util.Set;


public class Board {

    private HashMap<Vector2D, Enemy> enemyMap;
    private HashMap<Vector2D, Bonus> bonusMap;
    private HashMap<Vector2D, Wall> wallMap;
    private Vector2D dimension;
    private Player hero;
    private String[] nameAndPath = new String[2];
    private GameEvents events = null;
    private boolean cells[][];
    private boolean fought = false;

    public Board(){
        enemyMap = new HashMap<Vector2D, Enemy>();
        bonusMap = new HashMap<Vector2D, Bonus>();
        wallMap = new HashMap<Vector2D, Wall>();
    }

    public void setDimension(Vector2D size){
        if(dimension == null){
            if(size.x() > 30 || size.y() > 30 || size.x() < 6 || size.y() < 6){
                
            }
            this.dimension = size;
            cells = new boolean[dimension.y()][dimension.x()];
        }
    }
    
    public void reset(){
        for(Object obj: allEnemiesKeys()){
            Vector2D p = (Vector2D)obj;
            Enemy e = (Enemy)enemyMap.get(p);
            e.reset();
        }
        for(Object obj: allBonusKeys()){
            Vector2D p = (Vector2D)obj;
            Bonus b = (Bonus)bonusMap.get(p);
            b.reset();
        }

        cells = new boolean[dimension.y()][dimension.x()];
        if(getPlayer() != null){
            getPlayer().reset();
            revealSurroundingCells();
        }

        setActiveEnemy(null);
    }

    public Vector2D getDimension(){
        return dimension;
    }

    public int getRow() {
        return dimension.x();
    }

    public int getCol() {
        return dimension.y();
    }
    
    
    /*Entity management*/

    /**
     * Spawns an Enemy in the board.
     */
    public void add(Enemy value, Vector2D key) throws OutOfBoardException{
        if(validPosition(key)){
            throw new OutOfBoardException();
        }
        this.enemyMap.put(key, value);
    }

    /**
     * Spawns a Bonus in the board.
     */
    public void add(Bonus value, Vector2D key) throws OutOfBoardException{
        if(validPosition(key)){
            throw new OutOfBoardException();
        }
        this.bonusMap.put(key, value);
    }

    /**
     * Spawns a Wall in the board.
     */
    public void add(Wall value, Vector2D key) throws OutOfBoardException{
        if(validPosition(key)){
            throw new OutOfBoardException();
        }
        this.wallMap.put(key, value);
    }

    /**
     * Spawns the Player in the board.
     */
    public void add(Player hero) throws OutOfBoardException{
        if( hero != null){
            if(validPosition(hero.getPosition())){
                throw new OutOfBoardException();
            }
            this.hero = hero;
            revealSurroundingCells();
        }
    }

    private boolean validPosition(Vector2D p){
        return (p.x() < 0 || p.x() >= getRow() || p.y() < 0 || p.y() >= getCol());
    }

    public boolean emptyCell(Vector2D position) {
        if(!enemyMap.containsKey(position) && !bonusMap.containsKey(position) 
            && !wallMap.containsKey(position)) {
            return true;
        }
        return false;
    }

    public Enemy getEnemy(Vector2D key) {
        return (Enemy) this.enemyMap.get(key);
    }

    public Wall getWall(Vector2D key) {
        return (Wall) this.wallMap.get(key);
    }

    public Bonus getBonus(Vector2D key) {
        return (Bonus) this.bonusMap.get(key);
    }

    public Set<Vector2D> allEnemiesKeys() {
        return this.enemyMap.keySet();
    }

    public Set<Vector2D> allWallsKeys() {
        return this.wallMap.keySet();
    }

    public Set<Vector2D> allBonusKeys() {
        return this.bonusMap.keySet();
    }

    public Player getPlayer() {
        return hero;
    }
    
    
    /*Player movement*/
    
    public void movePlayerWest(){
        int y = getPlayer().getPosition().y() - 1;
        int x = getPlayer().getPosition().x();
        if(y >= 0)
        movePlayer(new Vector2D(x,y));
    }
    
    public void movePlayerEast(){
        int y = getPlayer().getPosition().y() + 1;
        int x = getPlayer().getPosition().x();
        if(y < getCol())
        movePlayer(new Vector2D(x,y));
    }
        
    public void movePlayerNorth(){
        int y = getPlayer().getPosition().y();
        int x = getPlayer().getPosition().x() - 1;
        if(x >= 0)
        movePlayer(new Vector2D(x,y));
    }
        
                
    public void movePlayerSouth(){
        int y = getPlayer().getPosition().y();
        int x = getPlayer().getPosition().x() + 1;
        if(x < getRow())
        movePlayer(new Vector2D(x,y));
    }
        
    private void movePlayer(Vector2D p){
        if(collidePlayer(p) && !getPlayer().isDead()){
            setActiveEnemy(null);
            getPlayer().move(p.x(),p.y());
            revealSurroundingCells();
        }
    }
        
    private boolean collidePlayer(Vector2D p){
        fought = false;
        /*Fight enemy?*/
        if(getEnemy(p) != null && !getEnemy(p).isDead()){
            int prev_lvl = getPlayer().getLevel();
            getPlayer().fight(getEnemy(p));
            if(events != null){
                events.onEnemyFight(getEnemy(p));
                setActiveEnemy(getEnemy(p));
                if(getPlayer().isDead()){
                    events.onPlayerDeath(getPlayer());
                }
                else if(getEnemy(p).isDead()){
                    events.onEnemyDeath(getEnemy(p));
                }
            }
            if(getPlayer().getLevel() != prev_lvl && events != null){
                events.onLevelUp(getPlayer().getLevel());
            }
            fought = true;
            return false;
        }
        /*Hit wall?*/
        else if(getWall(p) != null){
            return false;
        }
        /*Pick bonus?*/
        else if(getBonus(p) != null){
            if(!getBonus(p).wasPicked() && events != null){
                events.onBonusPickUp(getBonus(p));
            }
            getPlayer().pickUp(getBonus(p));
        }
        return true;
    }

    protected void setActiveEnemy(Enemy e){
        if(this.events != null){
            events.onChangeEnemy(e);
        }
    }

    public void addEventHandler(GameEvents events){
        this.events = events;
    }
    
    
    /*Cell operations*/

    public boolean isRevealedCell(Vector2D p){
        return cells[p.y()][p.x()];
    }

    /**
     * Reveals the specified cell.
     * @return true if the cell wasn't revealed before.
     */
    public boolean revealCell(Vector2D p){
        try{
            if(!isRevealedCell(p)){
                cells[p.y()][p.x()] = true;
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){

        }
        return false;
    }

    /**
     * Reveals the cells surrounding the player
     */
    private void revealSurroundingCells(){
        Vector2D pl = getPlayer().getPosition();
        Vector2D pos = null;
        int revealed_cells = 0;
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                //Reveal the current cell
                pos = new Vector2D(pl.x() + i, pl.y() + j);
                if(revealCell(pos)){
                    revealed_cells++;
                }
                //Set the current enemy if there wasn't a fight
                if(getEnemy(pos) != null && fought == false && !getEnemy(pos).isDead()){
                    setActiveEnemy(getEnemy(pos));
                }
            }
        }

        //Heal characters
        getPlayer().heal(revealed_cells * getPlayer().getLevel());
        for (Object obj : allEnemiesKeys()){
            Enemy e = (Enemy)enemyMap.get((Vector2D)obj);
            if(!e.isDead())
            e.heal(revealed_cells * e.getLevel());
        }
    }
    public void setNameAndPath(String name, String path) {
        this.nameAndPath[0] = name;
        this.nameAndPath[1] = path;
    }

    public String getBoardPath() {
        return  this.nameAndPath[1];
    }
}