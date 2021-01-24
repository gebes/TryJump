package eu.gebes.tryjump.map;

import com.badlogic.gdx.math.Vector3;
import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MarkusLoadManager {
    private File FILE_NAME = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.tryjump\\maps\\optimised2.txt");


    List<Block.Type> types = Arrays.asList(Block.Type.values());

    public void getMap(Block[][][] blocks) {


        String out = "";

        Block startBlock = null;
        int count = 0;


        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {

                    if (blocks[x][y][z] == null) continue;

                    if (startBlock == null)
                        startBlock = blocks[x][y][z];

                    if (!startBlock.getType().equals(blocks[x][y][z].getType())) {
                        int type = blockId(blocks[x][y][z]);
                        Vector3 position = startBlock.getPosition();
                        out += (int) position.x + "/" + (int) position.y + "/" + (int) position.z + "/" + count + "x" + type + " ";
                        count = 0;
                        startBlock = null;
                    }

                    count++;
                }
            }
        }

        saveToFile(out.toString());
    }

    private int blockId(Block block) {
        if (block == null)
            return 0;

        return types.indexOf(block.getType()) + 1;
    }


    private void saveToFile(String map) {
        try {

            BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter(FILE_NAME));

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
