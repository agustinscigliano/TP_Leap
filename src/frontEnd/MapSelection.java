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
	private String chooseMap;
	private Map<String, String> filesMap;

	public MapSelection(String dirName) throws FileException,
			FileNotFoundException, IOException {

		filesMap = new HashMap<String, String>();

		input = "Dragon temple";
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

		ReadMapName parser = new ReadMapName(Directories.boards
					+ "Catedra.board");
		chooseMap = parser.readMap();
		filesMap.put(chooseMap, parser.getPath());


	}

}