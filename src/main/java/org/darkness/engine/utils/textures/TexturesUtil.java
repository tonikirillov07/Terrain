package org.darkness.engine.utils.textures;

import org.darkness.Main;
import org.darkness.engine.logs.Logs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;


public class TexturesUtil {
    public static final int NEAREST = GL_NEAREST;
    public static final int LINEAR = GL_LINEAR;
    public static final int NO_TEXTURE = -1;

    public static int createTextureId(String path, int filter){
        try {
            Texture texture = getTexture(path, filter);
            checkIsTextureNormal(texture);

            assert texture != null;
            return texture.getTextureID();
        }catch (Exception e){
            Logs.makeErrorLog(e);
        }

        return -1;
    }

    public static @Nullable Texture createTexture(String path, int filter){
        try {
            Texture texture = getTexture(path, filter);
            checkIsTextureNormal(texture);

            return texture;
        }catch (Exception e){
            Logs.makeErrorLog(e);
        }

        return null;
    }

    private static @Nullable Texture getTexture(String path, int filter){
        try {
            return TextureLoader.getTexture("PNG", Objects.requireNonNull(Main.class.getResourceAsStream(path)), filter);
        }catch (Exception e){
            Logs.makeErrorLog(e);
        }

        return null;
    }

    private static void checkIsTextureNormal(Texture texture){
        if(!isNormalTexture(texture)){
            Logs.makeErrorLog(new Exception("The texture has the wrong resolution! It may not display correctly. Texture size: " + texture.getImageWidth() + "x" + texture.getImageHeight()));
        }
    }

    private static boolean isNormalTexture(@NotNull Texture texture){
        return texture.getImageWidth() == texture.getImageHeight();
    }
}
