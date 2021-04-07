
import Emulator.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DialogForm {
    private JPanel rootPanel;
    private JPanel LogoPanel;
    private JPanel DialogListPanel;
    private JPanel FindPanel;
    private JPanel FriendInfo;
    private JPanel FriendSet;
    private JPanel Logo;
    private JPanel UserPanel;
    private JPanel findIco;
    private JTextField textFind;
    private JTextField MessageField;
    private JPanel SendMessageButton;
    private JPanel SendPanel;
    private JPanel DialogPanel;
    private JPanel SenderAvatar;
    private JLabel SenderName;
    private JPanel FriendPanel;
    private JPanel ADD;
    private JScrollPane DialogScroll;

    Color BG_Blue = new Color(0,179,230);
    Color BG_Purple = new Color(74, 68, 168);
    Color BG_Gray = new Color(230,230,230);
    Color BG_White = new Color(255,255,255);
    Color M_Blue = new Color(1, 167, 217);
    Color T_Black = new Color(0,0,0);
    Color T_Gray = new Color(160,160,160);
    Color B_Gray = new Color(200,200,200);

    static int checkDialog = 0;

    private static TreeMap <Integer, JPanel> dialogPanelList = new TreeMap<>();
    private static TreeMap <Integer, Mes> MessageList = new TreeMap<>();

    private BufferedImage LogoIMG, UserSet, FindIcon, SendButtonIcon, FSet, SA;
    private BufferedImage MOTop, MOBottom, MOSend, MITop, MIBottom, MISend;

    public DialogForm() {
        try {
            LogoIMG = ImageIO.read(new File("GUIComponents/logo-micro.png"));
            UserSet = ImageIO.read(new File("GUIComponents/icon-settings.png"));
            FindIcon = ImageIO.read(new File("GUIComponents/icon-search.png"));
            SendButtonIcon = ImageIO.read(new File("GUIComponents/button-send.png"));
            FSet = ImageIO.read(new File("GUIComponents/icon-edit.png"));

            MOTop = ImageIO.read(new File("GUIComponents/message-out-top.png"));
            MOBottom = ImageIO.read(new File("GUIComponents/message-out-bottom.png"));
            MOSend = ImageIO.read(new File("GUIComponents/message-out-right.png"));
            MITop = ImageIO.read(new File("GUIComponents/message-in-top.png"));
            MIBottom = ImageIO.read(new File("GUIComponents/message-in-bottom.png"));
            MISend = ImageIO.read(new File("GUIComponents/message-in-left.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }

        //Панель диалогов
        createDialogList("");
        //Панель пользователя
        teleUser ME = Emulator.getActiveUser();
        Avatar usAva = new Avatar(ME.getAvatar(), 2, false, "S");
        UserPanel.add(usAva);
        JLabel FI = new JLabel(ME.getFirstName() + " " + ME.getLastName());
        FI.setForeground(BG_White);
        UserPanel.add(FI);
        JPanel panelUserSet = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(UserSet, 0, 0, null);
            }
        };
        panelUserSet.setOpaque(false);
        panelUserSet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Launcher.goToForm(3);
                //Launcher.goToForm(5);
            }
        });
        panelUserSet.setPreferredSize(new Dimension(21, 21));
        UserPanel.add(panelUserSet);
        UserPanel.setPreferredSize(new Dimension(600, 30));

        //Панель поиска-------------------------------------------------------------------------
        textFind.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, BG_Blue));
        textFind.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                createDialogList(textFind.getText().trim().toUpperCase());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                createDialogList(textFind.getText().trim().toUpperCase());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        //Ввод сообщения
        MessageField.setBorder(BorderFactory.createMatteBorder(0,0,0,0, T_Black));
        //Диалог

        showDialog(checkDialog);
        /*LO.putConstraint(SpringLayout.SOUTH,panel1,-10,SpringLayout.SOUTH,DialogPanel);
        LO.putConstraint(SpringLayout.EAST,panel1,-10,SpringLayout.EAST,DialogPanel);*/


        FriendSet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                FriendSettings FS = new FriendSettings(checkDialog);
                FS.DelUser.setVisible(true);
                Launcher.frame.setContentPane(FS.getRootPanel());
                Launcher.frame.setVisible(true);
            }
        });
        UserPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Launcher.goToForm(3);
            }
        });
        ADD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                FriendSettings FS = new FriendSettings(0);
                FS.DelUser.setVisible(false);
                Launcher.frame.setContentPane(FS.getRootPanel());
                Launcher.frame.setVisible(true);
            }
        });
        SendMessageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Calendar C = GregorianCalendar.getInstance();
                String D = C.get(Calendar.DATE) + "-" + C.get(Calendar.MONTH + 1) + "-" + C.get(Calendar.YEAR);
                String T = C.get(Calendar.HOUR) + ":" + C.get(Calendar.MINUTE) + ":" + C.get(Calendar.SECOND);
                Mes m = new Mes(D,T, Emulator.getActiveID(), 3, MessageField.getText());
                MessageList.put(MessageList.size() + 1,m);
                Emulator.saveMessages(MessageList, checkDialog);
                showDialog(checkDialog);
                MessageField.setText("");
            }
        });
    }
    private void createDialogList(String search) {
        //Панель диалогов---------------------------------------------
        boolean find = search.length() > 0;
        dialogPanelList.clear();
        DialogListPanel.removeAll();
        teleUser u1 = Emulator.getActiveUser();
        int ID1 = Emulator.getUserKey(u1);
        for(Map.Entry<Integer, Friend> entry : Emulator.getActiveUser().getFriendList().entrySet()) {
            Friend u = entry.getValue();
            if (u.getID() != Emulator.getActiveID()) {
                Emulator.Dialogs.clear();
                int ID2 = u.getID();
                if (checkDialog == 0) checkDialog = ID2;
                File f = new File("src\\Emulator\\Base\\Dialog_" + Math.min(ID1, ID2) + "_" + Math.max(ID1, ID2) + "_.json");
                if (((!find) && (f.exists())) || ((find) && ((u.getSaveAs().toUpperCase().contains(search))))) {
                    createDialog(u.getID(), u.getFU().getAvatar(), ID2 == checkDialog, Math.random() > 0.5);
                    DialogListPanel.add(dialogPanelList.get(ID2));
                    dialogPanelList.get(ID2).addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            checkDialog = ID2;
                            createDialogList("");
                            showDialog(ID2);
                        }
                    });
                }

                Emulator.Dialogs.put(u.getID(), f.exists());
                System.out.println(ID1 + " и " + ID2 + " " + (f.exists() ? "" : "не ") + "существует");
            }
        }
        DialogListPanel.updateUI();
    }

    private void showDialog(int friendID)
    {
        int MW = 307;
        MessageList.clear();
        MessageList.putAll(Emulator.getMessages(checkDialog));
        DialogPanel.removeAll();
        try {
            Emulator.getActiveUser().getFriend(friendID).getSaveAs();
        }
        catch (Exception e)
        {
            friendID = 0;
            HashMap<Integer, Friend> FL = Emulator.getActiveUser().getFriendList();
            for(Map.Entry<Integer, Friend> entry : Emulator.getActiveUser().getFriendList().entrySet()) {
                friendID = friendID == 0 ? entry.getKey() : friendID ;
            }
        }
        try {
            SA = ImageIO.read(new File("GUIComponents/Avatar/AvatarM/"+ Emulator.UserList.get(friendID).getAvatar() + "_41.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SenderName.setText(Emulator.getActiveUser().getFriend(friendID).getSaveAs());
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        //***********************Менеджеры компоновки**********************
        // DialogPanel.setLayout(new GridLayout(0, 1));
        DialogPanel.setLayout(gbl);
        //*****************************************************************
        for (int i = 0; i < MessageList.size(); i++) {
            gbc.gridy = i;
            Mes mes = MessageList.get(i);
            if (mes == null) continue;
            boolean MO = Emulator.getActiveID() == mes.getSenderID();
            //<Панель сообщения>---------
            JPanel MessagePanel = new JPanel();
            MessagePanel.setAlignmentX(MO ? Panel.RIGHT_ALIGNMENT : Panel.LEFT_ALIGNMENT);
            MessagePanel.setLayout(new BorderLayout());
            JPanel MessageCloud = new JPanel();
            MessageCloud.setOpaque(false);

            MessageCloud.setLayout(new BorderLayout(0,0));
            // <Облачко слева/справа>----------
            JPanel PSend = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (MO) g.drawImage(MOSend, 0, 0, null);
                    else g.drawImage(MISend, 0, 0, null);
                }
            };
            PSend.setPreferredSize(new Dimension(7,11));
            PSend.setOpaque(false);
            //----------</Облачко слева/справа>

            //<Текст в облачке>----------
            JTextArea MesText = new JTextArea();
            MesText.setText(mes.getText());
            MesText.setMargin(new Insets(0,(MO ? 4 : 3) ,0,(MO ? 3 : 4)));
            MesText.setDisabledTextColor(BG_White);
            MesText.setEnabled(false);
            Dimension dim = MesText.getPreferredSize();

            dim.height = dim.height * (dim.width/(MW - 7) + 1) + 2;
            dim.width = MW;
            MesText.setPreferredSize(dim);
            //MesText.setPreferredSize(new Dimension(317, MesText.getPreferredSize().height + 2));
            MesText.setLineWrap(true);

            if (MO){
                MessageCloud.add(PSend,BorderLayout.EAST);
                MesText.setBackground(BG_Purple);
            }
            else {
                MessageCloud.add(PSend,BorderLayout.WEST);
                MesText.setBackground(M_Blue);

            }
            MessageCloud.add(MesText,BorderLayout.CENTER);


            //-----------</Облачко Текст/Центр>

            //<Облачко верх>-----------
            JPanel PTop = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (MO) g.drawImage(MOTop, 0, 0, null);
                    else g.drawImage(MITop, 0, 0, null);
                }
            };
            PTop.setPreferredSize(new Dimension(MW + 7,8));
            PTop.setOpaque(false);
            MessageCloud.add(PTop,BorderLayout.NORTH);
            //-----------</Облачко верх>

            // <Облачко низ>-----------
            JPanel PBottom = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (MO) g.drawImage(MOBottom, 0, 0, null);
                    else g.drawImage(MIBottom, 0, 0, null);
                }
            };
            MessageCloud.add(PBottom,BorderLayout.SOUTH);
            PBottom.setOpaque(false);
            //-----------</Облачко низ>
            MessageCloud.setPreferredSize(new Dimension(MW + 7,20 + MesText.getPreferredSize().height));
            //MessageCloud.setSize(new Dimension(314,MessageCloud.getHeight()));
            //----------</Облачко>
            //<Информация о сообщении>----------
            String MesInfo = mes.getDate().replaceAll("-", ".") + " " + mes.getTime();
            if (MO) {
                switch (mes.getStatus()) {
                    case 0:
                        MesInfo = MesInfo + ": Отправляется";
                        break;
                    case 1:
                        MesInfo = MesInfo + ": Отправлено";
                        break;
                    case 2:
                        MesInfo = MesInfo + ": Доставлено";
                        break;
                    case 3:
                        MesInfo = MesInfo + ": Прочитано";
                        break;
                }
            }
            JLabel DT = new JLabel("      " + MesInfo + "      ");
            DT.setOpaque(false);
            DT.setHorizontalAlignment(MO ? SwingConstants.RIGHT : SwingConstants.LEFT);
            DT.setForeground(T_Gray);
            DT.setBackground(new Color(0,255,0));
            //-----------</Информация о сообщении>
            JLabel SB = new JLabel();
            SB.setPreferredSize(new Dimension(20,0));
            SB.setOpaque(false);
            SB.setBackground(new Color(255,0,0));
            JPanel NP = new JPanel();
            int W1, W2, W3, W4, W5, W6;
            W1 = Launcher.frame.getPreferredSize().width;
            W2 = DialogListPanel.getPreferredSize().width;
            W3 = MessageCloud.getPreferredSize().width;
            W4 = SB.getPreferredSize().width;
            W5 = W1 - W2 - W3 - W4 - 16 - 20;
            W6 = DialogPanel.getPreferredSize().width;
            NP.setPreferredSize(new Dimension(Launcher.frame.getPreferredSize().width - DialogListPanel.getPreferredSize().width - MessageCloud.getPreferredSize().width - SB.getPreferredSize().width - 30 - 16, 0));
            NP.setOpaque(false);
            NP.setBackground(new Color(0,0,255));

            MessagePanel.add(NP, MO ? BorderLayout.WEST : BorderLayout.CENTER);
            MessagePanel.add(MessageCloud, MO ? BorderLayout.CENTER : BorderLayout.WEST);
            //MessagePanel.add(SB, BorderLayout.EAST);
            MessagePanel.add(DT,BorderLayout.SOUTH);

            MessagePanel.setAlignmentX(MO ? Panel.RIGHT_ALIGNMENT: Panel.LEFT_ALIGNMENT);
            MessagePanel.setOpaque(false);
            //MessagePanel.setSize(new Dimension(800,MessageCloud.getPreferredSize().height + 20));
            //MessagePanel.setBackground(new Color(i*10,i*10, i*10));
            //----------</Панель сообщения>
            DialogPanel.add(MessagePanel);
            gbl.setConstraints(MessagePanel, gbc);
            FriendPanel.updateUI();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createDialog(int ID, String AvatarPath, boolean Checked, boolean online)
    {
        JPanel panel1 = new JPanel();//Создается новая панель
        panel1.setBorder(BorderFactory.createMatteBorder(1,1,1,1,B_Gray));
        panel1.setBackground(Checked ? BG_White : BG_Gray);//Панели задается цвет
        panel1.setPreferredSize(new Dimension(301,43));//Панели задаются размеры
        panel1.setLayout(new FlowLayout(0,0,0));//Панели задается Layout
        dialogPanelList.put(ID,panel1);//Панель помещается в список диалогов

        Avatar avatar = new Avatar (AvatarPath, Checked ? 0 : 1, online, "M"); // Создается аватар
        //avatar.setPreferredSize(new Dimension(41,41));//Аватару задаются размеры
        panel1.add(avatar);//Аватар помещается на панель

        JPanel panel2 = new JPanel();//Создается новая панель с предпоказом
        panel2.setPreferredSize(new Dimension(258,41));
        panel2.setOpaque(false);
        panel2.setLayout(new FlowLayout(0,3,0));
        Friend F = Emulator.getActiveUser().getFriend(ID);
        JLabel FI = new JLabel(F.getSaveAs());
        FI.setPreferredSize(new Dimension(258,20));
        FI.setOpaque(false);
        panel2.add(FI);
        if (Checked) panel2.setBorder(BorderFactory.createMatteBorder(0,0,0,3,BG_Blue));
        JLabel LastMessage = new JLabel("Последнее сообщение");
        LastMessage.setPreferredSize(new Dimension(240,20));
        LastMessage.setForeground(T_Gray);
        LastMessage.setOpaque(false);
        panel2.add(LastMessage);
        panel1.add(panel2);
        dialogPanelList.put(ID,panel1);
    }



    private void createUIComponents() {
        Logo = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(LogoIMG,0,0,null);
            }
        };
        findIco = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(FindIcon,0,0,null);
            }
        };
        SendMessageButton = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(SendButtonIcon,0,0,null);
            }
        };
        FriendSet = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(FSet,11,11,null);
            }
        };
        SenderAvatar = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(SA,5,0,null);
            }
        };
    }

