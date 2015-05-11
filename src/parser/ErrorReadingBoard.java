/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

/**
 *
 * @author mpurita
 */
public class ErrorReadingBoard extends Exception{
    public ErrorReadingBoard(String message) {
        super(message);
    }
}