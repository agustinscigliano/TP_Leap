/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities.saveAndLoad;

import entities.Board;
import entities.Enemy;
import entities.InvalidLevelException;
import entities.OutOfBoardException;
import entities.Player;
import entities.Vector2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import parser.CellIsNotEmpty;
import parser.ErrorReadingBonus;
import parser.ErrorReadingEnemy;
import parser.ErrorReadingPlayer;
import parser.ErrorReadingWall;
import parser.FileException;

/**
 *
 * @author mpurita
 */
public class Load {
    String line = null;
    BufferedReader inStream;

    public Load(String inName) throws FileNotFoundException, IOException {
                inStream = new BufferedReader(new FileReader(inName));
        }

    public void loadMap(Board board) throws IOException, FileException,
            CellIsNotEmpty, ErrorReadingPlayer, ErrorReadingWall, OutOfBoardException,
            ErrorReadingBonus, InvalidLevelException, ErrorReadingEnemy {
        int i = 0;
        try{
            skipBoardPath();
            while (i < board.getRow() && line != null) {
                loadRevealedCell(board, i);
               i++;
            }
            while (line != null) {
                 String[] argToCheck = loadParameters(11);
                 if(argToCheck != null) {
                     Integer[] intToCheck = convertStringVecToIntVec(argToCheck);
                     switch(intToCheck[0]) {
                      case 1: loadPlayer(board, intToCheck);
                              break;
                      case 2: loadWall(board, intToCheck);
                              break;
                      case 3: loadEnemy(board, intToCheck);
                              break;
                      case 4: loadHealthBonus(board, intToCheck);
                              break;
                      case 5: loadStrengthBonus(board, intToCheck);
                              break;
                      }
                     }
          }
       }
       finally {
                        if(inStream != null) {
                                inStream.close();
                        }
                }
            
     }

    private void skipBoardPath() throws IOException {
        line = inStream.readLine();
    }

    private void loadPlayer(Board board, Integer[] intToCheck) 
            throws FileException, CellIsNotEmpty, ErrorReadingPlayer, OutOfBoardException {
        int x = intToCheck[1];
        int y = intToCheck[2];
        int heroLv = intToCheck[3];
        int heroExp = intToCheck[4];
        int heroHealth = intToCheck[5];
        int res = 0;
        Player hero = board.getPlayer();
        for(int i = 6; i < intToCheck.length; i++) {
            res += intToCheck[i];
        }
        if(res != 0) {
            throw new ErrorReadingPlayer("Invalid player parameters");
        }

        checkPlayerVector2D(x, y, board);
        checkHeroAttributes(board, x, y, heroLv, heroExp, heroHealth);
        hero.setExp(heroExp);
        hero.damage(hero.getMaxHealth() - heroHealth);
        hero.move(x, y);
    }

    private void checkHeroAttributes(Board board, int x, int y, int heroLv,
            int heroExp, int heroHealth) throws ErrorReadingPlayer {
        Player hero = board.getPlayer();
        hero.setLevel(heroLv);
        if(heroExp > hero.getNextLevelExp() || heroExp < 0) {
            throw new ErrorReadingPlayer("Error reading player experience");
        }
        if(heroHealth <= 0 || heroHealth > hero.getMaxHealth() ) {
            throw new ErrorReadingPlayer("Error reading player level");
        }
    }

    private Integer[] convertStringVecToIntVec(String[] argToCheck) {
        Integer[] intToCheck = new Integer[argToCheck.length];

        for(int i = 0; i < argToCheck.length; i++) {
            intToCheck[i] = Integer.parseInt(argToCheck[i].trim());
        }

        return intToCheck;
        
    }

    private String[] loadParameters(int size) throws IOException, FileException{
        String[] argToCheck;

        line = inStream.readLine();
        if(line == null) {
            return null;
        }
        else if(line.trim().compareTo("") == 0) {
            return loadParameters(size);
        }
        else {
            argToCheck = line.split(",");
            for(int i = 0; i < argToCheck.length; i++) {
                argToCheck[i] = argToCheck[i].trim();
            }
            if(argToCheck.length != size) {
              throw new FileException("Invalid file");
            }
            return argToCheck;
       }

        

    }

    private void loadWall(Board board, Integer[] intToCheck) throws FileException,
            CellIsNotEmpty, ErrorReadingWall, OutOfBoardException {
        int x = intToCheck[1];
        int y = intToCheck[2];
        int res = 0;
        for(int i = 3; i < intToCheck.length; i++) {
            res += intToCheck[i];
        }
        if(res != 0) {
            throw new ErrorReadingWall("Error reading wall parameters");
        }
        Vector2D wallPos = new Vector2D(x, y);
        if(!board.allWallsKeys().contains(wallPos)) {
            throw new ErrorReadingWall("Error reading wall Vector2D");
        }
    }

