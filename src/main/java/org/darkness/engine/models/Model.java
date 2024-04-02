package org.darkness.engine.models;

import lombok.Getter;
import lombok.Setter;
import org.darkness.Constants;
import org.darkness.engine.camera.Camera;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.utils.IOnAction;
import org.darkness.engine.utils.textures.TexturesUtil;
import org.darkness.engine.utils.transform.Rotation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;

import static org.darkness.Constants.Z_FAR;
import static org.darkness.Constants.Z_NEAR;

@Getter
public abstract class Model{
    @Setter
    private Vector3f position;
    @Setter
    private Rotation rotation;
    @Setter
    private Color color;
    @Setter
    private int texture;
    @Setter
    private float scale;
    @Setter
    private long id;
    @Setter
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

    public void applyColor(){
        try{
            GL11.glColor3f((float) getColor().getRed() / 100, (float) getColor().getGreen() / 100, (float) getColor().getBlue() / 100);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
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

    public boolean checkForPlayerStandingOnModel(@NotNull Camera camera) {
        if (isDetectCollision()) {
            float x1 = -getPosition().getX();
            float z1 = -getPosition().getZ();
            float y1 = -getPosition().getY();

            float x2 = x1 - scale;
            float z2 = z1 - scale;
            float y2 = y1 - scale;

            float playerY = camera.getPosition().y - Constants.PLAYER_HEIGHT;

            return ((camera.getPosition().x <= x1 & camera.getPosition().x >= x2) & (camera.getPosition().z <= z1 & camera.getPosition().z >= z2) & (playerY <= y1 & playerY >= y2));
        }
        return false;
    }

    public boolean checkForAnotherCollision(@NotNull Vector3f cameraPosition){
        if(isDetectCollision()) {
            float x1 = -getPosition().getX();
            float z1 = -getPosition().getZ();
            float y1 = -getPosition().getY();

            float halfScale = scale / 2;

            float x2 = (x1 - scale) - halfScale;
            float z2 = (z1 - scale) - halfScale;
            float y2 = (y1 - scale) - halfScale;

            float playerY = cameraPosition.y - Constants.PLAYER_HEIGHT;

            return (((cameraPosition.x <= x1 & cameraPosition.x >= x2) & (cameraPosition.z <= z1 & cameraPosition.z >= z2) |
                    (cameraPosition.x - halfScale <= x1 & cameraPosition.x - halfScale >= x2) & (cameraPosition.z - halfScale <= z1 & cameraPosition.z >= z2)) & ((playerY >= y1) & ((playerY > y2 & cameraPosition.y < y2) | (cameraPosition.y <= y1 & cameraPosition.y >= y2))));
        }
        return false;
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

    protected void renderUIElement(@NotNull IOnAction onAction){
        bindTexture();

        double halfWidth = (double) Display.getWidth() / 2;
        double halfHeight = (double) Display.getHeight() / 2;

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-halfWidth, halfWidth, -halfHeight, halfHeight, Z_NEAR, Z_FAR);

        onAction.onAction();

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    protected void initUIElementSettings(){
        setDetectCollision(false);

        if(position.getZ() >= 0) position.setZ(-2f);
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
