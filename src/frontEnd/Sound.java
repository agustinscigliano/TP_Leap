package frontEnd;

import java.io.FileInputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {

    public static void play(String filename){
        AudioStream as = null;
        try{
            as = new AudioStream(new FileInputStream(filename));
            AudioPlayer.player.start(as);
        }catch(Exception e){
            
        }
    }
}