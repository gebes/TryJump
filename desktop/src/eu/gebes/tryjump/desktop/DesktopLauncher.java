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


        panel.add(startButton);
        panel.add(resoltion);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreWindow(frame);

        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String res = String.valueOf(resoltion.getSelectedItem());

                if(res.equals("HD")){
                    start.startGame(1280,720);
                }else{
                    start.startGame(1920,1080);
                }

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
