package controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.Token;
import security.User;
import web.DataBase;

import javax.xml.crypto.Data;

@RestController
//@RequestMapping("user")
public class UserController
{


    @PostMapping("user/login")
    public User login(@RequestParam(value="username") String username, @RequestParam(value="password")String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        if (DataBase.loginUser(user.getUsername(), user.getPassword()))
        {
            String token = Token.getNewToken(user.getUsername());
            user.setToken(token);
            return user;
        }
        else {
            return null;
        }


    }


}