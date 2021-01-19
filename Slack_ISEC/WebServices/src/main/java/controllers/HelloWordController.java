package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWordController {

    @GetMapping("/hello-world")
    public String helloWorld(@RequestParam(value = "name", required = false /*,defaultValue = World"*/) String name,
                             @RequestParam(value = "idade") Integer idade) {

        return "Hello " + (name == null ? "World" : name) + idade + "!";
    }

}
