package zju.se.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }

    @GetMapping("/json")
    public Example JSONExample(@RequestParam(value = "name", defaultValue = "user") String name){
        return new Example(1, name);
    }
}
