import Emulator.Emulator;
import Emulator.Friend;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FriendSettings {
    private JPanel RootPanel;
    private JTextField FIO;
    private JButton Save;
    private JPanel Trash;
    public JPanel DelUser;
    private JTextField Telephon;
    private BufferedImage BG;


    public FriendSettings (int f)
    {
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Friend ff;
                if (f != 0)
                {
                    ff = Emulator.getActiveUser().getFriend(f);
                    FIO.setText(ff.getSaveAs());
                    Telephon.setText("+7" + ff.getNumber());
                    ff.setSaveAs(FIO.getText());
                    ff.setNumber(Telephon.getText());
                }
                else
                {
                    Emulator.getActiveUser().addFriend(Telephon.getText().substring(2) + " " + FIO.getText());
                    int id = Emulator.CheckNumber(Telephon.getText().substring(2));
                    if (id == -1)
                    {
                        int nid = 101;
                        while (Emulator.UserList.containsKey(nid))
                        {
                            nid++;
                        }
                        Emulator.addUser(nid, "---","---","",Telephon.getText().substring(2));
                        Emulator.getUser(nid).addFriend(Emulator.getActiveUser().getNumbers().get(0) + "---");
                        try {
                            Emulator.updateUserList();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                try {
                    Emulator.saveUserList();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                Launcher.goToForm(4);
            }
        });
        DelUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Emulator.getActiveUser().RemoveFriend(f);
                try {
                    Emulator.saveUserList();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                Launcher.goToForm(4);
            }
        });
    }

    public JPanel getRootPanel() {
        try {
            BG = ImageIO.read(new File("GUIComponents/icon-trash.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        DelUser.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(169,58,59)));
        RootPanel.setBackground(new Color(0,0,0,220));
        return RootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        Trash = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(BG,0,1,null);
            }
        };

    }
}
