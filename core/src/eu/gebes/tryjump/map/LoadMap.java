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
            input= tmp.split("-");

            for(int i=0;i< input.length;i++){
                String[] oneBlock = input[i].split("/");

                int x=Integer.parseInt(oneBlock[0]),y=Integer.parseInt(oneBlock[1]),z=Integer.parseInt(oneBlock[2]);
                int tmpX=x,tmpY=y,tmpZ=z;
                Block.Type block = Block.Type.fromId(Integer.parseInt(oneBlock[4]));

                for(int j =0; j<Integer.parseInt(oneBlock[3]);j++){
                    blocks[tmpX][tmpY][tmpZ] = blockManager.getBlockFor(block);
                    tmpX++;

                    if(tmpX==Variables.gridWidth){
                        tmpX=0;tmpZ++;
                        if(tmpZ==Variables.gridHeight){ tmpY++;tmpZ=0;}
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }
}
