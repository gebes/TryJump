package eu.gebes.tryjump.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import eu.gebes.tryjump.TryJump;
import eu.gebes.tryjump.Variables;

public class DesktopLauncher {
    public static void main(String[] arg) {

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
        config.width = 1280;
        config.height = 720;
        config.foregroundFPS = 0;

        new LwjglApplication(new TryJump(), config);
    }
}
