package eu.gebes.tryjump.desktop;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JButton button = new JButton("START");

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
        button.setSize(300,100);
        button.setPreferredSize(new Dimension(300, 70));

        return button;
    }

    public JComboBox resolution(){
        String comboBoxListe[] = {"FULL-HD", "HD"};


        JComboBox res = new JComboBox(comboBoxListe);
        res.setPreferredSize(new Dimension(300, 70));
        return  res;
    }

}
