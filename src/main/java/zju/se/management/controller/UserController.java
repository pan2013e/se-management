package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;
import zju.se.management.utils.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public Response<UserListResponseData> getUser() {
        return ResponseOK(new UserListResponseData(userService.getAllUsers()), "查询成功");
    }

    @GetMapping("/user/id/{id}")
    public Response<UserResponseData> getUserById(@PathVariable("id") Integer id) throws UserNotFoundException {
        User user = userService.getUserById(id);
        return ResponseOK(new UserResponseData(user), "查询成功");
    }

    @GetMapping("/user/name/{name}")
    public Response<UserResponseData> getUserByName(@PathVariable("name") String name) throws UserNotFoundException {
        User user = userService.getUserByName(name);
        return ResponseOK(new UserResponseData(user), "查询成功");
    }

    @GetMapping("/user/role/{role}")
    public Response<UserListResponseData> getUserByRole(@PathVariable("role") String role) {
        return ResponseOK(new UserListResponseData(userService.getUserByRole(User.userType.valueOf(role.toUpperCase()))),
                "查询成功");
    }

    @PostMapping("/user")
    public Response<?> addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "realName") String realName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "role") String role) throws UserAlreadyExistsException {
        User user = new User();
        user.setUserName(userName);
        user.setRealName(realName);
        user.setPassword(CryptoUtil.encrypt(password));
        user.setRole(User.userType.valueOf(role.toUpperCase()));
        userService.addUser(user);
        return ResponseOK("添加成功");
    }

}
