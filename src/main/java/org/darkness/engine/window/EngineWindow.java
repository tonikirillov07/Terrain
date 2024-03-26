package org.darkness.engine.window;

import org.darkness.Constants;
import org.darkness.engine.GlobalRender;
import org.darkness.engine.camera.Camera;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.models.Cube;
import org.darkness.engine.utils.textures.TexturesUtil;
import org.darkness.engine.utils.transform.Rotation;
import org.darkness.ui.text.TextDrawer;
import org.darkness.world.Fog;
import org.darkness.world.Lighting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

import static org.darkness.engine.utils.textures.TexturesUtil.NO_TEXTURE;

public class EngineWindow extends Constants {
    private float fps;
    private float deltaTime;
    private GlobalRender globalRender;
    private int texture;
    private Camera camera;
    private TextDrawer text;
    private Fog fog;
    private Lighting lighting;

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
            camera = new Camera(new Vector3f(-8.95068f, -2.4447231f, -12.380178f), new Vector3f(0, 0, 0));
            fog = new Fog();
            lighting = new Lighting();
            fog.init();
            lighting.init();

            int dirtTexture = TexturesUtil.createTextureId("/textures/dirt.png", TexturesUtil.LINEAR);
            int grassTexture = TexturesUtil.createTextureId("/textures/grass.png", TexturesUtil.LINEAR);
            texture = TexturesUtil.createTextureId("/textures/target.png", TexturesUtil.LINEAR);

            int currentTexture = dirtTexture;
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    currentTexture = currentTexture == dirtTexture ? grassTexture: dirtTexture;
                    globalRender.load(new Cube(new Vector3f(i, 0, j), Rotation.IDENTITY, new Color(255, 255, 255), currentTexture, 1f));
                }
            }

            text = new TextDrawer(new Vector3f(0, 2.5324621f, 0), new Rotation(-90, 0, 0, 1), new Color(255, 255, 255), NO_TEXTURE, 0.5f, "FPS: 0");
            globalRender.load(text);

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
            GL11.glFrustum(-1, 1, -1, 1, 2, 6);

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

                camera.control(deltaTime);
                camera.update();

                text.setText("FPS:" + Math.round(getFps()));

                globalRender.renderAll();

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

    private void listenWindowEvents() {
        if (Display.wasResized()) resizeWindow(Display.getWidth(), Display.getHeight());
        if(Display.isCloseRequested()) destroy();
    }

    private void listenEspecialInput() {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) destroy();
        if(Keyboard.isKeyDown(Keyboard.KEY_P)) System.out.println(camera.getPosition());
        if(Keyboard.isKeyDown(Keyboard.KEY_R)) camera.resetTransform();
    }

    private void destroy() {
        try {
            globalRender.clear();

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
            float XToYRatio = (float) width / height;
            float size = 0.1f;

            GL11.glLoadIdentity();
            GL11.glFrustum(-XToYRatio * size, XToYRatio * size, -size, size, size * 2, 100);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }
}
