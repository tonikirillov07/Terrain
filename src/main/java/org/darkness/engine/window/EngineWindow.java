package org.darkness.engine.window;

import org.darkness.Constants;
import org.darkness.engine.GlobalRender;
import org.darkness.engine.camera.Camera;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.models.Cube;
import org.darkness.engine.models.Rectangle;
import org.darkness.engine.sounds.SoundsConstants;
import org.darkness.engine.sounds.ambience.BackgroundSounds;
import org.darkness.engine.utils.Utils;
import org.darkness.engine.utils.textures.TexturesConstants;
import org.darkness.engine.utils.transform.Rotation;
import org.darkness.ui.text.TextDrawer;
import org.darkness.world.Fog;
import org.darkness.world.Lighting;
import org.darkness.world.Sun;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

import static org.darkness.engine.utils.textures.TexturesUtil.NO_TEXTURE;

public class EngineWindow extends Constants {
    private float fps;
    private float deltaTime;
    private GlobalRender globalRender;
    private Camera camera;
    private TextDrawer fpsText, positionText;
    private Lighting lighting;
    private Sun sun;

    public void create() {
        try {
            Display.setTitle(TITLE);
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setVSyncEnabled(IS_VSYNC_ENABLED);
            Display.setResizable(IS_RESIZABLE_ENABLED);
            Display.create();

            Mouse.create();
            Keyboard.create();

            Mouse.setGrabbed(true);

            globalRender = new GlobalRender();
            camera = new Camera(Camera.getRandomPosition(WORLD_SIZE[0], WORLD_SIZE[1]), new Vector3f(0, 0, 0), globalRender.getModelList());
            Fog fog = new Fog();
            lighting = new Lighting();
            sun = new Sun(camera, lighting);
            lighting.init();
            new BackgroundSounds().start();

            for (int i = 0; i < WORLD_SIZE[0]; i++) {
                for (int j = 0; j < WORLD_SIZE[1]; j++) {
                    int randomTexture = new Random().nextInt(1, 4);
                    int currentTexture =  randomTexture == 1 ? TexturesConstants.DIRT_TEXTURE : randomTexture == 2 ? TexturesConstants.STONE_TEXTURE: TexturesConstants.GRASS_TEXTURE;

                    globalRender.load(new Rectangle(new Vector3f(i, 0, j), new Rotation(-90f, 1, 0, 0), WHITE_COLOR, currentTexture, 1f, randomTexture != 2 ? SoundsConstants.GROUND_SOUNDS: SoundsConstants.STONE_SOUNDS));

                }
            }

            globalRender.load(new Cube(new Vector3f((float) WORLD_SIZE[0] / 3, 2 , (float) WORLD_SIZE[1] / 3), Rotation.IDENTITY, WHITE_COLOR, new Random().nextInt(1, 3) == 1 ? TexturesConstants.DIRT_TEXTURE : TexturesConstants.GRASS_TEXTURE, 1f));
            globalRender.load(new Cube(new Vector3f((float) WORLD_SIZE[0] / 2, 2 , (float) WORLD_SIZE[1] / 2), Rotation.IDENTITY, WHITE_COLOR, new Random().nextInt(1, 3) == 1 ? TexturesConstants.DIRT_TEXTURE : TexturesConstants.GRASS_TEXTURE, 1f));

            fpsText = new TextDrawer(new Vector3f(-((float) Display.getWidth() / 2) + 30, ((float) Display.getHeight() / 2) - 30, -2), new Rotation(-90f, 0, 0, 1), WHITE_COLOR, NO_TEXTURE, 16f, "FPS: 0");
            positionText = new TextDrawer(new Vector3f(-((float) Display.getWidth() / 2) + 30, ((float) Display.getHeight() / 2) - 55, -2), new Rotation(-90f, 0, 0, 1), WHITE_COLOR, NO_TEXTURE, 16f, "Position: 0");
            globalRender.load(fpsText);
            globalRender.load(positionText);

            initGLContext();
            initLoop();
        } catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void initGLContext() {
        try {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glClearColor((float) SKY_COLOR.getRed() / 100, (float) SKY_COLOR.getGreen() / 100, (float) SKY_COLOR.getBlue() / 100, 1);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthFunc(GL11.GL_LEQUAL);

            GL11.glEnable(GL13.GL_MULTISAMPLE);

            GL11.glLoadIdentity();
            GL11.glFrustum(-1, 1, -1, 1, 2, Z_FAR);

            resizeWindow(Display.getWidth(), Display.getHeight());
            GL11.glTranslatef(0, 0, -2);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void initLoop(){
        while(!Display.isCloseRequested()){
            try {
                float startTime = System.nanoTime();

                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                listenEspecialInput();
                listenWindowEvents();
                if(Mouse.isGrabbed()) keepCursorInCenter();

                lighting.doDayNightCycle(deltaTime);

                GL11.glClearColor(((float) SKY_COLOR.getRed() / 100) * lighting.getSkyColorCoefficient(), ((float) SKY_COLOR.getGreen() / 100) * lighting.getSkyColorCoefficient(),
                        ((float) SKY_COLOR.getBlue() / 100) * lighting.getSkyColorCoefficient(), 1);

                sun.update();
                globalRender.renderAll();
                updateCamera();

                fpsText.setText("FPS:" + Math.round(getFps()));
                positionText.setText("Position:" + (Utils.roundNumber(camera.getPosition().x) + "," +
                        Utils.roundNumber(camera.getPosition().y) + "," + Utils.roundNumber(camera.getPosition().z)));

                Display.update();
                Display.sync(FPS);

                deltaTime = (System.nanoTime() - startTime) / 1_000_000_000f;
                fps = 1 / deltaTime;

                Display.setTitle(TITLE + " (FPS: " + Math.round(fps) + "). Position: " + camera.getPosition());
            }catch (Exception e) {
                Logs.makeErrorLog(e);
            }
        }

        destroy();
    }

    private void keepCursorInCenter(){
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
    }

    private void updateCamera() {
        camera.control(deltaTime);
        camera.update();

        camera.checkIsOnGround(globalRender.getModelList());
        camera.applyGravity();

        if(camera.getPosition().y >= 22) camera.setPosition(Camera.getRandomPosition(WORLD_SIZE[0], WORLD_SIZE[1]));
    }

    private void listenWindowEvents() {
        if (Display.wasResized()) resizeWindow(Display.getWidth(), Display.getHeight());
        if(Display.isCloseRequested()) destroy();
    }

    private void listenEspecialInput() {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) destroy();
        if(Keyboard.isKeyDown(Keyboard.KEY_P)) System.out.println(camera.getPosition());
        if(Keyboard.isKeyDown(Keyboard.KEY_R)) camera.resetTransform();
        if(Keyboard.isKeyDown(Keyboard.KEY_F5)) Mouse.setGrabbed(!Mouse.isGrabbed());
        if(Keyboard.isKeyDown(Keyboard.KEY_F6)) globalRender.getModelList().get(0).tpCamera(camera);
    }

    private void destroy() {
        try {
            globalRender.clear();

            AL.destroy();
            Keyboard.destroy();
            Mouse.destroy();
            Display.destroy();

            System.exit(0);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public float getFps() {
        return fps;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    private void resizeWindow(int width, int height){
        try {
            GL11.glViewport(0, 0, width, height);
            float xToYRatio = (float) width / height;
            float size = 0.1f;

            GL11.glLoadIdentity();
            GL11.glFrustum(-xToYRatio * size, xToYRatio * size, -size, size, size * 2, Z_FAR);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }
}
