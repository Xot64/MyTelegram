import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TelBook {
     private JPanel rootPanel;
    private JTextArea FriendList;

    public JPanel getRootPanel() throws IOException {
    /*    ArrayList<UserContact> FL = Launcher.getTelBook();
        for (UserContact item : FL) {
            FriendList.append(item.getFirstName() + " " + item.getLastName() + " - " + item.getPhone());
        }*/
        return rootPanel;
    }

}
