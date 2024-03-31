package org.darkness.engine.models;

import org.darkness.Constants;
import org.darkness.engine.camera.Camera;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.utils.textures.TexturesUtil;
import org.darkness.engine.utils.transform.Rotation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;

public abstract class Model{
    private Vector3f position;
    private Rotation rotation;
    private Color color;
    private int texture;
    private float scale;
    private long id;
    private boolean detectCollision = true;
    private File[] stepSounds;

    public Model(Vector3f position, Rotation rotation, Color color, int texture, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.color = color;
        this.texture = texture;
        this.scale = scale;
    }

    public Model(Vector3f position, Rotation rotation, Color color, int texture, float scale, File[] stepSounds) {
        this.position = position;
        this.rotation = rotation;
        this.color = color;
        this.texture = texture;
        this.scale = scale;
        this.stepSounds = stepSounds;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isDetectCollision() {
        return detectCollision;
    }

    public void setDetectCollision(boolean detectCollision) {
        this.detectCollision = detectCollision;
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

    public File[] getStepSounds() {
        return stepSounds;
    }

    public boolean checkForPlayerStandingOnModel(@NotNull Camera camera){
        float x1 = -getPosition().getX();
        float z1 = -getPosition().getZ();
        float y1 = -getPosition().getY();

        float x2 = x1 - scale;
        float z2 = z1 - scale;
        float y2 = y1 - scale;

        float playerY = camera.getPosition().y - Constants.PLAYER_HEIGHT;

        return ((camera.getPosition().x <= x1 & camera.getPosition().x >= x2) & (camera.getPosition().z <= z1 & camera.getPosition().z >= z2) & (playerY <= y1 & playerY >= y2))
                & isDetectCollision();
    }

    public boolean checkForAnotherCollision(@NotNull Vector3f cameraPosition){
        float x1 = -getPosition().getX();
        float z1 = -getPosition().getZ();
        float y1 = -getPosition().getY();

        float halfScale = scale / 2;

        float x2 = (x1 - scale) - halfScale;
        float z2 = (z1 - scale) - halfScale;
        float y2 = (y1 - scale) - halfScale;

        float playerY = cameraPosition.y - Constants.PLAYER_HEIGHT;
        boolean isCollisionFound = (((cameraPosition.x <= x1 & cameraPosition.x >= x2) & (cameraPosition.z <= z1 & cameraPosition.z >= z2) |
                (cameraPosition.x - halfScale <= x1 & cameraPosition.x - halfScale >= x2) & (cameraPosition.z - halfScale <= z1 & cameraPosition.z >= z2)) & ((playerY >= y1) & ((playerY > y2 & cameraPosition.y < y2) | (cameraPosition.y <= y1 & cameraPosition.y >= y2))));

        return isCollisionFound & isDetectCollision();
    }

    public void tpCamera(Camera camera){
        float x1 = -getPosition().getX();
        float z1 = -getPosition().getZ();
        float y1 = -getPosition().getY();

        float x2 = (x1 - scale) - scale / 2;
        float z2 = (z1 - scale) - scale / 2;
        float y2 = (y1 - scale) - scale / 2;

        float z3 = (z2 - scale) + scale / 2;
        float x3 = (x2 - scale) + scale / 2;

        camera.setPosition(new Vector3f(x3, 0, z3));
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
                ", id=" + id +
                '}';
    }
}
