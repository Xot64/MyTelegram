package Emulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class teleUser {
    private String LastName;
    private String FirstName;
    private String FatherName;
    private ArrayList<String> Numbers = new ArrayList<>();
    private HashMap<Integer, Friend> FriendList = new HashMap<Integer, Friend>();
    private ArrayList<String> FriendPhones = new ArrayList<>();
    private String Avatar = "None";

    public teleUser(String FN, String LN, String PN, String N, String A)
    {
        FirstName = FN;
        LastName = LN;
        FatherName = PN;
        Numbers.add(N);
        if (!A.equals("")) Avatar = A;


    }
    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getFatherName() {
        return FatherName.length() > 0? FatherName : "";
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public ArrayList<String> getNumbers() {
        return Numbers;
    }

    public void addNumber(String number) {
        Numbers.add(number);
    }

    public void deleteNumber(String number) {
        if (Numbers.contains(number)) Numbers.remove(number);
    }

    public HashMap<Integer, Friend> getFriendList ()
    {
        return FriendList;
    }

    public Friend getFriend(int ID) {
        return FriendList.get(ID);
    }

    public ArrayList<String> getFriendPhones() {
        return FriendPhones;
    }

    public void addFriend(String friend) {
        Friend f = new Friend(friend);
        FriendList.put(f.getID(),f);
        FriendPhones.add(f.getNumber());
    }

    public void RemoveFriend(int f)
    {
        FriendList.remove(f);
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }


}
