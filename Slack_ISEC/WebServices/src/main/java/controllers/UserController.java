package controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.Token;
import security.User;
import web.DataBase;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("user")
public class UserController {

    @PostMapping("login")
    public User login(@RequestBody User user) {
        if (DataBase.loginUser(user.getUsername(), user.getPassword())) {
            String token = Token.getNewToken(user.getUsername());
            user.setToken(token);
            return user;
        } else {
            return null;
        }


    }


}