package org.darkness.engine.utils.textures;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record TextureRectangle(float top, float bottom, float left, float right) {
    @Contract(" -> new")
    public static @NotNull TextureRectangle getDefault(){
        return new TextureRectangle(0,0,0,0);
    }
}
