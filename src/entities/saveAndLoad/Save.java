package entities.saveAndLoad;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import entities.Board;
import entities.Directories;
import entities.Enemy;
import entities.Player;
import entities.Vector2D;
import java.io.File;


public class Save {
        private BufferedWriter outStream = null;
        
        public Save(String fileName, Board board) throws IOException {
                File file = new File ( Directories.savegames, fileName + ".save");
                outStream = new BufferedWriter(new FileWriter(file));
                writeOnFile(board);
        }
        
        private void writeOnFile(Board board) throws IOException {
                writeBoardPath(board);
                writeRevealedCell(board);
                writeWall(board);
                writeEnemy(board);
                writeBonus(board);
                writePlayer(board);
                
                
                outStream.close();
        }

        private void writeBoardPath(Board board) throws IOException {
            outStream.write(board.getBoardPath() + "\n");
        }
        private void writeBonus(Board board) throws IOException {
                String line = "";
                Set<Vector2D> keys;
                int bonusPower;
                int typeCell;
                keys = board.allBonusKeys();
                
                for (Vector2D object : keys) {
                        bonusPower = board.getBonus(object).getPower();
                        typeCell = board.getBonus(object).getFileDescription();
                        line += typeCell + "," + object.x() + "," + object.y() 
                        + ",0,0,0,0,0,0," + bonusPower + ",";
                        if(board.getBonus(object).wasPicked())
                                line += 1 + "\n";
                        else
                                line += 0 + "\n";
                        outStream.write(line);
                        line = "";
                }
        }
        
        private void writeWall(Board board) throws IOException {
                String line = "";
                Set<Vector2D> keys;
                int typeCell;
                int i = 0;
                keys = board.allWallsKeys();
 

                for (Vector2D object : keys) {
                        typeCell = board.getWall(object).getFileDescription();
                        line += typeCell + "," + object.x() + "," + object.y() 
                        + ",0,0,0,0,0,0,0,0" + "\n";
                        outStream.write(line);
                        line = "";
                }
        }
        
        private void writeEnemy(Board board) throws IOException {
                String line = "";
                Set<Vector2D> keys;
                Enemy element;
                int typeCell, enemyType, enemyLv, enemyHealth;
                int i = 0;
                keys = board.allEnemiesKeys();

                for (Vector2D object : keys) {
                        element = board.getEnemy( object);
                        typeCell = element.getFileDescription();
                        line += typeCell + "," + object.x() + "," + object.y()
                                + ",0,0,0," + element.getEnemyType() + "," +
                                element.getLevel() + "," + element.getHealth() + 
                                ",0,0" + "\n";
                        outStream.write(line);
                        line = "";
                }
        }
        
        private void writePlayer(Board board) throws IOException {
                String line = "";
                Player hero;
                hero = board.getPlayer();
                
                line += "1," + hero.getPosition().x() + "," + hero.getPosition().y()
                        + "," + hero.getLevel() + "," + hero.getExp() + "," + hero.getHealth() 
                        + ",0,0,0,0,0" + "\n";
                outStream.write(line);
        }

        private void writeRevealedCell(Board board) throws IOException {
            String line = "";
            for(int i = 0; i < board.getRow(); i++) {
                for(int j = 0; j < board.getCol(); j++) {
                    line += board.isRevealedCell(new Vector2D(i, j)) + ", ";
                }
                outStream.write(line  + "\n");
                line = "";
            }
        }

}