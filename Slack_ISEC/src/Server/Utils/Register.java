package Server.Utils;

import java.io.Serializable;

public class Register implements Serializable {
    String name;
    String username;
    String password;
    String photoPath;

    public Register(String name, String username, String password, String photoPath) {
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
}
