package eu.gebes.tryjump.desktop.map;

import eu.gebes.tryjump.Variables;
import eu.gebes.tryjump.desktop.game.StartApplication;
import eu.gebes.tryjump.desktop.settings.SettingsPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MapPane {
    private final MapManagment mapManagment = new MapManagment();
    private String[] maps = mapManagment.load();
    private static StartApplication startApplication = new StartApplication();
    private static SettingsPane settingsPaneClass = new SettingsPane();

    public JPanel mapPane(){
        JPanel mapSelector = new JPanel();
        JLabel header = new JLabel("MAPS");
        header.setFont(new java.awt.Font("Tahoma", 1, 28));
        header.setPreferredSize(new Dimension(300, 70));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.TOP);
        mapSelector.add(header);

        for(int i=0;i<maps.length;i++){
            mapSelector.add(button(maps[i]));
        }
        return mapSelector;
    }

    private JButton button(String text){
        final String  name = text;
        JButton button = new JButton(text);
        button.setFont(new java.awt.Font("Tahoma", 1, 28));
        button.setPreferredSize(new Dimension(300, 50));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.TOP);

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Variables.mapName = name;
                startApplication.startGame(settingsPaneClass.getWidth(), settingsPaneClass.getHeight(), settingsPaneClass.getFov(), settingsPaneClass.isFullscreenBool(), settingsPaneClass.getVolume());
            }
        });

        return button;
    }
}
