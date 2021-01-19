package Server.Utils;

import java.io.Serializable;

public class Register implements Serializable {
    private int serverId;
    private String name;
    private String username;
    private String password;
    private String photoPath;

    public Register(String name, String username, String password, String photoPath, int serverId) {
        this.serverId = serverId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.photoPath = photoPath;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public int getServerId() {
        return serverId;
    }
}
