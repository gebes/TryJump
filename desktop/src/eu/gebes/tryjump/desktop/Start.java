package eu.gebes.tryjump.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import eu.gebes.tryjump.TryJump;
import eu.gebes.tryjump.Variables;

public class Start {
    private String[] arg;

    public Start(String[] arg){
        this.arg = arg;
    }

    public void startGame(int width, int height){
        if (arg.length > 0) {
            Variables.gridWidth = Integer.parseInt(arg[0]);
        }
        if (arg.length > 1) {
            Variables.gridHeight = Integer.parseInt(arg[1]);
        }
        if (arg.length > 2) {
            Variables.gridDepth = Integer.parseInt(arg[2]);
        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = width;
        config.height = height;
        config.foregroundFPS = 0;

        new LwjglApplication(new TryJump(), config);
    }
}
