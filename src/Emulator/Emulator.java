package Emulator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Emulator {


    public static TreeMap <Integer, Integer> registred = new TreeMap<>(); //Список зарегистрированных устройств <ID Устройства, ID порльзователя>
    public static TreeMap <Integer, teleUser> UserList = new TreeMap<>();  //Список зарегистрированных пользовательей <ID, Пользователь>
    public static TreeMap<Integer, Friend> friends = new TreeMap<>();//Список друзей <ID, Друг>
    public static TreeMap<Integer,Boolean> Dialogs = new TreeMap<>();//Список активных диалогов <ID собеседника, наличие>

    private static String conTarPath = "src\\Emulator\\Base\\Connectors.json";
    private static String userPath = "src\\Emulator\\Base\\Users.json";
    public static int activeUserID = -1;


    public Emulator()
    {

    }
    public static void StartEmulator() throws IOException, ParseException {
        updateTarget();
        updateUserList();
    }
//------------------------Работа с соединениями---------------------------------------------------------
    //Обновить список устройств из файла
    public static void updateTarget () throws IOException, ParseException {
        JSONParser pars = new JSONParser();
        JSONObject obj = (JSONObject) pars.parse(new String(Files.readAllBytes(Paths.get(conTarPath))));
        for(Object key :obj.keySet())
        {
            JSONObject value = (JSONObject) obj.get(key);
            registred.put(Integer.parseInt(key.toString()), Integer.parseInt(value.get("UserID").toString()));
        }
    }

    //Вывести список устройств
    public static ArrayList<Integer> getTargets () {
        ArrayList<Integer> targets = new ArrayList<>();
        for(Map.Entry<Integer,Integer> entry : registred.entrySet()) {
            targets.add(entry.getKey());
        }
        return targets;
    }

    //Сохранить связи Устройство-Пользователь в файл
    public static void reWriteTargets () throws FileNotFoundException {
        // Подготовка файла к записи
        File f = new File(conTarPath);
        f.mkdirs();
        PrintWriter BW = new PrintWriter(conTarPath);
        // Запись в файл
        BW.write("\t{\n");
        for (Map.Entry<Integer, Integer> entry : registred.entrySet()) {
            BW.write("\t\"" + entry.getKey() + "\" : {\n");
            BW.write("\t\t\"UserID\":\"" + entry.getValue() + "\"\n");
            BW.write("\t},\n");
        }
        BW.write("}");
        System.out.println("Добавлено новое соединение");
        BW.flush();
        BW.close();
    }

    //Добавить связь Устройство-Пользователь
    public static void addTarget (int targetId, int user) throws IOException, ParseException {
        // Актуализировать существующий список
        updateTarget();
        //Добавить новое /обновить устройство
        if (registred.containsKey(targetId)) registred.replace(targetId,user);
        else registred.put(targetId,user);
        //Внести изменение в файл данных
        reWriteTargets();
    }

    //Вывести ID пользователя данного устройства
    public static int getTargetUserID(int Target)
    {
        return registred.get(Target);
    }

//------------------------Работа с пользователями---------------------------------------------------------
    //Обновить список зарегистрированных пользователей из файла
    public static void updateUserList () throws IOException, ParseException {
        //Очистка списка зарегистрированных пользователей
        UserList.clear();

        JSONParser pars = new JSONParser();
        JSONObject obj = (JSONObject) pars.parse(new String(Files.readAllBytes(Paths.get(userPath))));
        for(Object key :obj.keySet())
        {
            JSONObject value = (JSONObject) obj.get(key);
            JSONArray numbers = (JSONArray) value.get("Numbers");
            teleUser u = new teleUser(value.get("FirstName").toString(),value.get("LastName").toString(),(value.get("FatherName").toString().length() > 0 ? value.get("FatherName").toString() : ""),numbers.get(0).toString(),value.get("Avatar").toString());
            for (int i = 1; i < numbers.size(); i++)
            {
                u.addNumber(numbers.get(i).toString());
            }
            UserList.put(Integer.parseInt(key.toString()),u);
        }

        for(Object key :obj.keySet())
        {
            JSONObject value = (JSONObject) obj.get(key);
            JSONArray friendsNumbers = (JSONArray) value.get("Friends");
            for (int i = 0; i < friendsNumbers.size(); i++)
            {
                UserList.get(Integer.parseInt(key.toString())).addFriend(friendsNumbers.get(i).toString());
            }
        }
    }

    //Вывести подключенного пользователя
    public static teleUser getActiveUser ()
    {
        return UserList.get(activeUserID);
    }

    //Сохранение списка пользователей в файл
    public static void saveUserList () throws IOException, ParseException {
        File f = new File(userPath);
        f.mkdirs();
        PrintWriter BW = new PrintWriter(userPath);
        BW.write("\t{\n");
        for(Map.Entry<Integer, teleUser> entry : UserList.entrySet()) {
            teleUser u = entry.getValue();
            BW.write("\t\"" + entry.getKey() + "\" : {\n");
            BW.write("\t\t\"FirstName\":\"" +  u.getFirstName() + "\",\n");
            BW.write("\t\t\"LastName\":\"" +  u.getLastName() + "\",\n");
            BW.write("\t\t\"FatherName\":\"" +  u.getFatherName() + "\",\n");
            BW.write("\t\t\"Numbers\":[\"");
            for (int i = 0; i < u.getNumbers().size(); i++) {
                BW.write(u.getNumbers().get(i));
                if (i < u.getNumbers().size() - 1) BW.write("\",\"");
            }
            BW.write("\"]\n");
            BW.write("\t\t\"Avatar\":\"" +  u.getAvatar() + "\",\n");
            BW.write("\t\t\"Friends\":[\"");

            Iterator<Map.Entry<Integer, Friend>> itr = u.getFriendList().entrySet().iterator();
            while (itr.hasNext())
            {
                Friend F = itr.next().getValue();
                BW.write(F.getNumber() + " " + F.getSaveAs());
                if (itr.hasNext())
                {
                    BW.write("\",\"");
                }
            }

            BW.write("\"]\n\t\t},\n");
        }
        BW.write("}");
        System.out.println("Добавлен новый пользователь");
        BW.flush();
        BW.close();
    }

    //Вывести ID подключенного пользователя
    public static int getActiveID ()
    {
        return activeUserID;
    }

    public static void addUser (int id, String FN, String LN, String PN, String N)
    {
        UserList.put(id, new teleUser(FN,LN,PN,N,""));
        try {
            saveUserList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //Изменение параметров подключенного пользователя
    public static int setUser (String FN, String LN, String PN, String N, String A) throws IOException, ParseException {
        teleUser user = new teleUser(FN, LN, PN, N, A);
        int id = -1;
        if (activeUserID >= 0)
        {
            id = activeUserID;
            UserList.get(activeUserID).setFirstName(FN);
            UserList.get(activeUserID).setLastName(LN);
            UserList.get(activeUserID).setFatherName(PN);
        }
        else
        {
            id = UserList.lastKey() + 1;
            UserList.put(id,user);
            activeUserID = id;
        }
        saveUserList();
        return id;
    }

    //Вывести ID пользователя с номером Number, если не найлен, то -1
    public static int CheckNumber (String Number) {
        for(Map.Entry<Integer, teleUser> entry : UserList.entrySet()) {
            for (int i = 0; i < entry.getValue().getNumbers().size();i++)
            {
                if (entry.getValue().getNumbers().get(i).equals(Number)) return entry.getKey();
            }
        }
        return -1;
    }

    //Вывести ID пользователя, если не найлен, то -1
    public static int getUserKey (teleUser user) {
        for(Map.Entry<Integer, teleUser> entry : UserList.entrySet()) {
            if (entry.getValue() == user) return entry.getKey();
        }
        return -1;
    }

    //Вывести пользователя с указанным ID
    public static teleUser getUser (int ID)
    {
        return UserList.containsKey(ID) ? UserList.get(ID) : null;
    }

    //Вывести диалоги пользователя с ID
    public static TreeMap<Integer, Mes> getMessages (int ID) {
        TreeMap<Integer,Mes> ALM = new TreeMap<>();
        String MesPath = "src\\Emulator\\Base\\Dialog_" + Math.min(activeUserID, ID) + "_" + Math.max(activeUserID, ID) + "_.json";
        File f = new File(MesPath);
        int nm = 0;
        if (f.exists())
        {
            JSONParser pars = new JSONParser();
            JSONObject obj = null;
            try {
                obj = (JSONObject) pars.parse(new String(Files.readAllBytes(Paths.get(MesPath))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(Object key :obj.keySet())
            {
                JSONObject value = (JSONObject) obj.get(key);
                Mes m = new Mes(value.get("Date").toString(),value.get("Time").toString(),Integer.parseInt(value.get("From").toString()),Integer.parseInt(value.get("Status").toString()),value.get("Text").toString());
                ALM.put(nm,m);
                nm++;
            }
        }
        return ALM;
    }
    public static void saveMessages (TreeMap<Integer,Mes> MM, int SendTo) {
        String MesPath = "src\\Emulator\\Base\\Dialog_" + Math.min(activeUserID, SendTo) + "_" + Math.max(activeUserID, SendTo) + "_.json";
        File f = new File(MesPath);
        int nm = 0;
        if (!f.exists()) {
            f.mkdirs();
        }

        PrintWriter BW = null;
        try {
            BW = new PrintWriter(MesPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BW.write("\t{\n");
        for(Map.Entry<Integer, Mes> entry : MM.entrySet()) {
            Mes u = entry.getValue();
            BW.write("\t\"" + entry.getKey() + "\" : {\n");
            BW.write("\t\t\"Date\":\"" +  u.getDate() + "\",\n");
            BW.write("\t\t\"Time\":\"" +  u.getTime() + "\",\n");
            BW.write("\t\t\"From\":\"" +  u.getSenderID() + "\",\n");
            BW.write("\t\t\"Status\":\"" +  u.getStatus() + "\",\n");
            BW.write("\t\t\"Text\":\"" +  u.getText() + "\",\n");
            BW.write("\t\t},\n");
        }
        BW.write("}");
        System.out.println("Сообщения сохранены");
        BW.flush();
        BW.close();
    }
    public static Friend getFriend(int ID) {
        return friends.get(ID);
    }

//------------------------Работа с ---------------------------------------------------------
}
