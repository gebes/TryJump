package eu.gebes.tryjump.desktop;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level =  AccessLevel.PRIVATE)
public class StartGUI {
    JButton startButton = new JButton("SETTINGS");
    JButton settingsButton = new JButton("START");

    public JPanel main(){
        JPanel panel = new JPanel();
        panel.setSize(300,500);

        JLabel header = new JLabel("START GAME");
        header.setFont(new java.awt.Font("Tahoma", 1, 28));
        header.setPreferredSize(new Dimension(300, 70));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.TOP);

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    public JButton startButton(){
        startButton.setSize(300,100);
        startButton.setPreferredSize(new Dimension(300, 70));

        return startButton;
    }

    public JButton settingsButton(){
        settingsButton.setSize(300,100);
        settingsButton.setPreferredSize(new Dimension(300, 70));

        return settingsButton;
    }
}
