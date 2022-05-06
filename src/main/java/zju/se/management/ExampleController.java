package zju.se.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/test")
    String test(){
        return "Hello world";
    }
}
