package org.darkness.world;

import org.darkness.engine.logs.Logs;
import org.darkness.engine.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.Objects;

public class Fog {
    public void init(){
        try {
            Logs.makeInfoLog("Initializing fog...");

            GL11.glEnable(GL11.GL_FOG);
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFog(GL11.GL_FOG_COLOR, Objects.requireNonNull(Utils.createFloatBuffer(new float[]{0.5f, 0.5f, 0.5f, 1f})));
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.35f);
            GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_DONT_CARE);
            GL11.glFogf(GL11.GL_FOG_START, 3.0f);
            GL11.glFogf(GL11.GL_FOG_END, 6.0f);

            Logs.makeInfoLog("Success!");
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }
}
