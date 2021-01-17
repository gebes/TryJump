package eu.gebes.tryjump.desktop;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DesktopLauncher extends JFrame {

    private static Start start;
    private static GUI guiClass  = new GUI();
    private static JFrame frame = new JFrame();

    public static void main(String[] arg) {
        start = new Start(arg);
        initComponents();
    }

    private static void initComponents() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(330,300);
        JPanel  panel = guiClass.main();
        JButton startButton = guiClass.startButton();
        final JComboBox  resoltion = guiClass.resolution();
        final TextField textField = new TextField("67");

        panel.add(startButton);
        panel.add(resoltion);
        panel.add(new Label("FOV"));
        panel.add(textField);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreWindow(frame);

        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String res = String.valueOf(resoltion.getSelectedItem());

                String[] split = res.split("x");
                int width = Integer.parseInt(split[0]);
                int height = Integer.parseInt(split[1]);

                int fov = Integer.parseInt(textField.getText());


                start.startGame(width, height, fov);

                frame.dispose();
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
