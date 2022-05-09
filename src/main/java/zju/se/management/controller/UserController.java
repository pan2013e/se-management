package zju.se.management.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.authentication.TokenUtil;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/id/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/user/name/{name}")
    public User getUserByName(@PathVariable("name") String name) {
        return userService.getUserByName(name);
    }

    @PostMapping("/user")
    public Response addPatientUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password) {
        try{
            User user = new User();
            user.setUserName(userName);
            user.setPassword(CryptoUtil.encrypt(password));
            user.setRole(User.userType.PATIENT);
            userService.addUser(user);
            return new Response(0, null, "注册成功");
        } catch (Exception e) {
            return new Response(-1, null, "注册失败");
        }
    }

}
