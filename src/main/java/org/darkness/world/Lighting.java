package org.darkness.world;

import org.darkness.Constants;
import org.darkness.engine.logs.Logs;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class Lighting extends Constants {
    private float currentLightAngle = 0;

    public void init(){
        try {
            Logs.makeInfoLog("Initializing light...");

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);

            Logs.makeInfoLog("Success!");
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public void doDayNightCycle(float deltaTime){
        GL11.glPushMatrix();
        GL11.glRotatef(currentLightAngle, 1, 0, 1);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Objects.requireNonNull(LIGHT0_POSITION));
        GL11.glPopMatrix();

        currentLightAngle += deltaTime * 10;
        if(currentLightAngle >= 360) currentLightAngle = 0;
    }

    public float getCurrentLightAngle() {
        return currentLightAngle;
    }
}
