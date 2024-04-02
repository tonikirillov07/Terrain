package org.darkness.engine.sounds;

import lombok.Getter;
import org.darkness.engine.logs.Logs;
import org.newdawn.slick.Sound;

@Getter
public class SoundPlayer {
    private Sound sound;

    public SoundPlayer(String soundPath) {
        try{
            sound = new Sound(soundPath);
        }catch (Exception e){
            Logs.makeErrorLog(e);
        }
    }

    public void play(){
        sound.play();
    }

}
