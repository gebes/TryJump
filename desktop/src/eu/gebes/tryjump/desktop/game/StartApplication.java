package eu.gebes.tryjump.desktop.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import eu.gebes.tryjump.TryJump;
import eu.gebes.tryjump.Variables;

public class StartApplication {

    public void startGame(int width, int height, int fov, boolean fullscreen, int musicVolume) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = width;
        config.height = height;
        config.foregroundFPS = 144;
        config.fullscreen = fullscreen;
        config.vSyncEnabled=false;
        Variables.FOV = fov;
        Variables.musicVolume = musicVolume;
        System.out.println(Variables.mapName);

        new LwjglApplication(new TryJump(), config);
    }
}
