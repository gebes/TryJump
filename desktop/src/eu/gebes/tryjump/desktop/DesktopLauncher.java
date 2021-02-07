package eu.gebes.tryjump.desktop;

import javax.swing.*;

public class DesktopLauncher extends JFrame {
    private static GUI gui = new GUI();

    public static void main(String[] arg) {
        gui.initComponents();
    }
}
