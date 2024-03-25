package org.darkness.envirounment;

import org.darkness.engine.utils.textures.TexturesUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public class SkyBox {
    public void init(){

    }

    public void initTextures(String @NotNull [] skyBoxTextures, int target){
        GL11.glGenTextures();
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, target);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        for (int i = 0; i < skyBoxTextures.length; i++) {
            int texture = TexturesUtil.createTextureId(skyBoxTextures[i], TexturesUtil.LINEAR);

            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, Display.getWidth(), Display.getHeight(),
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texture);

        }

        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
    }
}
