package org.darkness;

import org.darkness.engine.utils.Utils;
import org.lwjgl.util.Color;

import java.nio.FloatBuffer;

public abstract class Constants {
    public static final short SCREEN_WIDTH = 1200;
    public static final short SCREEN_HEIGHT = 800;
    public static final short FPS = 60;
    public static final String TITLE = "Game";
    public static final boolean IS_VSYNC_ENABLED = true;
    public static final boolean IS_RESIZABLE_ENABLED = true;
    public static final Color SKY_COLOR = new Color(52, 80, 92);
    public static final float LOOK_X_LIMIT = 90f;
    public static final String SKYBOX_TEXTURES_PATH = "/textures/skybox/";
    public static final byte MOVE_FORWARD = -1;
    public static final byte MOVE_BACKWARD = 1;
    public static final byte MOVE_LEFT = 2;
    public static final byte MOVE_RIGHT = -2;
    public static final byte DONT_MOVE = 0;
    public static final byte MOVE_UP = 3;
    public static final byte MOVE_DOWN = -3;
    public static final float DEFAULT_MOVE_SPEED = 7f;
    public static final float DEFAULT_MOUSE_SENSITIVITY = 5f;
    public static final String FONT_TEXTURE_DEFAULT_PATH = "/textures/font/Verdana.png";
    public static final double CAMERA_SHAKE_VALUES_LIMIT = Math.PI;
    public static final FloatBuffer LIGHT0_POSITION = Utils.createFloatBuffer(new float[]{0, 1, 0, 0});
    public static final String SKYBOX_BACK_TEXTURE_PATH = SKYBOX_TEXTURES_PATH + "skyboxBack.png";
    public static final String SKYBOX_DOWN_TEXTURE_PATH = SKYBOX_TEXTURES_PATH + "skyboxDown.png";
    public static final String SKYBOX_FRONT_TEXTURE_PATH = SKYBOX_TEXTURES_PATH + "skyboxFront.png";
    public static final String SKYBOX_LEFT_TEXTURE_PATH = SKYBOX_TEXTURES_PATH + "skyboxLeft.png";
    public static final String SKYBOX_RIGHT_TEXTURE_PATH = SKYBOX_TEXTURES_PATH + "skyboxRight.png";
    public static final String SKYBOX_UP_TEXTURE_PATH = SKYBOX_TEXTURES_PATH + "skyboxUp.png";
}
