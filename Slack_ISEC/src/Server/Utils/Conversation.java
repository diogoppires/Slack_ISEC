package Server.Utils;

import java.io.Serializable;

public class Conversation implements Serializable {
    String userSender;
    String message;
    String userReceiver;

    public Conversation(String userSender, String message, String userReceiver) {
        this.userSender = userSender;
        this.message = message;
        this.userReceiver = userReceiver;
    }

    public String getUserSender() {
        return userSender;
    }

    public String getMessage() {
        return message;
    }

    public String getUserReceiver() {
        return userReceiver;
    }
}
