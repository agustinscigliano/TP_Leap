package frontEnd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import parser.FileException;
import parser.ReadMapName;
import entities.Directories;
import entities.ListFiles;

public class MapSelection {

	private String input;
	private String[] chooseMap, choices;
	private Map<String, String> filesMap;

	public MapSelection(String dirName) throws FileException,
			FileNotFoundException, IOException {

		filesMap = new HashMap<String, String>();
		ListFiles file = new ListFiles(dirName, ".board");
		choices = file.getFiles();
		chooseMap = new String[choices.length];
		readAllNamesMaps();
	}

	public String getInput() {
		return input;
	}

	public String getPath(String key) {
		return filesMap.get(key);
	}

	private void readAllNamesMaps() throws FileNotFoundException, IOException,
			FileException {
		for (int i = 0; i < choices.length; i++) {
			ReadMapName parser = new ReadMapName(Directories.boards
					+ choices[i]);
			chooseMap[i] = parser.readMap();
			filesMap.put(chooseMap[i], parser.getPath());
		}
		input = (String) JOptionPane.showInputDialog(null, "BOARDS",
				"Choose a map", JOptionPane.QUESTION_MESSAGE, new ImageIcon(
						Directories.resources + "iconDungeons.png"), chooseMap,
				chooseMap[0]);

	}

}