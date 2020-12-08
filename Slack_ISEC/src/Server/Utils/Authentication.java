package Server.Utils;

import java.io.Serializable;

public class Authentication implements Serializable {
    String username;
    String password;

    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
