package org.darkness.world;

import org.darkness.Constants;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.utils.Utils;
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
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Objects.requireNonNull(Utils.createFloatBuffer(new float[]{1 + getSunColorIntensity() * 2, 1, 1, 0})));

        float color = (getSkyColorCoefficient() * 0.15f) + 0.15f;
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Objects.requireNonNull(Utils.createFloatBuffer(new float[]{color, color, color, 1})));
        GL11.glPopMatrix();

        currentLightAngle += deltaTime * 20;
        if(currentLightAngle > 180) currentLightAngle -= 360;
    }


    public float getNormalizedLightAngle(){
        return (getCurrentLightAngle() + 180) / 360;
    }

    public float getSkyColorCoefficient(){
        return 1 - (Math.abs(getCurrentLightAngle()) / 180);
    }

    public float getSunColorIntensity(){
        return 90 - Math.abs(getCurrentLightAngle());
    }

    public float getCurrentLightAngle() {
        return currentLightAngle;
    }
}
