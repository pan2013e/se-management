package zju.se.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class ViewController {

    @RequestMapping(value = {
            "/login",
            "/login/**"
    })
    public String index() {
        return "login/index";
    }

    @RequestMapping(value = {
            "/welcome/**",
            "/undefined/**",
    })
    public String welcome() { return "welcome/index"; }

}
