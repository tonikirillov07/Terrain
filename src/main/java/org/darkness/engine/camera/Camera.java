package org.darkness.engine.camera;

import org.darkness.Constants;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.sounds.Footsteps;
import org.darkness.engine.utils.transform.Directions;
import org.darkness.engine.utils.transform.Rotation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private final Vector3f position, startPosition, rotation;
    private Rotation rotationX, rotationY;
    private float angleX, angleY;
    private final Vector3f directionForward;
    private boolean isMoving = false;
    private final Footsteps footsteps;

    public Camera(Vector3f position, @NotNull Vector3f rotation) {
        this.position = position;
        this.startPosition = position;
        this.rotation = rotation;

        footsteps = new Footsteps();
        directionForward = new Vector3f();

        rotationX = new Rotation(rotation.getX(), 1, 0, 0);
        rotationY = new Rotation(rotation.getY(), 0, 1, 0);
    }

    public void update(){
        try {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GLU.gluPerspective(70F, (float) Display.getWidth() / Display.getHeight(), 0.05f, 100f);

            GL11.glRotatef(rotationX.angle(), 1, 0, 0);
            GL11.glRotatef(rotationY.angle(), 0, 1, 0);

            GL11.glTranslatef(position.getX(), position.getY(), position.getZ());
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public void control(float deltaTime){
        try {
            angleX = (float) Math.toRadians(rotationX.angle());
            angleY = (float) Math.toRadians(rotationY.angle());

            rotate(deltaTime);
            move(deltaTime);
            playStepsSounds(deltaTime);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void playStepsSounds(float deltaTime) {
        if(isMoving) footsteps.play(deltaTime);
    }

    private void move(float deltaTime) {
        try {
            byte moveType = Keyboard.isKeyDown(Keyboard.KEY_W) ? Constants.MOVE_FORWARD : Keyboard.isKeyDown(Keyboard.KEY_S) ? Constants.MOVE_BACKWARD :
                    Keyboard.isKeyDown(Keyboard.KEY_A) ? Constants.MOVE_LEFT : Keyboard.isKeyDown(Keyboard.KEY_D) ? Constants.MOVE_RIGHT :
                            Keyboard.isKeyDown(Keyboard.KEY_Q) ? Constants.MOVE_UP : Keyboard.isKeyDown(Keyboard.KEY_E) ? Constants.MOVE_DOWN : Constants.DONT_MOVE;

            isMoving = moveType != Constants.DONT_MOVE;

            switch (moveType) {
                case Constants.MOVE_FORWARD -> moveForward(Constants.MOVE_FORWARD, deltaTime);
                case Constants.MOVE_BACKWARD -> moveForward(Constants.MOVE_BACKWARD, deltaTime);
                case Constants.MOVE_LEFT -> moveLeft(Constants.MOVE_LEFT, deltaTime);
                case Constants.MOVE_RIGHT -> moveLeft(Constants.MOVE_RIGHT, deltaTime);

                case Constants.MOVE_UP -> moveUp((byte) 1, deltaTime);
                case Constants.MOVE_DOWN -> moveUp((byte) -1, deltaTime);
            }
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public boolean isMoving() {
        return isMoving;
    }

    private void moveLeft(byte direction, float deltaTime){
        translate(new Vector3f((float) ((float) (direction == Constants.MOVE_LEFT ? 1: -1) * Math.cos(angleX) * deltaTime * Constants.DEFAULT_MOVE_SPEED), (float) 0, (float) ((float) (direction == Constants.MOVE_LEFT ? 1: -1) * Math.sin(angleY) * deltaTime * Constants.DEFAULT_MOVE_SPEED)));
    }

    private void moveForward(byte direction, float deltaTime){
        try {
            directionForward.setX((direction == Constants.MOVE_FORWARD ? -1: 1) * (float) (Math.sin(angleY) * Math.cos(angleX) * deltaTime * Constants.DEFAULT_MOVE_SPEED));
            directionForward.setZ((direction == Constants.MOVE_BACKWARD ? -1: 1) * (float) (Math.cos(angleY) * Math.cos(angleX) * deltaTime * Constants.DEFAULT_MOVE_SPEED));

            translate(directionForward);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void moveUp(byte direction, float deltaTime){
        try {
            translate(new Vector3f(Directions.UP.x, direction * Directions.UP.y * deltaTime * Constants.DEFAULT_MOVE_SPEED, Directions.UP.z));
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void rotate(float deltaTime){
        try {
            int deltaX = Mouse.getDX();
            int deltaY = Mouse.getDY();

            rotationX = new Rotation((rotationX.angle() + -deltaY * deltaTime * Constants.DEFAULT_MOUSE_SENSITIVITY), 1, 0, 0);
            rotationY = new Rotation((rotationY.angle() + deltaX * deltaTime * Constants.DEFAULT_MOUSE_SENSITIVITY), 0, 1, 0);

            if (rotationX.angle() >= Constants.LOOK_X_LIMIT) rotationX = new Rotation(Constants.LOOK_X_LIMIT, 1, 0, 0);
            if (rotationX.angle() <= -Constants.LOOK_X_LIMIT)
                rotationX = new Rotation(-Constants.LOOK_X_LIMIT, 1, 0, 0);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public void resetTransform(){
        setPosition(startPosition);
        rotationX = Rotation.IDENTITY;
        rotationY = Rotation.IDENTITY;
    }

    public void setPosition(@NotNull Vector3f position){
        this.position.set(position.x, position.y, position.z);
    }

    public void translate(@NotNull Vector3f vector3f){
        position.translate(vector3f.x, vector3f.y, vector3f.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
