package eu.gebes.tryjump.map;

import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.blocks.Block;

import java.io.*;

public class SaveMap {
    private File FILE_NAME = new File( System.getProperty("user.home") + "\\AppData\\Roaming\\.tryjump\\maps\\" + Variables.mapName +".txt");


    public void getMap(Block[][][] blocks){
        StringBuilder map = new StringBuilder();

        for(int i =0;i< Variables.gridWidth;i++){
            for(int j=0;j<Variables.gridHeight;j++){
                for(int k = 0;k<Variables.gridDepth;k++){
                    if(blocks[i][j][k]!=null){
                        int ID = blocks[i][j][k].getType().getId();
                        map.append(i+"/");
                        map.append(j+"/");
                        map.append(k+"/");
                        map.append(ID+"/");
                    }
                }
            }
        }
        saveMap(map.toString());
    }

    public void saveMap(String map){
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
