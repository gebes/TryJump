package eu.gebes.tryjump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.map.MapManagment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player {
    @Setter
    Vector3 velocity = Vector3.Zero;
    float gravity;
    float movementSpeed;
    final Grid grid;
    @Setter
    boolean canJump = false;
    float floorDistance = 1000;
    Camera camera;
    StopWatch stopWatch;
    MapManagment mapManagment = new MapManagment();

    public Player(Grid grid, Camera camera, StopWatch stopWatch) {
        this.grid = grid;
        this.camera = camera;
        this.stopWatch = stopWatch;
        if (!(Variables.create)) {
            gravity = 9.81f;
            movementSpeed = 18;
        } else {
            gravity = 2;
            movementSpeed = 10f;
        }
    }

    public Vector3 moveBy(Vector3 translation, Vector3 player) {

        int length = 9;
        int h = length / 2;

        float width = 0.4f;
        float extension = 0.01f;


        float distanceX = Float.MAX_VALUE;
        float distanceY = Float.MAX_VALUE;
        float distanceZ = Float.MAX_VALUE;


        Vector3 playerA = new Vector3(player.x - width, player.y - 1.4f, player.z - width);
        Vector3 playerB = new Vector3(player.x + width, player.y + 0.4f, player.z + width);
        {
            BoundingBox playerBoxZ = new BoundingBox(
                    new Vector3(playerA.x, playerA.y, Float.MIN_VALUE),
                    new Vector3(playerB.x, playerB.y, Float.MAX_VALUE)
            );


            List<BoundingBox> blocks = new LinkedList<>();
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    for (int k = 0; k < length; k++) {
                        int x = (int) (player.x - h + i);
                        int y = (int) (player.y - h + j);
                        int z = (int) (player.z - h + k);


                        Block block = grid.getBlock(x, y, z);

                        if (block == null) continue;
                        Vector3 cubeA = new Vector3(x, y, z);
                        Vector3 cubeB = new Vector3(x + 1, y + 1, z + 1);
                        BoundingBox cubeBox = new BoundingBox(cubeA, cubeB);
                        blocks.add(cubeBox);
                        if (cubeBox.intersects(playerBoxZ)) {
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
        }

        playerA = new Vector3(player.x - width, player.y - 1.4f, player.z - width);
        playerB = new Vector3(player.x + width, player.y + 0.4f, player.z + width);
        {
            BoundingBox playerBoxY = new BoundingBox(
                    new Vector3(playerA.x, Float.MIN_VALUE, playerA.z),
                    new Vector3(playerB.x, Float.MAX_VALUE, playerB.z)
            );


            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    for (int k = 0; k < length; k++) {
                        int x = (int) (player.x - h + i);
                        int y = (int) (player.y - h + j);
                        int z = (int) (player.z - h + k);


                        Block block = grid.getBlock(x, y, z);

                        if (block == null) continue;
                        Vector3 cubeA = new Vector3(x, y, z);
                        Vector3 cubeB = new Vector3(x + 1, y + 1, z + 1);
                        BoundingBox cubeBox = new BoundingBox(cubeA, cubeB);


                        if (cubeBox.intersects(playerBoxY)) {
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

                        }

                    }
                }
            }
            if (translation.y >= 0) {
                if (translation.y > distanceY) {
                    velocity.y = distanceY - extension;
                    //velocity.y = 0;
                }
            } else {
                if (translation.y < -distanceY) {
                    velocity.y = -distanceY + extension;
                    //velocity.y = 0;
                    floorDistance = distanceY;
                    canJump = true;
                }
            }

            player.y += translation.y;
        }
        playerA = new Vector3(player.x - width, player.y - 1.4f, player.z - width);
        playerB = new Vector3(player.x + width, player.y + 0.4f, player.z + width);
        {
            BoundingBox playerBoxX = new BoundingBox(
                    new Vector3(Float.MIN_VALUE, playerA.y, playerA.z),
                    new Vector3(Float.MAX_VALUE, playerB.y, playerB.z)
            );


            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    for (int k = 0; k < length; k++) {
                        int x = (int) (player.x - h + i);
                        int y = (int) (player.y - h + j);
                        int z = (int) (player.z - h + k);


                        Block block = grid.getBlock(x, y, z);

                        if (block == null) continue;
                        Vector3 cubeA = new Vector3(x, y, z);
                        Vector3 cubeB = new Vector3(x + 1, y + 1, z + 1);
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


                                if (edgeBlock >= edgePlayer) {
                                    float dis = edgeBlock - edgePlayer;

                                    if (dis < distanceX)
                                        distanceX = dis;
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

            player.x += translation.x;
        }

        if (translation.z >= 0) {
            if (translation.z > distanceZ)
                translation.z = distanceZ;
        } else {
            if (translation.z < -distanceZ)
                translation.z = -distanceZ;
        }

        player.z += translation.z;

        if (((int) player.x) == Variables.endX && ((int) player.y) == Variables.endY && ((int) player.z) == Variables.endZ) {
            stopGame();
        }
        if (player.y < 3 && !Variables.create) {
            fallDown();
        }


        player.scl(Variables.blockSize);

        return translation;
    }

    void fallDown() {
        stopWatch.stop();
        stopWatch.start();
        camera.position.set(0, 100, 130);
    }

    void stopGame() {
        stopWatch.stop();
        Variables.time = (int) stopWatch.getElapsedTimeSecs();
        mapManagment.save();
        Gdx.app.exit();
    }
}
