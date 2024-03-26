package org.darkness.world;

import org.darkness.engine.logs.Logs;
import org.lwjgl.opengl.GL11;

public class Lighting {
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
}
