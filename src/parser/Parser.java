package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import entities.*;
import java.io.File;


public class Parser{

        private BufferedReader inStream = null;
        private String line;
    private String path;
        private final int PLAYER = 1;
        private final int WALL = 2;
        private final int ENEMY = 3;
        private final int BHEALTH = 4;
        private final int BSTRENGTH = 5;
        
        public Parser(String inName) throws FileNotFoundException, IOException {
        File file = new File(inName);
        path = file.getCanonicalPath();
        inStream = new BufferedReader(new FileReader(file));
        }
        
        public void checkFile(Board board) throws IOException, FileException, 
                CellIsNotEmpty, ErrorReadingWall, ErrorReadingPlayer, OutOfBoardException,
        InvalidLevelException, ErrorReadingEnemy, ErrorReadingBoard, MissingHero {

                try {
                        if(!eraseEmptyLine()) {
                                throw new FileException("Invalid file");
                        }
                        checkBoard(board);
                        if(!eraseEmptyLine()) {
                                throw new FileException("Invalid file");
                        }
                        while(line != null) {
                                checkComment(line.charAt(0));
                                if(line != null){
                    checkTypeCell(board);
                    eraseEmptyLine();
                 }
                        }
                    if(board.getPlayer() == null) {
                    	throw new MissingHero("En el mapa no hay un heroe");
                    }
                }
                finally {
                        if(inStream != null) {
                                inStream.close();
                        }
                }
        }
        
        private boolean eraseEmptyLine() throws IOException{
                int aux;
                
                line = inStream.readLine();
                if(line != null){
                        line = line.trim();
                        aux = line.compareTo("");
                        if(aux == 0) {
                                eraseEmptyLine();
                        }
                        return true;
                }
                return false;
        }
        
        private void checkBoard(Board board) throws IOException, FileException, ErrorReadingBoard{
                
                checkRowsAndCols(board);
                if(!eraseEmptyLine()) {
                        throw new ErrorReadingBoard("Invalid board parameters");
                }
                checkBoardName(board);
        }
        
        private void checkRowsAndCols(Board board) throws IOException, FileException {
                String[] argToCheck;
                int rows, cols;
                
                rows = 0;
                cols = 0;
                
                if(!checkComment(line.charAt(0))) {
                        argToCheck = line.split(",");
                        if(argToCheck.length > 2) {
                                if(!checkComment(argToCheck[2].charAt(0))) {
                                        throw new FileException("Invalid file");
                                }
                        }
                        argToCheck[0] = argToCheck[0].trim();
                        argToCheck[1] = argToCheck[1].trim();
                        rows = checkRow(argToCheck[1]);
                        cols = checkCol(argToCheck[0]);

                        board.setDimension(new Vector2D(cols, rows));
                }
                else {
                        checkRowsAndCols(board);
                }
        }
        
        private int checkRow(String arg) throws FileException {
                int rows;
                
                rows = Integer.parseInt(arg);
                if(rows < 6 || rows > 30) {
                        throw new FileException("Invalid file");
                }
                return rows;
        }
        
        private int checkCol(String arg) throws FileException {
                int cols;
                
                cols = Integer.parseInt(arg);
                if(cols < 6 || cols > 30) {
                        throw new FileException("Invalid file");
                }
                return cols;
        }
        
        private void checkBoardName(Board board) throws IOException, FileException {
                int index;

                index = line.indexOf("#");
                if(index != -1) {
                        if(index == 0) {
                                if(!eraseEmptyLine()) {
                                        throw new FileException("Invalid file");
                                }
                                checkBoardName(board);
                                
                        }
                        else {
                                 board.setNameAndPath(line.substring(0, index), path);
                 
                        }
                }
                else {
                        board.setNameAndPath(line, path);
            
                }
        }
        
        private boolean checkComment(int character) throws IOException, FileException {
                if(character == '#') {
                        eraseEmptyLine();
                        return true;
                }
                return false;
        }
        
