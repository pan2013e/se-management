package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;
import zju.se.management.utils.UserListResponseData;
import zju.se.management.utils.UserNotFoundException;
import zju.se.management.utils.UserResponseData;

@RestController
@RequestMapping("/api/privileged")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public Response getUser() {
        return new Response(0, new UserListResponseData(userService.getAllUsers()), "查询成功");
    }

    @GetMapping("/user/id/{id}")
    public Response getUserById(@PathVariable("id") Integer id) {
        try {
            User user = userService.getUserById(id);
            return new Response(0, new UserResponseData(user), "查询成功");
        } catch (Exception e) {
            return new Response(-1, null, e.getMessage());
        }
    }

    @GetMapping("/user/name/{name}")
    public Response getUserByName(@PathVariable("name") String name) throws UserNotFoundException {
        try {
            User user = userService.getUserByName(name);
            return new Response(0, new UserResponseData(user), "查询成功");
        } catch (Exception e) {
            return new Response(-1, null, e.getMessage());
        }
    }

    @PostMapping("/user")
    public Response addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "role") String role) {
        try {
            userService.getUserByName(userName);
            User user = new User();
            user.setUserName(userName);
            user.setPassword(CryptoUtil.encrypt(password));
            user.setRole(User.userType.valueOf(role));
            userService.addUser(user);
            return new Response(0, null, "注册成功");
        } catch (Exception e) {
            return new Response(1, null, e.getMessage());
        }
    }

}
