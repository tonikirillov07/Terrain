package org.darkness.ui.hud;

import org.darkness.engine.models.Model;
import org.darkness.engine.utils.textures.TexturesConstants;
import org.darkness.engine.utils.transform.Rotation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

public class Target extends Model {
    public Target(Vector3f position, Rotation rotation, Color color, int texture, float scale) {
        super(position, rotation, color, texture, scale);

        setDetectCollision(false);
        setTexture(TexturesConstants.TARGET_TEXTURE);
    }

    @Override
    public void render() {
        super.render();

        renderUIElement(() -> {
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
        });
    }
}
