package entities;

import parser.ErrorReadingPlayer;
/**
 * Generates an exception when an invalid level is used or required.
 * @author mpurita, gdelgiud, ppauli
 *
 */
public class InvalidLevelException extends ErrorReadingPlayer {

    public InvalidLevelException(String message){
        super(message);
    }
}
