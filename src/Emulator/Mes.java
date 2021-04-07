package Emulator;

import java.io.BufferedReader;
import java.sql.Time;
import java.util.Date;

public class Mes {
    private String date = new String();
    private String time = new String();
    private int SenderID;
    private int status;
    private String Text = new String();

    public Mes (String date, String time, int Sender, int Status, String Text)
    {
        this.date = date;
        this.time = time;
        this.SenderID = Sender;
        this.status = Status;
        this.Text = Text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getSenderID() {
        return SenderID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return Text;
    }
}
