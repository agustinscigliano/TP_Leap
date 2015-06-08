/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class ReadMapName{
    private String line;
    private BufferedReader inStream;
    private String path;

    public ReadMapName(String inName) throws FileNotFoundException, IOException {
       File file = new File(inName);
       inStream = new BufferedReader(new FileReader(file));
       path = file.getCanonicalPath();
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

    private String readName() throws FileException, IOException {
        line = line.trim();

        int index;

                index = line.indexOf("#");
                if(index != -1) {
                        if(index == 0) {
                                if(!eraseEmptyLine()) {
                                        throw new FileException("Invalid file");
                                }
                                return readName();

                        }
                        else {
                                 return line.substring(0, index);
                        }
                }
                else {
                        return line;
                }
        }

    public String readMap() throws IOException, FileException {
        String mapName;
        try {
                        if(!eraseEmptyLine()) {
                                throw new FileException("Invalid file");
                        }
            skipDimension();
                        if(!eraseEmptyLine()) {
                                throw new FileException("Invalid file");
                        }
            mapName = readName();

                }
                finally {
                        if(inStream != null) {
                                inStream.close();
                        }
                }
        return mapName;
    }
    private void skipDimension() throws IOException, FileException {
        if(!checkComment(line.charAt(0))) {
            line = inStream.readLine();
        }
    }

    private boolean checkComment(int character) throws IOException, FileException {
                if(character == '#') {
                        eraseEmptyLine();
                        return true;
                }
                return false;
        }
    public String getPath() {
        return path;
    }

}