//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------


    class Avatar extends JPanel{
        String ImagePath;
        String MaskPath;
        public Avatar (String Image, int color, boolean online, String size)
        {
            int NumSize = 0;
            if (size == "S") NumSize = 29;
            if (size == "M") NumSize = 41;
            if (size == "L") NumSize = 66;
            if (size == "XL") NumSize = 160;
            this.ImagePath = "GUIComponents/Avatar/Avatar" + size + "/" + Image + "_" + NumSize + ".png";
            this.MaskPath = "GUIComponents/Avatar/Mask" + size + "/Mask_";
            switch (color)
            {
                case 0:
                    MaskPath = MaskPath + "White";
                    break;
                case 1:
                    MaskPath = MaskPath + "Gray";
                    break;
                case 2:
                    MaskPath = MaskPath + "Blue";
                    break;
            }
            MaskPath = MaskPath + (online ? "On": "") + "_"+ NumSize + ".png";
            setPreferredSize(new Dimension(NumSize,NumSize));
        }
        public void paintComponent(Graphics g){
            Image im = null;
            try {
                im = ImageIO.read(new File(ImagePath));
            } catch (IOException e) {
                System.out.println("Аватар по адресу:" + ImagePath + " не найден");
            }
            g.drawImage(im, 0, 0, null);
            try {
                im = ImageIO.read(new File(MaskPath));
            } catch (IOException e) {
                System.out.println("Маска по адресу:" + MaskPath + " не найдена");
            }
            g.drawImage(im, 0, 0, null);
        }
    }
}
