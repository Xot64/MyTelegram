package Emulator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.Launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Friend {
    private int ID = 0;
    private String Number = new String();
    private String SaveAs = new String();
    private ArrayList <Mes> Dialog = new ArrayList<>();
    private boolean OpenDialog = false;
    private teleUser FU;

    public Friend (String FriendText)
    {
        this.Number = FriendText.substring(0, 10);
        this.ID = Emulator.CheckNumber(FriendText.substring(0,10));
        this.SaveAs = FriendText.substring(11).trim();
        FU = Emulator.getUser(ID);
    }

    private void refreshMessages (int UserId) throws IOException, ParseException {
        String path = "Messages/List" + ID + "-" + UserId + ".JSON";
        JSONParser pars = new JSONParser();
        JSONObject obj = (JSONObject) pars.parse(new String(Files.readAllBytes(Paths.get(path))));
        Dialog.clear();
        for (Object key : obj.keySet())
        {
            JSONObject value = (JSONObject) obj.get(key);
            Mes m = new Mes(value.get("Date").toString(),value.get("Time").toString(),Integer.parseInt(value.get("From").toString()),Integer.parseInt(value.get("Status").toString()),value.get("Text").toString());
  //          Phone.put(value.get("Phone").toString(), value.get("Name").toString());
        }
 //       continue;

    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Integer getID() {
        return ID;
    }


    public String getSaveAs() {
        return SaveAs;
    }

    public void setSaveAs(String NewSaveAs) {
        SaveAs = NewSaveAs;
    }

    public ArrayList<Mes> getDialog() {
        return Dialog;
    }

    public void setDialog(ArrayList<Mes> dialog) {
        Dialog = dialog;
    }
    public void addToDialog(Mes mes) {
        Dialog.add(mes);
    }

    public teleUser getFU() {
        return FU;
    }
}
