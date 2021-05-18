package eu.gebes.tryjump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.map.MapManagment;
import eu.gebes.tryjump.map.WorldLoadManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerController extends FirstPersonCameraController {

    Block.Type selectedBlock = Block.Type.Stone;
    Camera camera;
    Grid grid = null;
    StopWatch stopWatch = new StopWatch();
    MapManagment mapManagment = new MapManagment();
    Player player;
    WorldLoadManager worldLoadManager = new WorldLoadManager();

    public PlayerController(Camera camera, Grid grid) {
        super(camera);
        this.camera = camera;
        this.grid = grid;
        stopWatch.start();
        player = new Player(grid, camera, stopWatch);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Variables.create) {
            if (button == 0) {
                grid.editBoxByRayCast(camera.position, camera.direction, null);
            } else if (button == 1) {
                grid.editBoxByRayCast(camera.position, camera.direction, selectedBlock);
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void update() {
        float dt = Gdx.graphics.getDeltaTime();

        player.getVelocity().scl(0.9f * dt, 1, 0.9f * dt);

        Vector3 camDir = camera.direction.cpy();
        camDir.y = 0;
        camDir.nor();

        Vector3 camRight = new Vector3(camDir.z, 0f, -camDir.x);

        float val = player.getMovementSpeed() * dt;

        float v = val;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            v *= 1.98123;
        Vector3 newVel = new Vector3(player.getVelocity());
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newVel.add(camDir.scl(v));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newVel.sub(camDir.scl(val));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newVel.add(camRight.scl(v));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newVel.sub(camRight.scl(v));
        }
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
                if (proofJump(getCameraWorldPosition())) {
                    player.setCanJump(false);
                    newVel.y = 0.98f;
                }
            } else {
                player.setCanJump(true);
                newVel.y = 0.2f;
            }
        }

        if (!Variables.create)
            newVel.sub(0, 9.8f * 0.5f * dt, 0);

        player.setVelocity(newVel);

        camera.position.add(player.moveBy(player.getVelocity(),  getCameraWorldPosition()));

        if (camera.position.x < 0)
            camera.position.x = 0;
        if (camera.position.y < Variables.blockSize) {
            camera.position.y = Variables.blockSize;
            if (player.getVelocity().y <= 0) {
                player.getVelocity().y = 0;
            }
        }
        if (camera.position.z < 0)
            camera.position.z = 0;

        if (camera.position.x > Variables.gridWidth * Variables.blockSize - Variables.blockSize)
            camera.position.x = Variables.gridWidth * Variables.blockSize - Variables.blockSize;
        if (camera.position.y > Variables.gridHeight * Variables.blockSize - Variables.blockSize) {
            camera.position.y = Variables.gridHeight * Variables.blockSize - Variables.blockSize;
            player.getVelocity().y = 0;
        }
        if (camera.position.z > Variables.gridDepth * Variables.blockSize - Variables.blockSize)
            camera.position.z = Variables.gridDepth * Variables.blockSize - Variables.blockSize;


        super.update();
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        touchDragged(screenX, screenY, 0);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            stopGame(3);
        }
        if (keycode == Input.Keys.NUM_1) {
            selectedBlock = Block.Type.Dirt;
        }
        if (keycode == Input.Keys.NUM_2) {
            selectedBlock = Block.Type.Grass;
        }
        if (keycode == Input.Keys.NUM_3) {
            selectedBlock = Block.Type.Log;
        }
        if (keycode == Input.Keys.NUM_4) {
            selectedBlock = Block.Type.Planks;
        }
        if (keycode == Input.Keys.NUM_5) {
            selectedBlock = Block.Type.Stone;
        }
        if (keycode == Input.Keys.NUM_6) {
            selectedBlock = Block.Type.CleanStone;
        }
        if (keycode == Input.Keys.NUM_7) {
            selectedBlock = Block.Type.Brick;
        }
        if (keycode == Input.Keys.NUM_8) {
            selectedBlock = Block.Type.Leaves;
        }
        if (keycode == Input.Keys.NUM_9) {
            selectedBlock = Block.Type.Gold;
        }
        if (keycode == Input.Keys.NUM_0) {
            selectedBlock = Block.Type.Bedrock;
        }
        if (keycode == Input.Keys.ALT_RIGHT) {
            if (Variables.create) {
                Vector3 position = getCameraWorldPosition();
                grid.setBlock((int) position.x, (int) position.y - 2, (int) position.z, Block.Type.Diamond);
                Variables.endX = (int) position.x;
                Variables.endY = (int) position.y;
                Variables.endZ = (int) position.z;
                stopGame(1);
            }
        }
        return false;
    }

    public Vector3 getCameraWorldPosition() {
        Vector3 p = camera.position.cpy();
        p.scl(1f / Variables.blockSize);
        p.add(0.5f);
        return p;
    }

    void stopGame(int ID) {
        if (ID == 1) {
            worldLoadManager.saveMap(Grid.blocks);
            mapManagment.save();
        }

        Gdx.app.exit();
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
}