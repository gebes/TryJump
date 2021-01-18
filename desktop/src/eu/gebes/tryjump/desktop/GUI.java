package eu.gebes.tryjump.desktop;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private static StartApplication startApplication;
    private static StartGUI startGuiClass = new StartGUI();
    private static Settings settingsClass  = new Settings();
    private static JFrame frame = new JFrame();

    public void initComponents(String[] arg) {
        startApplication = new StartApplication(arg);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(330,300);
        final JPanel  panel = startGuiClass.main();
        JButton startButton = startGuiClass.settingsButton();
        JButton settingsButton = startGuiClass.startButton();
        final JPanel settings = settingsClass.panel();
        JButton backButton = settingsClass.backButton();

        panel.add(startButton);
        panel.add(settingsButton);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreWindow(frame);

        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startApplication.startGame(settingsClass.getWidth(), settingsClass.getHeight(), settingsClass.getFov());
                frame.dispose();
            }

        });
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                frame.setContentPane(settings);
                frame.setSize(330,500);
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
}
