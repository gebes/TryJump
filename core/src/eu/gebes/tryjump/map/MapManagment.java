package eu.gebes.tryjump.map;

import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.utils.FileLocations;

import java.io.*;


public class MapManagment {
    private final String[] maps = Variables.maps;

    public void save() {
        try {
            BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter(FileLocations.MAPS_FILE));
            for (int i = 0; i < maps.length; i++) {
                if (maps[i].contains(Variables.mapName)) {
                    String[] tmp = maps[i].split(":");
                    if (Integer.parseInt(tmp[1]) > Variables.time) {
                        maps[i] = Variables.mapName + ":" + Variables.time;
                    }
                }
                outputWriter.write(maps[i]);
                outputWriter.newLine();
            }
            if (Variables.levelEditorModeEnabled) {
                outputWriter.write(Variables.mapName + ":" + "10000");
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