package org.darkness.engine.sounds;

import org.darkness.engine.logs.Logs;
import org.newdawn.slick.Sound;

import java.io.File;
import java.util.Random;

public class Footsteps {
    private float timer = 0;
    private final Random random;

    public Footsteps() {
        random = new Random();
    }

    public void play(float deltaTime, float speed, File[] stepsFiles){
        if(stepsFiles != null) {
            float footstepDelay = (float) (((1 / speed) + deltaTime) * Math.PI);

            if (timer >= footstepDelay) {
                try {
                    Sound sound = new Sound(stepsFiles[random.nextInt(stepsFiles.length)].getPath());
                    sound.play();

                    timer = 0;
                } catch (Exception e) {
                    Logs.makeErrorLog(e);
                }
            }

            timer += deltaTime;
        }
    }
}
