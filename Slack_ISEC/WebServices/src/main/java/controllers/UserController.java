package controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public User login(@RequestBody User user) {
        // TODO: Login with database (check username and password)?
        String token = Token.getNewToken(user.getUsername());
        user.setToken(token);
        return user;
        /*

        if (DataBase.loginUser(user.getUsername(), user.getPassword()))
        {
            String token = Token.getNewToken(user.getUsername());
            user.setToken(token);
            return user;
        }
        else {
            return null;
        }

         */
    }


}