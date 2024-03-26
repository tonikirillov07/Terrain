package org.darkness.ui.text;

import org.darkness.engine.models.Model;
import org.darkness.engine.utils.textures.TextureRectangle;
import org.darkness.engine.utils.textures.TexturesUtil;
import org.darkness.engine.utils.transform.Rotation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

import static org.darkness.Constants.FONT_TEXTURE_DEFAULT_PATH;

public class TextDrawer extends Model {
    private String text;
    public TextDrawer(Vector3f position, Rotation rotation, Color color, int texture, float scale, String text) {
        super(position, rotation, color, texture, scale);
        this.text = text;

        setTexture(TexturesUtil.createTextureId(FONT_TEXTURE_DEFAULT_PATH, TexturesUtil.NEAREST));
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        GL11.glPushMatrix();

        super.render();
        bindTexture();

        for (int i = 0; i < text.length(); i++) {
            TextureRectangle textureRectangle = calculateTextureRectangle(text.toCharArray()[i]);

            float left = textureRectangle.left();
            float right = textureRectangle.right();
            float top = textureRectangle.top();
            float bottom = textureRectangle.bottom();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(left,top);
            GL11.glVertex2f(0,0);
            GL11.glTexCoord2f(right, top);
            GL11.glVertex2f(0, getScale());
            GL11.glTexCoord2f(right,bottom);
            GL11.glVertex2f(getScale(), getScale());
            GL11.glTexCoord2f(left, bottom);
            GL11.glVertex2f(getScale(), 0);
            GL11.glEnd();

            GL11.glTranslatef(0, getScale(), 0);
        }

        GL11.glPopMatrix();
    }

    @Contract("_ -> new")
    private @NotNull TextureRectangle calculateTextureRectangle(char character){
        float charSize = 1f / 16f;
        int x = character % 16;
        int y = character / 16;

        float left = x * charSize;
        float right = left + charSize;
        float top = y * charSize;
        float bottom = top + charSize;

        return new TextureRectangle(top, bottom, left, right);
    }
}
