package eu.gebes.tryjump.utils;

import javax.swing.*;
import java.io.File;

public class FileLocations {
    public static final File GAME_HOME_FOLDER;
    public static final File MAPS_FILE, SETTINGS_FILE;


    static {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            GAME_HOME_FOLDER = new File(System.getProperty("user.home") + "/AppData/Roaming/.tryjump/");
        } else {
            GAME_HOME_FOLDER = new File(System.getProperty("user.home") + "/TryJump/");
        }
        MAPS_FILE = new File(GAME_HOME_FOLDER + "/maps.txt");
        SETTINGS_FILE = new File(GAME_HOME_FOLDER + "/settings.txt");

    }
}
