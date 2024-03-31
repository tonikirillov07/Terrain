package org.darkness.engine.models;

import org.darkness.engine.logs.Logs;
import org.darkness.engine.utils.transform.Rotation;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;

import static org.lwjgl.opengl.GL11.*;

public class Cube extends Model{
    public Cube(Vector3f position, Rotation rotation, Color color, int texture, float scale) {
        super(position, rotation, color, texture, scale);
    }

    public Cube(Vector3f position, Rotation rotation, Color color, int texture, float scale, File[] stepSounds) {
        super(position, rotation, color, texture, scale, stepSounds);
    }

    @Override
    public void render() {
        try {
            super.render();

            float scale = getScale();

            //Front
            glBegin(GL_QUADS);
            glNormal3f(0, 0, 1);
            glTexCoord3f(0, 0, 0);
            glVertex3f(0, 0, 0);
            glTexCoord3f(0, 1, 0);
            glVertex3f(0, scale, 0);
            glTexCoord3f(1, 1, 0);
            glVertex3f(scale, scale, 0);
            glTexCoord3f(1, 0, 0);
            glVertex3f(scale, 0, 0);

            //Back
            glNormal3f(0, 0, -1);
            glTexCoord3f(0, 0, 1);
            glVertex3f(0, 0, scale);
            glTexCoord3f(0, 1, 1);
            glVertex3f(0, scale, scale);
            glTexCoord3f(1, 1, 1);
            glVertex3f(scale, scale, scale);
            glTexCoord3f(1, 0, 1);
            glVertex3f(scale, 0, scale);

            //Top
            glNormal3f(0, 1, 0);
            glTexCoord3f(0, 0, 0);
            glVertex3f(0, scale, 0);
            glTexCoord3f(0, 1, 0);
            glVertex3f(0, scale, scale);
            glTexCoord3f(1, 1, 0);
            glVertex3f(scale, scale, scale);
            glTexCoord3f(1, 0, 0);
            glVertex3f(scale, scale, 0);

            //Bottom
            glNormal3f(0, -1, 0);
            glTexCoord3f(0, 0, 0);
            glVertex3f(0, 0, 0);
            glTexCoord3f(0, 1, 0);
            glVertex3f(0, 0, scale);
            glTexCoord3f(1, 1, 0);
            glVertex3f(scale, 0, scale);
            glTexCoord3f(1, 0, 0);
            glVertex3f(scale, 0, 0);

            //Left
            glNormal3f(-1, 0, 0);
            glTexCoord3f(0, 0, 0);
            glVertex3f(0, 0, 0);
            glTexCoord3f(0, 1, 0);
            glVertex3f(0, scale, 0);
            glTexCoord3f(1, 1, 0);
            glVertex3f(0, scale, scale);
            glTexCoord3f(1, 0, 0);
            glVertex3f(0, 0, scale);

            //Right
            glNormal3f(1, 0, 0);
            glTexCoord3f(0, 0, 0);
            glVertex3f(scale, 0, 0);
            glTexCoord3f(0, 1, 0);
            glVertex3f(scale, scale, 0);
            glTexCoord3f(1, 1, 0);
            glVertex3f(scale, scale, scale);
            glTexCoord3f(1, 0, 0);
            glVertex3f(scale, 0, scale);
            glEnd();
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }
}
