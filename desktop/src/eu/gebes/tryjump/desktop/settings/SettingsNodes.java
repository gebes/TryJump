package eu.gebes.tryjump.desktop.settings;

import javax.swing.*;
import java.awt.*;

public class SettingsNodes {
    public JLabel describtionText(String Text){
        JLabel text = new JLabel(Text);
        text.setMinimumSize(new Dimension(55, 40));
        text.setPreferredSize(new Dimension(55, 40));
        text.setMaximumSize(new Dimension(55, 40));
        text.setHorizontalAlignment(SwingConstants.CENTER);

        return text;
    }
    public  JLabel valueText(String Text){
        JLabel text = new JLabel(Text);
        text.setMinimumSize(new Dimension(30, 40));
        text.setPreferredSize(new Dimension(30, 40));
        text.setMaximumSize(new Dimension(30, 40));
        text.setHorizontalAlignment(SwingConstants.CENTER);

        return text;
    }

    public JComboBox resolution(int width){

        String[] values = null;
        switch(width){
            case 1280:
                values = new String[]{"1280x720", "3840x2160","2560x1440","1920x1080"};
                break;
            case 1920:
                values = new String[]{"1920x1080","3840x2160", "2560x1440","1280x720"};
                break;
            case 2560:
                values = new String[]{"2560x1440","3840x2160","1920x1080" ,"1280x720"};
                break;
            case 3840:
                values = new String[]{"3840x2160","2560x1440","1920x1080" ,"1280x720"};
                break;
        }
        JComboBox res = new JComboBox(values);
        res.setPreferredSize(new Dimension(300, 70));

        return  res;
    }
    public JComboBox fullscreen(boolean bool){
        String[] values;
        if(bool==true){values = new String[]{"FULLSCREEN","WINDOW", };}
        else{ values = new String[]{"WINDOW", "FULLSCREEN"};}
        JComboBox fullscreen = new JComboBox(values);
        fullscreen.setPreferredSize(new Dimension(300, 70));

        return  fullscreen;
    }
}
