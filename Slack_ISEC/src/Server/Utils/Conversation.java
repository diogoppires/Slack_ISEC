package Server.Utils;

import java.io.Serializable;

public class Conversation implements Serializable {
   private int serverId;
   private String userSender;
   private String message;
   private String userReceiver;

    public Conversation(String userSender, String message, String userReceiver, int serverId) {
        this.serverId = serverId;
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

    public int getServerId() {
        return serverId;
    }
}
