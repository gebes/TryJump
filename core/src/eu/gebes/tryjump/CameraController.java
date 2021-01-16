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
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector3 vector = camera.direction.cpy();
            vector.y = 0;
            camera.position.add(
                    moveByRaycast(camera.position, vector, Gdx.graphics.getDeltaTime() * Variables.blockSize * 5)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            Vector3 vector = camera.direction.cpy();
            vector.y = 0;
            camera.position.add(
                    moveByRaycast(camera.position, vector, Gdx.graphics.getDeltaTime() * Variables.blockSize * -5)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            camera.position.add(
                    moveByRaycast(camera.position, new Vector3(0, 1, 0), Gdx.graphics.getDeltaTime() * Variables.blockSize * 5)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camera.position.add(
                    moveByRaycast(camera.position, new Vector3(0, -1, 0), Gdx.graphics.getDeltaTime() * Variables.blockSize * 5)
            );
        }
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

        float width = 0f;

        Vector3 playerA = new Vector3(playerX - width, playerY - 1f, playerZ - width);
        Vector3 playerB = new Vector3(playerX + width, playerY + 0.3f, playerZ + width);
        BoundingBox playerBox = new BoundingBox(
                playerA,
                playerB
        );

        BoundingBox playerBoxX = new BoundingBox(
                new Vector3(Float.MIN_VALUE, playerA.y, playerA.z),
                new Vector3(Float.MAX_VALUE, playerB.y, playerB.z)
        );

        BoundingBox playerBoxY = new BoundingBox(
                new Vector3(playerA.x, Float.MIN_VALUE, playerA.z),
                new Vector3(playerB.x, Float.MAX_VALUE, playerB.z)
        );

        BoundingBox playerBoxZ = new BoundingBox(
                new Vector3(playerA.x, playerA.y, Float.MIN_VALUE),
                new Vector3(playerB.x, playerB.y, Float.MAX_VALUE)
        );

        float distanceX = Float.MAX_VALUE;
        float maxY = Float.MAX_VALUE;
        float maxZ = Float.MAX_VALUE;

      /*  for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < length; k++) {
                    int x = playerX - h + i;
                    int y = playerY - h + j;
                    int z = playerZ - h + k;*/

        for (int x = 0; x < Variables.gridSize; x++) {
            for (int y = 0; y < Variables.gridSize; y++) {
                for (int z = 0; z < Variables.gridSize; z++) {
                    Block block = grid.getBlock(x, y, z);

                    if (block == null) continue;

                    Vector3 cubeA = new Vector3(x, y, z);
                    Vector3 cubeB = new Vector3(x + 1, y - 1, z + 1);
                    BoundingBox cubeBox = new BoundingBox(cubeA, cubeB);

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

                            System.out.println(edgeBlock  + " " + edgePlayer);

                            if (edgeBlock >= edgePlayer) {
                                float dis = edgeBlock - edgePlayer;

                                if (dis < distanceX)
                                    distanceX = dis;
                            }


                        }

                  /*      if (translation.x >= 0) {

                            float kanteSpieler = playerBox.getCenterX() + playerBox.getWidth() / 2;
                            float kanteBlock = cubeBox.getCenterX();

                            if (cubeBox.getCenterX() + cubeBox.getWidth()< kanteSpieler)
                                continue;

                            if (kanteSpieler >= kanteBlock) {
                                float dis = kanteBlock - kanteSpieler;

                                if (dis < maxX)
                                    maxX = dis;
                            }

                        } else {


                            float kanteSpieler = playerBox.getCenterX() - playerBox.getWidth() / 2;
                            float kanteBlock = cubeBox.getCenterX() + cubeBox.getWidth();


                            if (cubeBox.getCenterX() - cubeBox.getWidth() > kanteSpieler)
                                continue;

                            if (kanteSpieler < kanteBlock) {
                                float dis = kanteSpieler - kanteBlock;

                                if (dis < maxX)
                                    maxX = dis;
                            }

                        }*/

                    } else if (cubeBox.intersects(playerBoxY) && false) {
                        if (translation.y >= 0) {

                            float kanteSpieler = playerBox.getCenterY() + playerBox.getHeight() / 2;
                            float kanteBlock = cubeBox.getCenterY();

                            if (cubeBox.getCenterY() + cubeBox.getHeight() / 2 < kanteSpieler)
                                continue;

                            if (kanteSpieler >= kanteBlock) {
                                float dis = kanteBlock - kanteSpieler;

                                if (dis < maxY)
                                    maxY = dis;
                            }

                        } else {

                            float kanteSpieler = playerBox.getCenterY() - playerBox.getHeight() / 2;
                            float kanteBlock = cubeBox.getCenterY() + cubeBox.getHeight();


                            if (cubeBox.getCenterY() - cubeBox.getHeight() > kanteSpieler)
                                continue;

                            if (kanteSpieler < kanteBlock) {
                                float dis = kanteSpieler - kanteBlock;

                                if (dis < maxY)
                                    maxY = dis;
                            }

                        }

                    } else if (cubeBox.intersects(playerBoxZ) && false) {
                        if (translation.z >= 0) {

                            float kanteSpieler = playerB.z;
                            float kanteBlock = cubeA.z;

                            if (kanteSpieler < kanteBlock) {
                                float dis = kanteBlock - kanteSpieler;

                                if (dis < maxZ)
                                    maxZ = dis;
                            }

                        } else {


                            float kanteSpieler = playerB.z;
                            float kanteBlock = cubeA.z;

                            if (kanteSpieler >= kanteBlock) {
                                float dis = kanteSpieler - kanteBlock;

                                if (dis < maxZ)
                                    maxZ = dis;
                            }

                        }
                    }

                }
            }
        }


        if (translation.x >= 0) {
            if (translation.x > distanceX)
                translation.x = distanceX;
        } else {
            if (translation.x < -distanceX)
                translation.x = -distanceX;
        }

        if (translation.y >= 0) {
            if (translation.y > maxY)
                translation.y = maxY;
        } else {
            if (translation.y < -maxY)
                translation.y = -maxY;
        }


        if (translation.z >= 0) {
            if (translation.z > maxZ)
                translation.z = maxZ;
        } else {
            if (translation.z < -maxZ)
                translation.z = -maxZ;
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
