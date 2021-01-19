package controllers;


import org.springframework.web.bind.annotation.*;
import web.DataBase;

@RestController
@RequestMapping("messages")
public class Messages {

    @PostMapping("get")
    public String messages(
            @RequestParam(value = "number") int number) {
        var text = DataBase.searchMessages(number);
        System.out.println(text);
        return text;

    }

    @PostMapping("send")
    public String sendmessage(
            @RequestParam(value = "message") String text) {
        return text;
    }

}
