package org.darkness.engine.sounds;

import org.darkness.engine.logs.Logs;
import org.newdawn.slick.Sound;

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

    public Sound getSound() {
        return sound;
    }
}
