package eu.gebes.tryjump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.map.WorldLoadManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CameraController extends FirstPersonCameraController {

    Block.Type selectedBlock = Block.Type.Stone;
    final Camera camera;
    final Grid grid;

    public CameraController(Camera camera, Grid grid) {
        super(camera);
        this.camera = camera;
        this.grid = grid;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            grid.editBoxByRayCast(camera.position, camera.direction, null);
        } else if (button == 1) {
            grid.editBoxByRayCast(camera.position, camera.direction, selectedBlock);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    Vector3 velocity = Vector3.Zero;
    float gravity = 9.81f;
    float movementSpeed = 16f;
    boolean canJump = false;
    float floorDistance = 1000;

    @Override
    public void update() {
        float dt = Gdx.graphics.getDeltaTime();

        velocity.scl(0.9f * dt, 1, 0.9f * dt);


        Vector3 camDir = camera.direction.cpy();
        camDir.y = 0;
        camDir.nor();

        Vector3 camRight = new Vector3(camDir.z, 0f, -camDir.x);

        float val = movementSpeed * dt;

        float v = val;
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            v *= 1.98123;
        Vector3 newVel = new Vector3(velocity);
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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canJump && floorDistance < 0.5) {
            canJump = false;
            newVel.y = 2;
        }

        newVel.sub(0, (gravity/2)* dt, 0);

        velocity = newVel;

        Vector3 t = moveBy(velocity);
        camera.position.add(
                t
        );

        if (camera.position.x < 0)
            camera.position.x = 0;
        if (camera.position.y < Variables.blockSize) {
            camera.position.y = Variables.blockSize;
            if(velocity.y <= 0){
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


        super.update();
    }

    public Vector3 moveBy(Vector3 translation) {

        int length = 9;
        int h = length / 2;

        float width = 0.4f;
        float extension = 0.01f;


        Vector3 player = getCameraWorldPosition();

        Vector3 playerA = new Vector3(player.x - width, player.y - 1.4f, player.z - width);
        Vector3 playerB = new Vector3(player.x + width, player.y + 0.4f, player.z + width);


        BoundingBox playerBoxX = new BoundingBox(
                new Vector3(Float.MIN_VALUE, playerA.y, playerA.z - extension),
                new Vector3(Float.MAX_VALUE, playerB.y, playerB.z + extension)
        );

        BoundingBox playerBoxY = new BoundingBox(
                new Vector3(playerA.x - extension, Float.MIN_VALUE, playerA.z - extension),
                new Vector3(playerB.x + extension, Float.MAX_VALUE, playerB.z + extension)
        );

        BoundingBox playerBoxZ = new BoundingBox(
                new Vector3(playerA.x - extension, playerA.y, Float.MIN_VALUE),
                new Vector3(playerB.x + extension, playerB.y, Float.MAX_VALUE)
        );

        float distanceX = Float.MAX_VALUE;
        float distanceY = Float.MAX_VALUE;
        float distanceZ = Float.MAX_VALUE;

        List<BoundingBox> blocks = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < length; k++) {
                    int x = (int) (player.x - h + i);
                    int y = (int) (player.y - h + j);
                    int z = (int) (player.z - h + k);

      /*  for (int x = 0; x < Variables.gridSize; x++) {
            for (int y = 0; y < Variables.gridSize; y++) {
                for (int z = 0; z < Variables.gridSize; z++) {*/
                    Block block = grid.getBlock(x, y, z);

                    if (block == null) continue;
                    Vector3 cubeA = new Vector3(x, y, z);
                    Vector3 cubeB = new Vector3(x + 1, y + 1, z + 1);
                    BoundingBox cubeBox = new BoundingBox(cubeA, cubeB);
                    blocks.add(cubeBox);

                    if (cubeBox.intersects(playerBoxX)) {

                        if (translation.x < 0) {
                            float edgeBlock = cubeB.x;
                            float edgePlayer = playerA.x;

                            if (edgeBlock <= edgePlayer) {
                                float dis = edgePlayer - edgeBlock;

                                if (dis < distanceX)
                                    distanceX = dis;
                            }

                        } else {


                            float edgeBlock = cubeA.x;
                            float edgePlayer = playerB.x;


                            if (edgeBlock >= edgePlayer) {
                                float dis = edgeBlock - edgePlayer;

                                if (dis < distanceX)
                                    distanceX = dis;
                            }


                        }

                    } else if (cubeBox.intersects(playerBoxY)) {
                        if (translation.y < 0) {
                            float edgeBlock = cubeB.y;
                            float edgePlayer = playerA.y;

                            if (edgeBlock <= edgePlayer) {
                                float dis = edgePlayer - edgeBlock;

                                if (dis < distanceY)
                                    distanceY = dis;
                            }

                        } else {

                            float edgeBlock = cubeA.y;
                            float edgePlayer = playerB.y;


                            if (edgeBlock >= edgePlayer) {
                                float dis = edgeBlock - edgePlayer;

                                if (dis < distanceY)
                                    distanceY = dis;
                            }

                        }

                    } else if (cubeBox.intersects(playerBoxZ)) {
                        if (translation.z < 0) {
                            float edgeBlock = cubeB.z;
                            float edgePlayer = playerA.z;

                            if (edgeBlock <= edgePlayer) {
                                float dis = edgePlayer - edgeBlock;

                                if (dis < distanceZ)
                                    distanceZ = dis;
                            }

                        } else {

                            float edgeBlock = cubeA.z;
                            float edgePlayer = playerB.z;


                            if (edgeBlock >= edgePlayer) {
                                float dis = edgeBlock - edgePlayer;

                                if (dis < distanceZ)
                                    distanceZ = dis;
                            }

                        }
                    }

                }
            }
        }


        if (translation.x >= 0) {
            if (translation.x > distanceX)
                translation.x = distanceX - extension;
        } else {
            if (translation.x < -distanceX)
                translation.x = -distanceX + extension;
        }

        if (translation.y >= 0) {
            if (translation.y > distanceY) {
                velocity.y = distanceY - extension;
                //velocity.y = 0;
            }
        } else {
            if (translation.y < -distanceY) {
                velocity.y = -distanceY + extension;
               // velocity.y = 0;
                floorDistance = distanceY;
                canJump = true;
            }
        }


        if (translation.z >= 0) {
            if (translation.z > distanceZ)
                translation.z = distanceZ - extension;
        } else {
            if (translation.z < -distanceZ)
                translation.z = -distanceZ + extension;
        }

        Vector3 testA = playerA.cpy().add(translation);
        Vector3 testB = playerB.cpy().add(translation);
        BoundingBox testBox = new BoundingBox(
                testA,
                testB
        );

        for (BoundingBox block : blocks) {
            if (block.intersects(testBox)) {
                translation = Vector3.Zero;
                break;
            }
        }

        player.scl(Variables.blockSize);

        return translation;
    }

    public Vector3 getCameraWorldPosition() {
        Vector3 p = camera.position.cpy();
        p.scl(1f / Variables.blockSize);
        p.add(0.5f, 0.5f, 0.5f);
        return p;
    }

    public Vector3 getCameraPosition() {
        Vector3 p = camera.position.cpy();
        p.add(Variables.blockSize, 0, Variables.blockSize);
        return p;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        touchDragged(screenX, screenY, 0);
        return super.mouseMoved(screenX, screenY);
    }

    WorldLoadManager worldLoadManager = new WorldLoadManager();

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            worldLoadManager.saveMap(Grid.blocks);
            Gdx.app.exit();
        }

        if (keycode == Input.Keys.NUM_1) {
            selectedBlock = Block.Type.Dirt;
        }
        if (keycode == Input.Keys.NUM_2) {
            selectedBlock = Block.Type.Stone;
        }
        if (keycode == Input.Keys.NUM_3) {
            selectedBlock = Block.Type.Log;
        }
        if (keycode == Input.Keys.NUM_4) {
            selectedBlock = Block.Type.Planks;
        }
        if (keycode == Input.Keys.NUM_5) {
            selectedBlock = Block.Type.Leaves;
        }
        if (keycode == Input.Keys.NUM_0) {
            selectedBlock = Block.Type.Bedrock;
        }
        return false;
    }

}
