package org.darkness.engine.camera;

import org.darkness.Constants;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.models.Cube;
import org.darkness.engine.models.Model;
import org.darkness.engine.sounds.Footsteps;
import org.darkness.engine.utils.transform.Directions;
import org.darkness.engine.utils.transform.Rotation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Random;

import static org.darkness.Constants.ACCELERATION_OF_GRAVITY;
import static org.darkness.Constants.FOV_Y;

public class Camera {
    private final Vector3f position, startPosition, rotation;
    private Rotation rotationX, rotationY;
    private float angleX, angleY;
    private boolean isMoving = false;
    private final Footsteps footsteps;
    private float deltaTime = 0, currentSpeed = 0;
    private boolean isOnGround, hasCollisionWithBlock, doGravity = true;
    private Model standingModel;
    private final List<Model> allModels;

    public Camera(Vector3f position, @NotNull Vector3f rotation, List<Model> allModels) {
        this.position = position;
        this.startPosition = position;
        this.rotation = rotation;
        this.allModels = allModels;

        footsteps = new Footsteps();

        rotationX = new Rotation(rotation.getX(), 1, 0, 0);
        rotationY = new Rotation(rotation.getY(), 0, 1, 0);

        initCameraShaking();
    }

    private void initCameraShaking() {
        new Thread(() -> {
            while(true) {
                for (double i = -Constants.CAMERA_SHAKE_VALUES_LIMIT; i < Constants.CAMERA_SHAKE_VALUES_LIMIT; i += 0.1) {
                    shakeCamera(i);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (double i = Constants.CAMERA_SHAKE_VALUES_LIMIT; i > -Constants.CAMERA_SHAKE_VALUES_LIMIT; i -= 0.1) {
                    shakeCamera(i);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    private void shakeCamera(double angle){
        if(!isMoving()) {
            rotationX = new Rotation((float) (rotationX.angle() + (Math.sin(angle * deltaTime))), 1, 0, 0);
            rotationY = new Rotation((float) (rotationY.angle() + (Math.sin(angle * deltaTime))), 0, 1, 0);
        }
    }

    public void update(){
        try {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GLU.gluPerspective(FOV_Y, (float) Display.getWidth() / Display.getHeight(), 0.05f, Constants.Z_FAR);

            GL11.glRotatef(rotationX.angle(), 1, 0, 0);
            GL11.glRotatef(rotationY.angle(), 0, 1, 0);

            GL11.glTranslatef(position.getX(), position.getY(), position.getZ());
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    public void control(float deltaTime){
        try {
            this.deltaTime = deltaTime;

            angleX = (float) Math.toRadians(rotationX.angle());
            angleY = (float) Math.toRadians(rotationY.angle());

            rotate(deltaTime);
            move(deltaTime);
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void playStepsSounds(float deltaTime, float currentSpeed) {
        if(isMoving & isOnGround() & standingModel != null) footsteps.play(deltaTime, currentSpeed, standingModel.getStepSounds());
    }

    private void move(float deltaTime) {
        try {
            byte moveType = Keyboard.isKeyDown(Keyboard.KEY_W) ? Constants.MOVE_FORWARD : Keyboard.isKeyDown(Keyboard.KEY_S) ? Constants.MOVE_BACKWARD :
                    Keyboard.isKeyDown(Keyboard.KEY_A) ? Constants.MOVE_LEFT : Keyboard.isKeyDown(Keyboard.KEY_D) ? Constants.MOVE_RIGHT :
                            Keyboard.isKeyDown(Keyboard.KEY_Q) ? Constants.MOVE_UP : Keyboard.isKeyDown(Keyboard.KEY_E) ? Constants.MOVE_DOWN : Constants.DONT_MOVE;

            isMoving = moveType != Constants.DONT_MOVE;
            currentSpeed =  Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? Constants.DEFAULT_MOVE_SPEED * 2f: Constants.DEFAULT_MOVE_SPEED;

            if(isMoving){
                checkForCollision(allModels, position);

                if(moveType != Constants.MOVE_UP & moveType != Constants.MOVE_DOWN) playStepsSounds(deltaTime, currentSpeed);
            }

            switch (moveType) {
                case Constants.MOVE_FORWARD -> moveForward(Constants.MOVE_FORWARD, deltaTime);
                case Constants.MOVE_BACKWARD -> moveForward(Constants.MOVE_BACKWARD, deltaTime);
                case Constants.MOVE_LEFT -> moveLeft(Constants.MOVE_LEFT, deltaTime);
                case Constants.MOVE_RIGHT -> moveLeft(Constants.MOVE_RIGHT, deltaTime);

                case Constants.MOVE_UP -> moveUp((byte) 1, deltaTime);
                case Constants.MOVE_DOWN -> moveUp((byte) -1, deltaTime);
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) jump();
            if(Mouse.isButtonDown(0)){
                int length = 15;

                for (int i = 0; i < length; i++) {
                    Vector3f directionForward  = new Vector3f();
                    directionForward.setX(-(float) (Math.sin(angleY) * Math.cos(angleX) * i));
                    directionForward.setZ((float) (Math.cos(angleY) * Math.cos(angleX) * i));

                    Vector3f positionCopy = new Vector3f(position);
                    Vector3f translatedVector = positionCopy.translate(directionForward.getX(), directionForward.getY(), directionForward.getZ());

                    boolean aboolean = false;

                    for (Model allModel : allModels) {
                        aboolean = (allModel.checkForAnotherCollision(translatedVector));
                        if (aboolean) {
                            //allModel.setColor(new Color(255, 0, 0));
                            allModels.add(new Cube(new Vector3f(allModel.getPosition().x, allModel.getPosition().y + 1, allModel.getPosition().z), Rotation.IDENTITY, new Color(255, 255, 255), 0, 1f));

                            break;
                        }
                    }

                    if(aboolean) break;
                }

            }
        }catch (Exception e) {
            Logs.makeErrorLog(e);
        }
    }

    private void jump() {
        if(isOnGround()){
            new Thread(() -> {
                doGravity = false;

                for (double i = 0f; i <= 1.8f; i+=0.1) {
                    float y  = (float) (-Math.pow(i, 2) + Math.pow(i * 1.3f, 2) + 1);

                    translate(new Vector3f(0, -y * deltaTime, 0));
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (double i = 1.8f; i >= 0; i-=0.1) {
                    float y  = (float) (-Math.pow(i, 2) + Math.pow(i * 1.3f, 2) + 1);

                    translate(new Vector3f(0, -y * deltaTime, 0));
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                doGravity = true;
            }).start();
        }
    }

    public boolean isMoving() {
        return isMoving;
    }

    private void moveLeft(byte direction, float deltaTime){
        float angle = rotationY.angle() + (90 * (direction == Constants.MOVE_LEFT ? -1: 1));
        float speed = currentSpeed * deltaTime;

        float newZ = speed * (float) Math.cos(Math.toRadians(angle));
        float newX =  speed * (float) Math.sin(Math.toRadians(angle));

        Vector3f positionCopy = new Vector3f(position);
        positionCopy.z += newZ;
        positionCopy.x -= newX;

        boolean aboolean = false;

        for (Model allModel : allModels) {
            aboolean = (allModel.checkForAnotherCollision(positionCopy));
            if (aboolean) break;
        }

        if(!aboolean) {
            position.z += speed * (float) Math.cos(Math.toRadians(angle));
            position.x -= speed * (float) Math.sin(Math.toRadians(angle));
        }
    }

    private void moveForward(byte direction, float deltaTime){
        try {
            currentSpeed = (direction == Constants.MOVE_BACKWARD ? currentSpeed / 1.4f: currentSpeed) * deltaTime;

            Vector3f directionForward  = new Vector3f();
            directionForward.setX((direction == Constants.MOVE_FORWARD ? -1: 1) * (float) (Math.sin(angleY) * Math.cos(angleX) * currentSpeed));
            directionForward.setZ((direction == Constants.MOVE_BACKWARD ? -1: 1) * (float) (Math.cos(angleY) * Math.cos(angleX) * currentSpeed));

            Vector3f positionCopy = new Vector3f(position);
            Vector3f translatedVector = positionCopy.translate(directionForward.getX(), directionForward.getY(), directionForward.getZ());

            boolean aboolean = false;

            for (Model allModel : allModels) {
                aboolean = (allModel.checkForAnotherCollision(translatedVector));
                if (aboolean) break;
            }


           if(!aboolean) translate(directionForward);
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

    @Contract("_, _ -> new")
    public static @NotNull Vector3f getRandomPosition(float worldLength, float worldWidth){
        return new Vector3f(-new Random().nextFloat(worldWidth), -5.4447231f, -new Random().nextFloat(worldLength));
    }

    public void checkIsOnGround(@NotNull List<Model> modelList){
        for (Model model : modelList) {
            isOnGround = model.checkForPlayerStandingOnModel(this);
            if(isOnGround) standingModel = model;

            if(isOnGround) break;
        }
    }

    public void checkForCollision(@NotNull List<Model> modelList, Vector3f direction){
        for (Model model : modelList) {
            hasCollisionWithBlock = model.checkForAnotherCollision(direction);

            if(hasCollisionWithBlock) break;
        }
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public void applyGravity(){
        if(!isOnGround()){
            if(doGravity) translate(new Vector3f(0f, ACCELERATION_OF_GRAVITY * deltaTime, 0f));
        }
    }

    public void translate(@NotNull Vector3f vector3f){
        position.translate(vector3f.x, vector3f.y, vector3f.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Rotation getRotationX() {
        return rotationX;
    }

    public Rotation getRotationY() {
        return rotationY;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
