package eu.gebes.tryjump.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import eu.gebes.tryjump.Grid;
import eu.gebes.tryjump.TryJump;
import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementController extends FirstPersonCameraController {

    Player player;
    Camera camera;
    Grid grid;


    public MovementController(Camera camera, Player player) {
        super(camera);
        this.player = player;
        this.camera = camera;
        this.grid = player.getGrid();
    }

    Vector3 velocity = Vector3.Zero;
    boolean grounded = false;
    float playerSpeed = 2.0f * Variables.blockSize;
    float jumpHeight = 1.0f;
    float gravityValue = -9.81f;
    float maxSpeed = 3f;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Variables.levelEditorModeEnabled) {
            if (button == 0) {
                player.getGrid().editBoxByRayCast(player.getWorldPosition().sub(0.5f), camera.direction, null);
            } else if (button == 1) {
                player.getGrid().editBoxByRayCast(player.getWorldPosition().sub(0.5f), camera.direction, player.getSelectedBuildBlock());
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    boolean won = false;

    @Override
    public void update() {
        float dt = Gdx.graphics.getDeltaTime();

        Vector3 camDir = camera.direction.cpy();
        camDir.y = 0;
        camDir.nor();
        Vector3 camRight = new Vector3(camDir.z, 0f, -camDir.x);

        boolean pressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.add(camDir);
            pressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.sub(camDir);
            pressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.add(camRight);
            pressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.sub(camRight);
            pressed = true;
        }


        {
            Vector3 cpy = velocity.cpy();
            cpy.y = 0;


            float speed = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? maxSpeed * 2 : maxSpeed;

            if (cpy.len() > speed) {
                cpy.setLength(speed);
            }
            if(!pressed)
            cpy.scl(0.1f);
            velocity.x = cpy.x;
            velocity.z = cpy.z;

        }


        if (Variables.levelEditorModeEnabled) {

            velocity.y = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                velocity.y = maxSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                velocity.y = -maxSpeed;

            if (!pressed) {
                velocity.x = 0;
                velocity.z = 0;
            }

        } else {

            Vector3 playerPos = getWorldPosition();
            Block block = grid.getBlock((int) playerPos.x, (int) (playerPos.y - 1.5), (int) playerPos.z);
            if (block != null && grounded && block.getType() == Block.Type.Gold && !won) {
                won = true;
                TryJump.soundManager.won();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(8000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                }, "Won").start();
            }

            if (grounded && velocity.y < 0) {
                velocity.y = 0f;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && grounded) {
                // velocity.y += (float) Math.sqrt(100 * jumpHeight * -3.0f * gravityValue);
                velocity.y = (float) Math.sqrt(jumpHeight * -12.0f * gravityValue);
            }

            velocity.add(0, gravityValue * dt * 4, 0);
        }


        if (won) velocity.setZero();
        velocity.scl(dt);
        camera.position.add(player.moveAndCollide(velocity, getWorldPosition()));
        velocity.scl(1f / dt);
        /*
        if (Variables.create) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                newVel.y = 0.1f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                newVel.y = -0.1f;
            } else {
                newVel.y = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) & player.isCanJump() && player.getFloorDistance() < 0.5) {
            if (!(Variables.create)) {
                if (proofJump(getWorldPosition())) {
                    player.setCanJump(false);
                    newVel.y = 0.98f;
                }
            } else {
                player.setCanJump(true);
                newVel.y = 0.2f;
            }
        }

        if (!Variables.create)
            newVel.sub(0, Variables.gravity * 0.5f * dt, 0);

        player.setVelocity(newVel);

        camera.position.add(player.moveBy(player.getVelocity(), getWorldPosition()));
*/
        if (Variables.levelEditorModeEnabled) {
            if (camera.position.x < 0)
                camera.position.x = 0;
            if (camera.position.y < Variables.blockSize) {
                camera.position.y = Variables.blockSize;
                if (velocity.y <= 0) {
                    velocity.y = 0;
                }
            }
            if (camera.position.z < 0)
                camera.position.z = 0;

            if (camera.position.x > Variables.gridWidth * Variables.blockSize - Variables.blockSize)
                camera.position.x = Variables.gridWidth * Variables.blockSize - Variables.blockSize;
            if (camera.position.y > Variables.gridHeight * Variables.blockSize - Variables.blockSize) {
                camera.position.y = Variables.gridHeight * Variables.blockSize - Variables.blockSize;
                velocity.y = 0;
            }
            if (camera.position.z > Variables.gridDepth * Variables.blockSize - Variables.blockSize)
                camera.position.z = Variables.gridDepth * Variables.blockSize - Variables.blockSize;


        } else {

            if (getWorldPosition().y < 3) {
                player.setPositionToSpawn();
            }
        }


        super.update();
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        touchDragged(screenX, screenY, 0);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keycode) {

        // Select block
        int amountOfBlocks = Block.Type.values().length;
        int currentIndex = Arrays.asList(Block.Type.values()).indexOf(player.getSelectedBuildBlock());


        if (keycode == Input.Keys.UP) {
            currentIndex++;
        } else if (keycode == Input.Keys.DOWN) {
            currentIndex--;
        }

        if (currentIndex < 0) {
            currentIndex = amountOfBlocks - 1;
        }
        if (currentIndex >= amountOfBlocks) {
            currentIndex = 0;
        }

        player.setSelectedBuildBlock(Block.Type.values()[currentIndex]);


        if (keycode == Input.Keys.ENTER && Variables.levelEditorModeEnabled) {
            grid.save();
            player.getMapManagment().save();
            System.exit(0);
        }
        return false;
    }

    boolean proofJump(Vector3 position) {
        if (grid.hasBlock((int) position.x, (int) position.y - 2, (int) position.z)) {
            return true;
        } else if (grid.hasBlock((int) (position.x), (int) position.y - 2, round(position.z))) {
            return true;
        } else if (grid.hasBlock(round(position.x), (int) position.y - 2, (int) (position.z))) {
            return true;
        } else if (grid.hasBlock(round(position.x), (int) position.y - 2, round(position.z))) {
            return true;
        }
        return false;
    }

    int round(float coordinate) {
        float tmp = coordinate % 1;
        double difference = 0.70;
        if (tmp <= difference) {
            return (int) (coordinate - 1);
        } else if (tmp >= (-difference)) {
            return (int) (coordinate + 1);
        }
        return (int) coordinate;
    }

    public Vector3 getWorldPosition() {
        Vector3 p = camera.position.cpy();
        p.scl(1f / Variables.blockSize);
        p.add(0.5f);
        return p;
    }
}
