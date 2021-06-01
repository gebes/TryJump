package eu.gebes.tryjump.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import eu.gebes.tryjump.Grid;
import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.map.MapManagment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Player {


    @NonNull
    final Grid grid;

    public Player(@NonNull Grid grid) {
        this.grid = grid;
        camera = new PerspectiveCamera(Variables.FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setPositionToSpawn();

        camera.near = Variables.cameraNear;
        camera.far = Variables.cameraFar;
        movementController = new MovementController(camera, this);
        movementController.setDegreesPerPixel(Variables.cameraDegreesPerPixel);
        movementController.setVelocity(Variables.cameraVelocity);


        Gdx.input.setInputProcessor(movementController);
        Gdx.input.setCursorCatched(true);
    }

    Block.Type selectedBuildBlock = Block.Type.Stone;


    PerspectiveCamera camera;
    MovementController movementController;
    MapManagment mapManagment = new MapManagment();


    public void update() {
        getMovementController().update();
    }

    public void setPositionToSpawn(){
        setWorldPosition(Variables.gridWidth/2, 7, Variables.gridDepth/2);
    }

    public void setWorldPosition(int x, int y, int z){
        camera.position.set(x * Variables.blockSize, y * Variables.blockSize, z * Variables.blockSize);
    }

    public Vector3 getWorldPosition() {
        return getMovementController().getWorldPosition();
    }

    public Vector3 moveAndCollide(Vector3 translation, Vector3 player) {

        int length = 16;
        int h = length / 2;

        float width = 0.4f;
        float extension = 0.001f;


        float distanceX = Float.MAX_VALUE;
        float distanceY = Float.MAX_VALUE;
        float distanceZ = Float.MAX_VALUE;



        translation.scl(Variables.blockSize);
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

            if (translation.z >= 0) {
                if (translation.z > distanceZ)
                    translation.z = distanceZ - extension;
            } else {
                if (translation.z < -distanceZ)
                    translation.z = -distanceZ + extension;
            }

            player.z += translation.z;
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
            movementController.setGrounded(distanceY < 0.1f && translation.y <= 0);
            if (translation.y >= 0) {
                if (translation.y > distanceY) {
                    translation.y = distanceY - extension;
                }
            } else {
                if (translation.y < -distanceY) {
                    translation.y = -distanceY + extension;
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
                    translation.x = distanceX - extension;
            } else {
                if (translation.x < -distanceX)
                    translation.x = -distanceX + extension;
            }

            player.x += translation.x;
        }

        translation.scl(1f / Variables.blockSize);

        return translation;
    }


}
