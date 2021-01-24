package eu.gebes.tryjump.map;

import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.blocks.BlockManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoadMap {
    private Block[][][] blocks = new Block[Variables.gridWidth][Variables.gridHeight][Variables.gridDepth];
    private File FILE_NAME = new File( System.getProperty("user.home") + "\\AppData\\Roaming\\.tryjump\\maps\\" + Variables.mapName +".txt");
    private final BlockManager blockManager = new BlockManager();

    public Block[][][] loadMap(){
        String[] input;

        try {
            BufferedReader in  = new BufferedReader(new FileReader(FILE_NAME));
            String tmp = in.readLine();
            input= tmp.split("/");

            for(int i =0; i<input.length;i+=4){
                blocks[Integer.parseInt(input[i])][Integer.parseInt(input[i+1])][Integer.parseInt(input[i+2])] = blockManager.getBlockFor(Block.Type.fromId(Integer.parseInt(input[i+3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }
}
