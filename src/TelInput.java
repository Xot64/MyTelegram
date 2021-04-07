
import Emulator.Emulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TelInput {
    private JPanel rootPanel;
    private JTextField TelField;
    private JButton SendCodeBut;
    private JButton CheckCodeBut = new JButton("Отправить код");
    private JLabel ErText;
    private JTextArea TextArea;
    private JPanel LOGO;
    private JPanel resultPanel;
    private BufferedImage BG;
    private BufferedImage LogoIMG;
    private static int Code = 0;
    private JTextField InCode;
    private String Number = "";
    public TelInput() {
        try {
            BG = ImageIO.read(new File("GUIComponents/background.png"));
            LogoIMG = ImageIO.read(new File("GUIComponents/logo.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        SendCodeBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultPanel.removeAll();
               // GridLayout GLO = new GridLayout(4,1,0,0);
                resultPanel.setLayout(new GridLayout(4,1,0,0));
                Number = TelField.getText();
                Number = Number.replaceAll("[\\D]","");
                if ((Number.substring(0,1).equals("7") || Number.substring(0,1).equals("8")) && (Number.length() == 11)) {
                    System.out.println("Верный формат");
                    Number = Number.substring(1);
                    Launcher.formUser.PhoneText = "+7" + Number;
                            JLabel error = new JLabel("На указанный номер отправлено СМС с кодом подтверждения");
                    Code = (int) (Math.random()*999999);
                    System.out.println("СМС: " + Code);
                    error.setForeground(Color.green);
                    resultPanel.add(error);
                    InCode = new JTextField();
                    resultPanel.add(InCode);
                  //  CheckCodeBut = new JButton("Отправить код");
                    resultPanel.add(CheckCodeBut);
                    Emulator.activeUserID = Emulator.CheckNumber(Number);
                    System.out.println(Emulator.activeUserID);
                    Launcher.goToForm(2);
                }
                else
                {
                    System.out.println("Неверный формат");
                    JLabel error = new JLabel("Неверный формат. Ожидаемый формат +7 или 8 и 10 цифр");
                    error.setForeground(Color.red);
                    resultPanel.add(error);
                    error.setVisible(true);
                    Launcher.goToForm(2);
                }
                /*try {
                    if  (Launcher.setTelephone(TelField.getText()))
                    {

                        Launcher.outMessage("Окно 1: Нажата кнопка. Телефон ОК");
                        Launcher.frame.setContentPane(Launcher.formCode.getRootPanel());
                        Launcher.frame.setVisible(true);
                    }
                    else
                    {
                        Launcher.outMessage("Окно 1: Нажата кнопка. Телефон WRONG");
                        ErText.setVisible(true);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Launcher.outMessage("Окно 1: Нажата кнопка. EXCEPTION");
                }*/
            }
        });
            CheckCodeBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (Integer.parseInt(InCode.getText()) == Code) {
                        System.out.println("Код ОК");
                        Launcher.goToForm(3);

                    } else {
                        System.out.println("Код не ок.");
                        JLabel error = new JLabel("Неверный код.");
                        error.setForeground(Color.red);
                        resultPanel.add(error);
                        Launcher.goToForm(2);
                    }
                }
            });
    }

    public JPanel getRootPanel() {
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
        LOGO = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(LogoIMG,0,0,null);
            }
        };
    }
}
