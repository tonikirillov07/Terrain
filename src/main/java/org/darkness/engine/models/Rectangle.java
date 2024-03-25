package org.darkness.engine.models;

import org.darkness.engine.utils.transform.Rotation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

public class Rectangle extends Model{
    public Rectangle(Vector3f position, Rotation rotation, Color color, int texture, float scale) {
        super(position, rotation, color, texture, scale);
    }

    @Override
    public void render() {
        super.render();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(0,0);
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(0, getScale());
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(getScale(), getScale());
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(getScale(), 0);
        GL11.glEnd();
    }
}
