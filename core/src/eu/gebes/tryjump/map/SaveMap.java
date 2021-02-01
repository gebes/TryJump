package eu.gebes.tryjump.map;

import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;

import java.io.*;

public class SaveMap {
    private File FILE_NAME = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.tryjump\\maps\\optimised2.txt");

    public void getMap(Block[][][] blocks) {
        String out = "";

        Block startBlock = null;
        int count = 0;
        int startX = 0, startY =0 , startZ= 0;
        int type = 0;
        boolean bool = false;

        for (int y = 0; y < Variables.gridHeight; y++) {
            for (int z = 0; z < Variables.gridDepth; z++) {
                for (int x = 0; x < Variables.gridWidth; x++) {

                    if (blocks[x][y][z] == null){
                        if(bool){
                            out +=  startX + "/" +  startY + "/" +  startZ + "/" + count + "/" + type + "-";
                            count = 0;
                            startBlock = null;
                            bool = false;
                        }
                        continue;
                    }

                    if (startBlock == null) {
                        startX = x; startY = y;
                        startZ = z;
                        startBlock=blocks[x][y][z];
                        type = blocks[x][y][z].getType().getId();
                        bool = true;
                    }

                    if (!startBlock.getType().equals(blocks[x][y][z].getType())) {
                        System.out.println("d");
                        out +=  startX + "/" +  startY + "/" +  startZ + "/" + count + "/" + type + "-";
                        count = 0;
                        startBlock = null;
                        bool = false;
                    }

                    count++;
                }
            }
        }

        saveToFile(out);
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
