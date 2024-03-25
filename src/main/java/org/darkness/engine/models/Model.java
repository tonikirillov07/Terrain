package org.darkness.engine.models;

import org.darkness.engine.logs.Logs;
import org.darkness.engine.utils.textures.TexturesUtil;
import org.darkness.engine.utils.transform.Rotation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

public abstract class Model{
    private Vector3f position;
    private Rotation rotation;
    private Color color;
    private int texture;
    private float scale;

    public Model(Vector3f position, Rotation rotation, Color color, int texture, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.color = color;
        this.texture = texture;
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Color getColor() {
        return color;
    }

    public int getTexture() {
        return texture;
    }

    public void applyColor(){
        try{
            GL11.glColor3f((float) getColor().getRed() / 100, (float) getColor().getGreen() / 100, (float) getColor().getBlue() / 100);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void bindTexture(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture());
    }

    public void applyTransform(){
        try {
            GL11.glLoadIdentity();

            GL11.glTranslatef(position.getX(), position.getY(), position.getZ());
            GL11.glRotatef(getRotation().angle(), getRotation().x(), getRotation().y(), getRotation().z());
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public void resetTransform(){
        setPosition(new Vector3f(0,0,0));
        setRotation(Rotation.IDENTITY);
    }

    public void render(){
        try {
            applyColor();
            if (texture != TexturesUtil.NO_TEXTURE) bindTexture();
            applyTransform();
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    @Override
    public String toString() {
        return "Model{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", color=" + color +
                ", texture=" + texture +
                '}';
    }
}
