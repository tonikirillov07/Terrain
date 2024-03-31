package org.darkness.engine.utils;

import org.darkness.engine.logs.Logs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

public class Utils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static @Nullable FloatBuffer createFloatBuffer(float[] floats){
        try {
            FloatBuffer buffer = ByteBuffer.allocateDirect(floats.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            buffer.put(floats).flip();

            return buffer;
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }

        return null;
    }

    public static float getDistance(@NotNull Vector3f firstPosition, @NotNull Vector3f secondPosition){
        return (float) Math.sqrt(Math.pow(secondPosition.getX() - firstPosition.getX(), 2) + Math.pow(secondPosition.getY() - firstPosition.getY(), 2)
                + Math.pow(secondPosition.getZ() - firstPosition.getZ(), 2));
    }

    public static float roundNumber(float number){
        return Float.parseFloat(DECIMAL_FORMAT.format(number));
    }
}
