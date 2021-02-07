package eu.gebes.tryjump.map;

import com.badlogic.gdx.math.Vector3;
import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.blocks.BlockManager;

import java.io.*;

public class WorldLoadManager {
    private final BlockManager blockManager = new BlockManager();
    private final File FILE_NAME = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.tryjump\\maps\\"+Variables.mapName+".map");


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
        out.append((int) p.x).append("/").append((int) p.y).append("/").append((int) p.z).append("/").append(currentCount).append("/").append(startBlock.getType().getId());
    }

    public Block[][][] loadMap() {
        Block[][][] blocks = new Block[Variables.gridWidth][Variables.gridHeight][Variables.gridDepth];
        String[] input = null;


        try {
            BufferedReader in;
            if(Variables.mapName.equals("Jump1")||Variables.mapName.equals("Jump2")||Variables.mapName.equals("Jump3")){
                InputStream file = getClass().getResourceAsStream("/maps/"+Variables.mapName+".map");
                in = new BufferedReader(new InputStreamReader(file));
            }else {
                in = new BufferedReader(new FileReader(FILE_NAME));
            }
            input = in.readLine().split(" ");
            System.out.println(input);


            for(int i=0;i< input.length;i++){
                String[] oneBlock = input[i].split("/");
                int x=Integer.parseInt(oneBlock[0]),y=Integer.parseInt(oneBlock[1]),z=Integer.parseInt(oneBlock[2]);

                Block.Type block = Block.Type.fromId(Integer.parseInt(oneBlock[4]));

                for(int j =0; j<Integer.parseInt(oneBlock[3]);j++){
                    blocks[x][y][z] = blockManager.getBlockFor(block);
                    x++;
                    if(x==Variables.gridWidth){ x=0;z++;
                        if(z==Variables.gridHeight){ y++;y=0;}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blocks;
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