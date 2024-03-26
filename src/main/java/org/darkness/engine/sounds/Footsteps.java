package org.darkness.engine.sounds;

import org.darkness.Constants;
import org.darkness.engine.logs.Logs;
import org.newdawn.slick.Sound;

import java.io.File;
import java.util.Random;

public class Footsteps {
    private float timer = 0;
    private final File[] stepsSounds;
    private final Random random;

    public Footsteps() {
        stepsSounds = new File("sounds/steps").listFiles();
        random = new Random();
    }

    public void play(float deltaTime){
        float footstepDelay = (Constants.DEFAULT_MOVE_SPEED * (2.4f + deltaTime)) / 100;

        if(timer >= footstepDelay){
            try {
                Sound sound = new Sound(stepsSounds[random.nextInt(stepsSounds.length)].getPath());
                sound.play();

                timer = 0;
            }catch (Exception e){
                Logs.makeErrorLog(e);
            }
        }

        timer += deltaTime;
    }
}
