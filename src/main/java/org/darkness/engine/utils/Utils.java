package org.darkness.engine.utils;

import org.darkness.engine.logs.Logs;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utils {
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
}
