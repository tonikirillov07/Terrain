package org.darkness.engine.utils.transform;

public record Rotation(float angle, float x, float y, float z) {
    public static final Rotation IDENTITY = new Rotation(0,1,1,1);
}
