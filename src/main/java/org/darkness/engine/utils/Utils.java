package org.darkness.engine.utils;

import org.darkness.engine.logs.Logs;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Utils {
    public static @Nullable FloatBuffer createFloatBuffer(float[] floats){
        try {
            ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1024);
            FloatBuffer floatBuffer = directByteBuffer.asFloatBuffer();
            floatBuffer.put(floats);

            return floatBuffer;
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }

        return null;
    }
}
