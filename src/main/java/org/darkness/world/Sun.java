package org.darkness.world;

import lombok.AllArgsConstructor;
import org.darkness.engine.camera.Camera;
import org.darkness.engine.logs.Logs;
import org.lwjgl.opengl.GL11;

@AllArgsConstructor
public class Sun {
    private final Camera camera;
    private final Lighting lighting;
    private static final float SUN_SCALE = 7f;

    public void update() {
        try {
            GL11.glPushMatrix();
            GL11.glRotatef(camera.getRotation().getX(), 1, 0, 0);
            GL11.glRotatef(camera.getRotation().getY(), 0, 1, 0);
            GL11.glRotatef(-lighting.getCurrentLightAngle() * 2, 1, 0, 0);
            GL11.glTranslatef(0, 0, 100);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glColor3f(1f, 1f, 1f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, SUN_SCALE);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(SUN_SCALE, SUN_SCALE);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(SUN_SCALE, 0);
            GL11.glEnd();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            GL11.glPopMatrix();
        }catch (Exception e){
            Logs.makeErrorLog(e);
        }
    }
}
