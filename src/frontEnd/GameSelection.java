/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frontEnd;

import entities.Directories;
import entities.ListFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import parser.FileException;

/**
 *
 * @author mpurita
 */
public class GameSelection {
    private HashMap<String,String> filesGame;
    private String input;
    private String boardPath;
    private String[] chooseGame, choices;

        public GameSelection(String dirName) throws FileException, FileNotFoundException, IOException{
        filesGame = new HashMap();
        ListFiles file = new ListFiles(dirName, ".save");
        choices = file.getFiles();
        chooseGame = new String[choices.length];
        readAllGames();
        }

        public String getInput() {
                return input;
        }

    public String getPath(String key) {
        return filesGame.get(key);
    }

    private void readAllGames() throws FileNotFoundException, IOException, FileException {
        for(int i = 0; i < choices.length; i++) {
            chooseGame[i] = choices[i].substring(0, choices[i].length()-5);
            File file = new File(Directories.savegames + choices[i]);
            boardPath = readBoardPath(file);
            filesGame.put(chooseGame[i], file.getCanonicalPath());
        }
             input = (String)JOptionPane.showInputDialog(null, "BOARDS", "Choose a map",
                                JOptionPane.QUESTION_MESSAGE, new ImageIcon(Directories.resources + "iconDungeons.png"),
                chooseGame, chooseGame[0]);

    }

    public String getBoardPath() {
        return boardPath;
    }

    private String readBoardPath(File file) throws FileNotFoundException, IOException {
        BufferedReader inStream = new BufferedReader(new FileReader(file));
        return inStream.readLine().trim();
    }
}