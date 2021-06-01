package eu.gebes.tryjump.desktop.map;

import eu.gebes.tryjump.Variables;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MapPane {
    MapManagment mapManagment = new MapManagment();
    String[] maps = mapManagment.load();
    MapNodes mapNodes = new MapNodes();

    public JPanel mapPane(){
        Variables.maps = maps;
        JPanel mapSelector = new JPanel();
        JLabel header = new JLabel("MAPS");
        header.setFont(new java.awt.Font("Tahoma", 1, 28));
        header.setPreferredSize(new Dimension(300, 70));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.TOP);
        mapSelector.add(header);

        JScrollPane scrollPane = new JScrollPane(mapNodes.selector());
        scrollPane.setPreferredSize(new Dimension(300,150));

        mapSelector.add(scrollPane);
        mapSelector.add(mapNodes.buttonCreate());
        return mapSelector;
    }


}
