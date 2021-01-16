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
        super.update();
    }

    public Vector3 moveByRaycast(Vector3 startPoint, Vector3 direction, float movement) {

        Vector3 tmpStart = new Vector3(startPoint);
        Vector3 tmpDirection = new Vector3(direction);
        tmpDirection.nor();
        tmpDirection.scl(movement);
        Vector3 p = tmpStart.cpy();
        p.scl(1f / Variables.blockSize);
        float playerX = p.x;
        float playerY = p.y;
        float playerZ = p.z;

        tmpStart.add(tmpDirection);


        Vector3 translation = new Vector3(tmpStart);
        translation.set(direction);
        translation.scl(movement);


        int length = 9;
        int h = length / 2;

        p.scl(1f / Variables.blockSize);

        float width = 0.2f;

        float pa = 0.001f;

        Vector3 playerA = new Vector3(playerX - width, playerY - 1f, playerZ - width);
        Vector3 playerB = new Vector3(playerX + width, playerY + 1f, playerZ + width);
        BoundingBox playerBox = new BoundingBox(
                playerA,
                playerB
        );

        BoundingBox playerBoxX = new BoundingBox(
                new Vector3(Float.MIN_VALUE, playerA.y - pa, playerA.z - pa),
                new Vector3(Float.MAX_VALUE, playerB.y + pa, playerB.z + pa)
        );

        BoundingBox playerBoxY = new BoundingBox(
                new Vector3(playerA.x - pa, Float.MIN_VALUE, playerA.z - pa),
                new Vector3(playerB.x + pa, Float.MAX_VALUE, playerB.z + pa)
        );

        BoundingBox playerBoxZ = new BoundingBox(
                new Vector3(playerA.x - pa, playerA.y - pa, Float.MIN_VALUE),
                new Vector3(playerB.x + pa, playerB.y + pa, Float.MAX_VALUE)
        );

        float distanceX = Float.MAX_VALUE;
        float distanceY = Float.MAX_VALUE;
        float distanceZ = Float.MAX_VALUE;

        List<BoundingBox> blocks = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < length; k++) {
                    int x = (int) (playerX - h + i);
                    int y = (int) (playerY - h + j);
                    int z = (int) (playerZ - h + k);

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
                            float edgePlayer = playerB.x + 1;


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
                            float edgePlayer = playerB.z + 1;


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
                translation.x = distanceX - pa;
        } else {
            if (translation.x < -distanceX)
                translation.x = -distanceX + pa;
        }

        if (translation.y >= 0) {
            if (translation.y > distanceY)
                translation.y = distanceY - pa;
        } else {
            if (translation.y < -distanceY)
                translation.y = -distanceY + pa;
        }


        if (translation.z >= 0) {
            if (translation.z > distanceZ)
                translation.z = distanceZ - pa;
        } else {
            if (translation.z < -distanceZ)
                translation.z = -distanceZ + pa;
        }

        Vector3 testA = new Vector3(playerX + translation.x - width, playerY + translation.y - 1f, playerZ + translation.z - width);
        Vector3 testB = new Vector3(playerX + translation.x + width, playerY + translation.y + 1f, playerZ + translation.z + width);
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

        p.scl(Variables.blockSize);

        return translation;
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
