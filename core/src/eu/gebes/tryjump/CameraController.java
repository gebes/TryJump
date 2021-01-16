package eu.gebes.tryjump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import eu.gebes.tryjump.blocks.Block;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CameraController extends FirstPersonCameraController {

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
            grid.editBoxByRayCast(camera.position, camera.direction, Block.Type.Dirt);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }


    @Override
    public void update() {
        float dt = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector3 vector = camera.direction.cpy();
            vector.y = 0;
            camera.position.add(
                    moveByRaycast(camera.position, vector, dt * Variables.blockSize * 5)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            Vector3 vector = camera.direction.cpy();
            vector.y = 0;
            camera.position.add(
                    moveByRaycast(camera.position, vector, dt * Variables.blockSize * -5)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            camera.position.add(
                    moveByRaycast(camera.position, new Vector3(0, 2, 0), dt * Variables.blockSize * 5)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camera.position.add(
                    moveByRaycast(camera.position, new Vector3(0, -1, 0), dt * Variables.blockSize * 5)
            );
        }

        camera.position.add(
                moveByRaycast(camera.position, new Vector3(0, -1, 0), dt * Variables.blockSize * 5)
        );

        if (camera.position.x < 0)
            camera.position.x = 0;
        if (camera.position.y < Variables.blockSize)
            camera.position.y = Variables.blockSize;
        if (camera.position.z < 0)
            camera.position.z = 0;

        if (camera.position.x > Variables.gridWidth * Variables.blockSize)
            camera.position.x = Variables.gridWidth * Variables.blockSize;
        if (camera.position.y > Variables.gridHeight * Variables.blockSize - Variables.blockSize)
            camera.position.y = Variables.gridHeight * Variables.blockSize - Variables.blockSize;
        if (camera.position.z > Variables.gridDepth * Variables.blockSize)
            camera.position.z = Variables.gridDepth * Variables.blockSize;


        super.update();
    }

    public Vector3 moveByRaycast(Vector3 startPoint, Vector3 direction, float movement) {

        Vector3 tmpStart = new Vector3(startPoint);
        Vector3 tmpDirection = new Vector3(direction);
        tmpDirection.nor();
        tmpDirection.scl(movement);

        tmpStart.add(tmpDirection);


        Vector3 translation = new Vector3(tmpStart);
        translation.set(direction);
        translation.scl(movement);


        int length = 9;
        int h = length / 2;


        float width = 0.4f;
        float extension = 0.01f;


        Vector3 player = getCameraWorldPosition();

        Vector3 playerA = new Vector3(player.x - width, player.y - 1.2f, player.z - width);
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
            if (translation.y > distanceY)
                translation.y = distanceY - extension;
        } else {
            if (translation.y < -distanceY)
                translation.y = -distanceY + extension;
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
        p.add(0.5f, 0, 0.5f);
        return p;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        touchDragged(screenX, screenY, 0);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        return false;
    }

}
