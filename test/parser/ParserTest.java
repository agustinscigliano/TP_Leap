/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import entities.Board;
import entities.InvalidLevelException;
import entities.OutOfBoardException;

/**
 *
 * @author mpurita
 */
public class ParserTest {
	private Parser instance;
	private Board board;
	
	@Before
	public void setUp() {
		instance = null;
		board = new Board();
	}
    
    @Test(expected=FileException.class)
    public void readAnEmptyFile() {
			try {
				instance = new Parser("./test/parser/vacio.board.txt");
				instance.checkFile(board);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidLevelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CellIsNotEmpty e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingWall e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingPlayer e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfBoardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingEnemy e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingBoard e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MissingHero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    @Test(expected=CellIsNotEmpty.class)
    public void twoObjectInTheSamePosition(){
    	try{
    		instance = new Parser("./test/parser/cellIsNotEmpty.board.txt");
			instance.checkFile(board);
		} catch (InvalidLevelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CellIsNotEmpty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorReadingWall e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorReadingPlayer e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfBoardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorReadingEnemy e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorReadingBoard e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingHero e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test(expected=MissingHero.class)
   public void mapWithoutHero(){
    		try {
				instance = new Parser("./test/parser/withoutHero.board.txt");
				instance.checkFile(board);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidLevelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CellIsNotEmpty e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingWall e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingPlayer e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfBoardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingEnemy e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorReadingBoard e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MissingHero e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
}