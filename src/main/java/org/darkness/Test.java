package org.darkness;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.util.Objects;

public class Test {
    private static final String[] TEXTURES = {
            "/textures/skybox/skyboxRight.png",
            "/textures/skybox/skyboxLeft.png",
            "/textures/skybox/skyboxUp.png",
            "/textures/skybox/skyboxDown.png",
            "/textures/skybox/skyboxBack.png",
            "/textures/skybox/skyboxFront.png"
    };

    private final int[] textureIDs = new int[6];

    public Test() throws IOException {
        loadTextures();
    }

    public void render() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, -10); // Перемещаем skybox, чтобы он был далеко вдали
        GL11.glColor3f(1f, 1f, 1f);

        // Отрисовка каждой стороны skybox
        for (int i = 0; i < 6; i++) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDs[i]);
            GL11.glBegin(GL11.GL_QUADS);
            // Отрисовываем каждую сторону с текстурой
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3f(-1, -1, 1);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex3f(1, -1, 1);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex3f(1, 1, 1);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex3f(-1, 1, 1);
            GL11.glEnd();
        }

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void loadTextures() throws IOException {
        for (int i = 0; i < 6; i++) {
            textureIDs[i] = TextureLoader.getTexture("PNG", Objects.requireNonNull(Test.class.getResourceAsStream(TEXTURES[i]))).getTextureID();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Test skybox = new Test();

        // Цикл отрисовки
        while (!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0, 0, -10); // Перемещаем камеру за skybox

            skybox.render();

            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }
}
