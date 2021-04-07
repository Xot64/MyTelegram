import Emulator.Emulator;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Launcher {

    /*private static TelegramApiBridge bridge;
    static {
        try {
            bridge = new TelegramApiBridge("149.154.167.50:443", 515552, "29444894ac2b6aa681eac75642e77884");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static TelInput formTel = new TelInput();
    public static Welcome formWelcome = new Welcome();
    public static UserSettings formUser = new UserSettings();
    public static TelBook formBook = new TelBook();
    public static DialogForm formDialog;
    public static JFrame frame = new JFrame();
    public static int Target = 0;

    public static void main(String[] args) throws IOException, ParseException {
        //Имитация входа с выбранного устройства

        System.out.println("Это тестовая программа имитирует работу сервера телеграм. Для имитации входа введите ID устроства. \n\tПроизвольное число - симитировать вход с нового устройства \n\tИзвестное число - имитация входа с зарегистрировнного устройства.");
        Emulator.StartEmulator();
        ArrayList connetors = Emulator.getTargets();
        if (connetors.size() > 0)
        {
            System.out.println("Зарегистрированные устройства:");
            for (int i = 0; i < connetors.size(); i++)
            {
                System.out.print(connetors.get(i) + " \t");
                if ((i+1) % 10 == 0) System.out.println();
            }
            System.out.println();
        }
        else
        {
            System.out.println("Нет заригистрированных устройств");
        }
        BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
        Target = Integer.parseInt(BR.readLine());
        //String Target = "1002";
        boolean NewTarget = !connetors.contains(Target);

        System.out.println(NewTarget ? "Новое устройство" : "Устройство заригистрировано");
        /*----------Запуск окна------------*/

        Dimension Dim = new Dimension(800,600);
        frame.setSize(Dim);
        frame.setResizable(false);
        if (NewTarget)
        {
            goToForm(2);
        }
        else
        {
            Emulator.activeUserID = Emulator.getTargetUserID(Target);
            goToForm(1);
        }
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Мой телеграмм");


        /*BufferedReader RL = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Фамилия Имя:");
        String FI = RL.readLine();
        String Name [] = FI.trim().replaceAll("[^a-zA-Zа-яА-Я]+"," ").split(" ");
        System.out.println("Фамилия: " + Name[0] + "\nИмя: " + Name[1]);
        System.out.println("Введите номер телефона");
        String Phone = RL.readLine().replaceAll("[^0-9]","");
        System.out.println(Phone);
        AuthCheckedPhone checkedPhone = bridge.authCheckPhone(Phone);
        System.out.println("Номер " + (checkedPhone.isRegistered() ? "" : "не ") + "зарегистрирован");
        System.out.println("Номер " + (checkedPhone.isInvited() ? "" : "не ") + "приглашен");
        bridge.authSendCode(Phone);
        System.out.println("Введите код");
        AuthAuthorization authorization = bridge.authSignIn(RL.readLine().trim());
        System.out.println(authorization.getUser().getFirstName() + " " + authorization.getUser().getLastName());
        ArrayList <UserContact> PhoneList = new ArrayList<>();
        PhoneList = bridge.contactsGetContacts();
        System.out.println("Контактов в списке: " + PhoneList.size());
        for (UserContact item : PhoneList)
        {
            System.out.println(item.getFirstName() + " " + item.getLastName() + " " + item.getPhone());
        }
*/

    }
    public static void goToForm (int FN) {
        System.out.println("Открыть окно №" + FN);
        switch (FN)
        {
            case 1:
                frame.setContentPane(formWelcome.getRootPanel());
                break;
            case 2:
                frame.setContentPane(formTel.getRootPanel());
                break;
            case 3:
                frame.setContentPane(formUser.getRootPanel());
                break;
            case 4:
                formDialog = new DialogForm();
                frame.setContentPane(formDialog.getRootPanel());
                break;
            case 5:
                break;
            case 6:
                break;
        }
        frame.setVisible(true);
    }
    public static boolean setTelephone (String TelNum) throws IOException {
        String Phone = TelNum.replaceAll("[^0-9]","");
        if (Phone.length() == 11)
        {
            /*AuthCheckedPhone checkedPhone = bridge.authCheckPhone(Phone);
            bridge.authSendCode(Phone);*/
            return true;
        }
        else
        {
            return false;
        }

    }
    public static void sendCode (String Code) throws IOException {
       // AuthAuthorization authorization = bridge.authSignIn(Code);
    }

 /*   public static ArrayList<UserContact> getTelBook () throws IOException {
       // return bridge.contactsGetContacts();
    }*/
    public static void outMessage (String msg)
    {
        System.out.println(msg);
    }

}


