package zju.se.management.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.authentication.TokenUtil;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;
import zju.se.management.utils.AccessControlResponseData;
import zju.se.management.utils.TokenResponseData;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Response login(@RequestParam(value = "userName") String userName,
                          @RequestParam(value = "password") String password) {
        try{
            String hashed = userService.getPasswordByName(userName);
            boolean validationResult = CryptoUtil.validate(password, hashed);
            if (validationResult) {
                String token = TokenUtil.getToken(userService.getUserByName(userName));
                return new Response(0, new TokenResponseData(userName, token), "登录成功");
            } else {
                return new Response(-1, null, "密码错误");
            }
        } catch (Exception e) {
            return new Response(-1, null, "用户名不存在");
        }
    }

    @PostMapping("/register")
    public Response register(@RequestParam(value = "userName") String userName,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "role") String role) {

        if (userService.getUserByName(userName) != null) {
            return new Response(-1, null, "用户名已存在");
        }
        String hashed = CryptoUtil.encrypt(password);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(hashed);
        user.setRole(User.userType.valueOf(role));
        userService.addUser(user);
        return new Response(0, null, "注册成功");
    }

    @PostMapping("/verify")
    @ApiOperation(value = "验证token是否有效")
    public Response verify(@RequestParam(value = "userName") String userName,
                           @RequestParam(value = "token") String token) {

        try {
            DecodedJWT decodedJWT = TokenUtil.decodeToken(token);
            if (!decodedJWT.getClaim("userName").asString().equals(userName)) {
                return new Response(-1, null, "用户名不匹配");
            }
            User user = userService.getUserByName(userName);
            return new Response(0, new AccessControlResponseData(userName, user.getRole().toString()),
                    "验证成功");
        } catch (TokenExpiredException e) {
            return new Response(-1, null, "token过期");
        } catch (Exception e) {
            return new Response(-1, null, "非法token");
        }
    }

}
