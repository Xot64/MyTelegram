import Emulator.Emulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Welcome {
    private JPanel rootPanel;
    private JPanel LOGO;
    private JTextArea доброПожаловатьTextArea;
    private JTextArea TextUser;
    private JButton HelloButton;
    private JButton ItsNotMeButton;

    private BufferedImage BG;
    public BufferedImage LogoIMG;

    public Welcome()
    {
        try {
            BG = ImageIO.read(new File("GUIComponents/background.png"));

        }
        catch (IOException e){
            e.printStackTrace();
        }
        HelloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Launcher.goToForm(4);
            }
        });
        ItsNotMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Launcher.goToForm(2);
            }
        });
    }
    private void createUIComponents() {
        rootPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(BG,0,0,null);
            }
        };
        LOGO = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(LogoIMG,0,0,null);
            }
        };

    }

    public JPanel getRootPanel() {
        TextUser.setText(Emulator.getActiveUser().getFirstName());
        try {
            LogoIMG = ImageIO.read(new File("GUIComponents/Avatar/AvatarXL/" + Emulator.getActiveUser().getAvatar() + "_160.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootPanel;
    }
}
