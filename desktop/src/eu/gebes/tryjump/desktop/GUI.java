package eu.gebes.tryjump.desktop;

import eu.gebes.tryjump.desktop.map.MapPane;
import eu.gebes.tryjump.desktop.settings.SettingsPane;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private final MapPane mapPane = new MapPane();
    private final StartGUI startGuiClass = new StartGUI();
    private static final JFrame frame = new JFrame();
    private final SettingsPane settingsPaneClass = new SettingsPane();

    public void initComponents() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(330,300);
        final JPanel  panel = startGuiClass.main();
        JButton startButton = startGuiClass.settingsButton();
        JButton settingsButton = startGuiClass.startButton();
        final JPanel settings = settingsPaneClass.panel();
        final JPanel map = mapPane.mapPane();
        JButton backButton = settingsPaneClass.backButton();

        panel.add(startButton);
        panel.add(settingsButton);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreWindow(frame);

        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                frame.setContentPane(map);
                frame.setSize(330,360);
            }

        });
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                frame.setContentPane(settings);
                frame.setSize(330,450);
            }

        });
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                frame.setContentPane(panel);
                frame.setSize(330,300);
            }

        });
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static JFrame getFrame() {
        return frame;
    }
}
