package eu.gebes.tryjump.map;

import eu.gebes.tryjump.Variables;

import java.io.*;


public class MapManagment {
    private static String[] maps = Variables.maps;
    private static final String home = System.getProperty("user.home");
    private static final File FILE_DIRECTORY = new File( home + "\\AppData\\Roaming\\.tryjump\\");
    private static final File FILE_NAME = new File(FILE_DIRECTORY + "\\maps.txt");

    public static synchronized void save() {
        try {
            BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter(FILE_NAME));
            for(int i=0;i<maps.length;i++){
                if(maps[i].contains(Variables.mapName)){
                    String[] tmp = maps[i].split(":");
                    if(Integer.parseInt(tmp[1])>Variables.time){
                        maps[i]=Variables.mapName+":"+Variables.time;
                    }
                }
                outputWriter.write(maps[i]);
                outputWriter.newLine();
            }
            if(Variables.create){
                outputWriter.write(Variables.mapName+":"+"10000");
                outputWriter.newLine();
            }

            outputWriter.flush();
            outputWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream", e);
        }
    }

}