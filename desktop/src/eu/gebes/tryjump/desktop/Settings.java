package eu.gebes.tryjump.desktop;

import javax.swing.*;
import java.awt.*;

public class Settings {
    private JPanel settings = new JPanel();
    private final TextField textField = new TextField("67");
    private JButton backButton = new JButton("BACK");
    private final JComboBox res = new JComboBox(new String[]{"1920x1080", "1280x720", "2560x1440"});
    private int width = 1920;
    private int height = 1080;
    private int fov = 70;

    public JComboBox resolution(){
        res.setPreferredSize(new Dimension(300, 70));
        return  res;
    }

    public JButton backButton(){
        backButton.setSize(300,100);
        backButton.setPreferredSize(new Dimension(300, 70));

        return backButton;

    }

    public JPanel panel(){
        JPanel panel = new JPanel();
        panel.setSize(300,500);

        JLabel header = new JLabel("SETTINGS");
        header.setFont(new java.awt.Font("Tahoma", 1, 28));
        header.setPreferredSize(new Dimension(300, 70));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.TOP);

        panel.add(header, BorderLayout.NORTH);
        panel.add(resolution());
        panel.add(backButton);

        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String resolution = String.valueOf(res.getSelectedItem());

                String[] split = resolution.split("x");

                width = Integer.parseInt(split[0]);
                height = Integer.parseInt(split[1]);
                fov = Integer.parseInt(textField.getText());
            }

        });
        return panel;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFov() {
        return fov;
    }
}
