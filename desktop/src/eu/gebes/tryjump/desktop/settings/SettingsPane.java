package eu.gebes.tryjump.desktop.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingsPane {
    SettingsNodes nodes = new SettingsNodes();
    static SettingsSave save = new SettingsSave();
    static String[] settings = save.load();
    JButton backButton = new JButton("BACK");
    JComboBox res = nodes.resolution(Integer.parseInt(settings[0]));
    JComboBox fullscreen = nodes.fullscreen(Boolean.parseBoolean(settings[4]));
    JSlider musicVolume = new JSlider(SwingConstants.HORIZONTAL, 0, 100, Integer.parseInt(settings[3]));
    JSlider FOV = new JSlider(SwingConstants.HORIZONTAL, 20, 130, Integer.parseInt(settings[2]) );
    @Getter
    static int width = Integer.parseInt(settings[0]);
    @Getter
    static int height = Integer.parseInt(settings[1]);
    @Getter
    static int fov = Integer.parseInt(settings[2]);
    @Getter
    static int volume = Integer.parseInt(settings[3]);
    @Getter
    static boolean fullscreenBool = Boolean.parseBoolean(settings[4]);

    public JPanel panel(){
        JPanel panel = new JPanel();

        JLabel header = new JLabel("SETTINGS");
        header.setFont(new java.awt.Font("Tahoma", 1, 28));
        header.setPreferredSize(new Dimension(300, 70));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.TOP);
        final JLabel valueFOV = nodes.valueText(String.valueOf(FOV.getValue()));
        final JLabel valueVolume = nodes.valueText(String.valueOf(musicVolume.getValue())+ "%");

        panel.add(header, BorderLayout.NORTH);
        panel.add(res);
        panel.add(fullscreen);
        panel.add(nodes.describtionText("FOV"));
        panel.add(FOV);
        panel.add(valueFOV);
        panel.add(nodes.describtionText("VOLUME"));
        panel.add(musicVolume);
        panel.add(valueVolume);
        panel.add(backButton);

        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String resolution = String.valueOf(res.getSelectedItem());

                if(String.valueOf(fullscreen.getSelectedItem()).equals("FULLSCREEN")){fullscreenBool=true;}
                else{fullscreenBool = false;}

                String[] split = resolution.split("x");

                width = Integer.parseInt(split[0]);
                height = Integer.parseInt(split[1]);
                fov = FOV.getValue();
                volume = musicVolume.getValue();
                save.save(new String[]{String.valueOf(width), String.valueOf(height),String.valueOf(fov),String.valueOf(volume), String.valueOf(fullscreenBool)});
            }

        });

        FOV.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                valueFOV.setText(String.valueOf(FOV.getValue()));
            }
        });
        musicVolume.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                valueVolume.setText(String.valueOf(musicVolume.getValue()) + "%");
            }
        });
        return panel;
    }

    public JButton backButton(){
        backButton.setPreferredSize(new Dimension(300, 70));

        return backButton;
   }
}