    private void loadHealthBonus(Board board, Integer[] intToCheck) throws FileException,
            CellIsNotEmpty, OutOfBoardException, ErrorReadingBonus {
        loadBonus(board, intToCheck);
    }

    private void checkBonusParameters(Integer[] intToCheck, int value, int picked_up) throws FileException {
        int res = 0;

        for(int i = 3; i < intToCheck.length - 2; i++) {
            res += intToCheck[i];
        }
        if(res != 0 || value < 0 || picked_up > 1 || picked_up < 0) {
            throw new FileException("Error reading health bonus");
        }
    }

    private void loadEnemy(Board board, Integer[] intToCheck) throws FileException,
            OutOfBoardException, InvalidLevelException, ErrorReadingEnemy{
        int x = intToCheck[1];
        int y = intToCheck[2];
        int enemyType = intToCheck[6];
        int enemyLv = intToCheck[7];
        int enemyHealth = intToCheck[8];
        int res = intToCheck[3] + intToCheck[4] + intToCheck[5] + intToCheck[9]
                + intToCheck[10];
        Vector2D enemyPos = new Vector2D(x, y);
        if(res != 0) {
            System.out.println("");
            throw new FileException("Erro reading enemy");
        }
        if(enemyType < 1 || enemyType > 3) {
            throw new FileException("Error reading enemy type");
        }
        if(!board.allEnemiesKeys().contains(enemyPos)) {
            throw new ErrorReadingEnemy("Error reading enemy Vector2D");
        }
        checkEnemyParameters(board.getEnemy(enemyPos), enemyHealth);

        
 }

    private void checkEnemyParameters(Enemy enemy, int enemyHealth)
            throws FileException {
        
        if(enemyHealth > enemy.getMaxHealth()) {
            throw new FileException("Error reading enemy health");
        }
        enemy.damage(enemy.getMaxHealth() - enemyHealth);
    }

    private void loadBonus(Board board, Integer[] intToCheck) throws FileException,
            CellIsNotEmpty, OutOfBoardException, ErrorReadingBonus {
        int x = intToCheck[1];
        int y = intToCheck[2];
        int value = intToCheck[9];
        int picked_up = intToCheck[10];
        checkBonusParameters(intToCheck, value, picked_up);
        Vector2D bonusPos = new Vector2D(x, y);
        if(!board.allBonusKeys().contains(bonusPos)) {
            throw new ErrorReadingBonus("Error reading bonus Vector2D");
        }
        if(picked_up == 1) {
            board.getBonus(bonusPos).pickUp(null);
        }
    }

    private void loadStrengthBonus(Board board, Integer[] intToCheck) throws FileException,
            CellIsNotEmpty, OutOfBoardException, ErrorReadingBonus {
        loadBonus(board, intToCheck);
    }

    private void loadRevealedCell(Board board, int row) throws IOException, FileException {
        String[] argToCheck = loadParameters(board.getCol() + 1);
        for(int i = 0; i < argToCheck.length - 1; i++) {
            if(argToCheck[i].compareTo("true") == 0) {
                board.revealCell(new Vector2D(row, i));
            }
        }
        
    }

    private void checkPlayerVector2D(int x, int y, Board board) throws FileException, CellIsNotEmpty {
                if(x < 0 || x > board.getRow())
                        throw new FileException("Invalid file");
                if(y < 0 ||  y > board.getCol())
                        throw new FileException("Invalid file");
                checkEmptyCell(x, y, board);
        }

    private void checkEmptyCell(int x, int y, Board board) throws CellIsNotEmpty {
        Vector2D pos = new Vector2D(x, y);
                if(!board.emptyCell(pos)) {
            if(!isEnemy(board, pos) || !isBonus(board, pos)) {
                throw new CellIsNotEmpty("This cell is not empty");
            }
        }
        }

    private boolean isEnemy(Board board, Vector2D Vector2D) {
        Set<Vector2D> keys;

        keys = board.allEnemiesKeys();
        if(keys.contains(Vector2D)) {
            if(board.getEnemy(Vector2D).isDead()) {
                return true;
            }

        }
        return false;
    }

    private boolean isBonus(Board board, Vector2D Vector2D) {
        Set<Vector2D> keys;

        keys = board.allBonusKeys();
        if(keys.contains(Vector2D)) {
            if(board.getBonus(Vector2D).wasPicked()) {
                return true;
            }

        }
        return false;
    }


}