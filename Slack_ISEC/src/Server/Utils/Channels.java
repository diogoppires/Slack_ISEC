package Server.Utils;

import java.io.Serializable;

public class Channels implements Serializable {
    private int serverId;

    private String chName;
    private String chDescription;
    private String chPassword;
    private String chUserAdmin;
    private int type;

    public Channels(String chName, String chDescription, String chPassword, String chUserAdmin, int type, int serverId){
        this.serverId = serverId;
        this.chName = chName;
        this.chDescription = chDescription;
        this.chPassword = chPassword;
        this.chUserAdmin = chUserAdmin;
        this.type = type;
    }

    public int getServerId() {
        return serverId;
    }

    public String getChName() {
        return chName;
    }

    public String getChDescription() {
        return chDescription;
    }

    public String getChPassword() {
        return chPassword;
    }

    public String getChUserAdmin() {
        return chUserAdmin;
    }

    public int getType() {
        return type;
    }
}
