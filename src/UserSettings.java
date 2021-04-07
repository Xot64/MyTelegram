import Emulator.Emulator;
import org.javagram.response.object.User;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserSettings {
    private JPanel rootPanel;
    private JPanel Labels;
    private JPanel Input;
    private JButton SendBut;
    private JTextField FirstName;
    private JTextField LastName;
    private JTextField FatherName;
    private JTextField PhoneNumber;
    private JButton Send;
    private JLabel FN;
    private JLabel LN;
    private JLabel PN;
    private JLabel Phone;
    boolean FNCorrect = true;
    boolean LNCorrect = true;

    private String FNText;
    private String LNText;
    private String PNText;
    public String PhoneText;


    private BufferedImage BG;
    public UserSettings()
    {
        SendBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FNText = FirstName.getText().trim().length() > 0 ? FirstName.getText().trim() : "";
                LNText = LastName.getText().trim().length() > 0 ? LastName.getText().trim() : "";
                PNText = FatherName.getText().trim().length() > 0 ? FatherName.getText().trim() : "";
                FNCorrect = FNText.length() != 0;
                LNCorrect = LNText.length() != 0;

                if ((FNCorrect) && (LNCorrect)) {
                    System.out.println("Всё введено верно");
                    int usID = -1;
                    try {
                        usID = Emulator.setUser(FNText, LNText, PNText, PhoneText.replaceAll("[\\D]","").substring(1), "");
                    } catch (IOException|ParseException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        Emulator.addTarget(Launcher.Target,usID);
                    } catch (IOException|ParseException e1) {
                        e1.printStackTrace();
                    }
                    Launcher.goToForm(4);
                }
                else
                {
                    System.out.println("Ошибка ввода информации");
                    Launcher.goToForm(3);
                }
            }
        });
    }
    public JPanel getRootPanel() {
        //Установка фонового рисунка
        try {
            BG = ImageIO.read(new File("GUIComponents/background.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //Проверка найден ли пользователь.
        if (Emulator.activeUserID > -1)
        {
            FNText = Emulator.getActiveUser().getFirstName();
            LNText = Emulator.getActiveUser().getLastName();
            PNText = Emulator.getActiveUser().getFatherName();
            if (PhoneText == null)
            {
                PhoneText = "+7" + Emulator.getActiveUser().getNumbers().get(0);;
            }
            else
            {
                if (PhoneText.equals("")) {
                    PhoneText = "+7" + Emulator.getActiveUser().getNumbers().get(0);;
                }
            }
        }

        Labels.removeAll();
        Labels.setLayout(new GridLayout(4,1,0,0));
        Labels.setOpaque(false);
        FN = new JLabel("Фамилия");
        LN = new JLabel("Имя");
        PN = new JLabel("Отчество");
        Phone = new JLabel("Телефон");
        FN.setForeground(FNCorrect ? Color.WHITE: Color.red);
        LN.setForeground(LNCorrect ? Color.WHITE: Color.red);
        PN.setForeground(Color.WHITE);
        Phone.setForeground(Color.WHITE);
        FN.setHorizontalAlignment(JLabel.RIGHT);
        LN.setHorizontalAlignment(JLabel.RIGHT);
        PN.setHorizontalAlignment(JLabel.RIGHT);
        Phone.setHorizontalAlignment(JLabel.RIGHT);
        FN.setOpaque(false);
        LN.setOpaque(false);
        PN.setOpaque(false);
        Phone.setOpaque(false);
        Labels.add(FN);
        Labels.add(LN);
        Labels.add(PN);
        Labels.add(Phone);

        Input.removeAll();
        Input.setLayout(new GridLayout(4,1,0,0));
        Input.setOpaque(false);
        FirstName = new JTextField(FNText);
        FirstName.setPreferredSize(new Dimension(300,30));
        LastName = new JTextField(LNText);
        LastName.setPreferredSize(new Dimension(300,30));
        FatherName = new JTextField(PNText);
        FatherName.setPreferredSize(new Dimension(300,30));
        PhoneNumber = new JTextField(PhoneText);
        PhoneNumber.setPreferredSize(new Dimension(300,30));
        PhoneNumber.setEnabled(false);
        PhoneNumber.setBackground(Color.gray);
        PhoneNumber.setDisabledTextColor(Color.black);

        Input.add(FirstName);
        Input.add(LastName);
        Input.add(FatherName);
        Input.add(PhoneNumber);
        return rootPanel;
    }

    private void createUIComponents() {
        rootPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(BG,0,0,null);
            }
        };
    }
}
