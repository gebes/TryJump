package eu.gebes.tryjump.map;

import com.badlogic.gdx.math.Vector3;
import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.blocks.BlockManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class WorldLoadManager {
    private final BlockManager blockManager = new BlockManager();
    private final File FILE_NAME = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.tryjump\\maps\\level.data");


    public void saveMap(Block[][][] blocks) {

        StringBuilder out = new StringBuilder();
        Block startBlock = null;
        int currentCount = 0;

        for (int y = 0; y < blocks.length; y++) {
            for (int x = 0; x < blocks[y].length; x++) {
                for (int z = 0; z < blocks[x][y].length; z++) {

                    if (startBlock != null && (blocks[x][y][z] == null || !blocks[x][y][z].getType().equals(startBlock.getType()))) {

                        appendToBuilder(out, startBlock, currentCount);

                        startBlock = null;
                        currentCount = 0;
                    }

                    if (startBlock == null) startBlock = blocks[x][y][z];

                    if (blocks[x][y][z] != null && startBlock != null && blocks[x][y][z].getType().equals(startBlock.getType()))
                        currentCount++;
                }
            }
        }

        if (startBlock != null) {
            appendToBuilder(out, startBlock, currentCount);
        }

        saveToFile(out.toString());
    }

    private void appendToBuilder(StringBuilder out, Block startBlock, int currentCount) {
        Vector3 p = startBlock.getPosition().scl(1f / Variables.blockSize);
        if (out.length() > 0)
            out.append(" ");
        out.append((int) p.x).append("/").append((int) p.y).append("/").append((int) p.z).append("/").append(currentCount).append("x").append(startBlock.getType().getId());
    }

    public Block[][][] loadMap() {

        Block[][][] blocks = new Block[Variables.gridWidth][Variables.gridHeight][Variables.gridDepth];
        String[] input;

        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            input = in.readLine().split(" ");
        } catch (Exception e) {

            for (int x = 0; x < blocks.length; x++) {
                for (int y = 0; y < blocks[x].length; y++) {
                    for (int z = 0; z < blocks[x][y].length; z++) {
                        if (y == 0)
                            blocks[x][y][z] = blockManager.getBlockFor(Block.Type.Dirt);
                    }
                }
            }

            return blocks;
        }

        int currentIndex = 0;
        Safe safe = new Safe(input[currentIndex]);

        for (int y = safe.y; y < blocks.length; y++) {
            for (int x = safe.x; x < blocks[y].length; x++) {
                for (int z = safe.z; z < blocks[x][y].length; z++) {

                    if (safe.repeat == 0) {
                        if (currentIndex == input.length) break;
                        safe = new Safe(input[currentIndex++]);
                        x = safe.x;
                        y = safe.y;
                        z = safe.z;
                    }
                    if (safe.repeat != 0) {
                        blocks[x][y][z] = blockManager.getBlockFor(safe.getBlockType());
                        safe.repeat--;
                    }
                }
            }
        }
        return blocks;
    }

    @Data
    @AllArgsConstructor
    static
    class Safe {

        int x, y, z;
        Block.Type blockType;
        int repeat;

        public Safe(String input) {
            String[] split = input.split("/");
            this.x = Integer.parseInt(split[0]);
            this.y = Integer.parseInt(split[1]);
            this.z = Integer.parseInt(split[2]);
            split = split[3].split("x");
            this.repeat = Integer.parseInt(split[0]);
            this.blockType = Block.Type.fromId(Integer.parseInt(split[1]));
        }
    }

    private void saveToFile(String map) {
        try {

            BufferedWriter outputWriter= new BufferedWriter(new FileWriter(FILE_NAME));

            outputWriter.write(map);

            outputWriter.flush();
            outputWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream", e);
        }
    }
}
