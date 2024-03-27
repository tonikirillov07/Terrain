package org.darkness.engine.sounds.ambience;

import org.darkness.engine.sounds.SoundPlayer;

public class BackgroundSounds {
    private final SoundPlayer soundPlayer;

    public BackgroundSounds() {
        soundPlayer = new SoundPlayer("sounds/ambience/ambience.ogg");
    }

    public void start(){
        soundPlayer.play();
    }
}