        private void checkTypeCell(Board board) throws IOException, FileException, 
                CellIsNotEmpty, ErrorReadingWall, ErrorReadingPlayer, OutOfBoardException,
        InvalidLevelException, ErrorReadingEnemy {
                String[] argToCheck;
                int typeCell, x, y, enemyType, enemyLv, value, index;
                
                if(!checkComment(line.charAt(0))){
                        argToCheck = line.split(",");
                        if(argToCheck.length > 6) {
                                if(!checkComment(argToCheck[6].charAt(0))) {
                                        throw new FileException("Invalid file");
                                }
                        }
                        else if(argToCheck.length < 5) {
                                throw new FileException("Invalid file");
                        }
                        index = argToCheck[5].indexOf("#");
                        if(index > 0) {
                                argToCheck[5] = argToCheck[5].substring(0, index);
                        }
                        else if(index == 0) {
                                throw new FileException("Invalid file");
                        }
                        for(int i = 0; i < 6; i++) {
                                argToCheck[i] = argToCheck[i].trim();
                        }
                        typeCell = Integer.parseInt(argToCheck[0]);
                        x = Integer.parseInt(argToCheck[1]);
                        y = Integer.parseInt(argToCheck[2]);
                        enemyType = Integer.parseInt(argToCheck[3]);
                        enemyLv = Integer.parseInt(argToCheck[4]);                      
                        
                        value = Integer.parseInt(argToCheck[5]);
                        switch (typeCell) {
                        case PLAYER:
                                checkPlayer(x, y, enemyType, enemyLv, value, board);             
                                break;
                        case WALL:
                                checkWall(x, y, enemyType, enemyLv, value, board);
                                break;
                        case ENEMY:           
                                checkEnemy(x, y, enemyType, enemyLv, value, board);
                                break;
                        case BHEALTH:
                                checkBHealth(x, y, enemyType, enemyLv, value, board);
                                break;
                        case BSTRENGTH:
                                checkBStrength(x, y, enemyType, enemyLv, value, board);
                                break;
                        default:
                                throw new FileException("Invalid file");
                        }
                }
        }
        
        
        private void checkWall(int x, int y, int enemyType, int enemyLv, int value, Board board) 
                throws FileException, CellIsNotEmpty, ErrorReadingWall, OutOfBoardException{
                checkEmptyCell(x, y, board);
                if(enemyLv != 0 || enemyType != 0 || value != 0) {
                        throw new ErrorReadingWall("Error reading the wall parameter");
                }
                
                board.add(new Wall(), new Vector2D(x,y));
                
        }
        
        private void checkPlayer(int x, int y, int enemyType, int enemyLv, int value, Board board) throws FileException, 
                CellIsNotEmpty, ErrorReadingPlayer, OutOfBoardException {
        	checkEmptyCell(x, y, board);
                if(enemyLv != 0 || enemyType != 0 || value != 0) {
                        throw new ErrorReadingPlayer("Error reading the player parameter");
                }
                board.add(new Player(x, y));
        }
        
        private void checkEmptyCell(int x, int y, Board board) throws CellIsNotEmpty {
                if(!board.emptyCell(new Vector2D(x, y))) {
            throw new CellIsNotEmpty("This cell is not empty");
        }
        }
        
        private void checkEnemy(int x, int y, int enemyType, int enemyLv, int value, Board board) throws FileException, 
                CellIsNotEmpty, OutOfBoardException, InvalidLevelException, ErrorReadingEnemy {
        	checkEmptyCell(x, y, board);            
        	switch (enemyType) {
                                case 1:
                                        board.add(new Golem(enemyLv), new Vector2D(x,y));
                                        break;
                                case 2:
                                        board.add(new Dragon(enemyLv), new Vector2D(x, y));
                                        break;
                                case 3:
                                        board.add(new Snake(enemyLv), new Vector2D(x, y));
                                        break;
                                default:
                                        throw new ErrorReadingEnemy("Invalid enemy type");
                        }
        }

        private void checkBHealth(int x, int y, int enemyType, int enemyLv, int value, Board board) throws FileException, 
                CellIsNotEmpty, OutOfBoardException {
        	checkEmptyCell(x, y, board);
                if(value < 0 || enemyType != 0 || enemyLv != 0)
                        throw new FileException("Invalid file");
                board.add(new HealthBonus(value), new Vector2D(x, y));
        }
        
        private void checkBStrength(int x, int y, int enemyType, int enemyLv, int value, Board board) throws FileException, 
        CellIsNotEmpty, OutOfBoardException {
        	checkEmptyCell(x, y, board);
                if(value < 0 || enemyType != 0 || enemyLv != 0)
                        throw new FileException("Invalid file");
                board.add(new StrengthBonus(value), new Vector2D(x, y));
        }
}