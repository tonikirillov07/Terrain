package org.darkness.engine.sounds;

import java.io.File;

public abstract class SoundsConstants {
    private static final String SOUNDS_FOLDER = "sounds";
    public static final File[] GROUND_SOUNDS = new File(SOUNDS_FOLDER + "/steps/ground").listFiles();
    public static final File[] STONE_SOUNDS = new File(SOUNDS_FOLDER + "/steps/stone").listFiles();
}
