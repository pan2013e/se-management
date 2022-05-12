package zju.se.management.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.authentication.TokenUtil;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;
import zju.se.management.utils.*;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
@Api(protocols = "http", consumes = "application/json", produces = "application/json", tags = "认证接口")
public class AuthController extends BaseController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "前端登陆界面使用")
    public Response<TokenResponseData> login(@RequestParam(value = "userName") String userName,
                                             @RequestParam(value = "password") String password) throws UserNotFoundException, AuthErrorException {
        boolean validationResult = CryptoUtil.validate(password, userService.getPasswordByName(userName));
        if (!validationResult) {
            throw new AuthErrorException("密码错误");
        }
        String token = TokenUtil.getToken(userService.getUserByName(userName));
        return ResponseOK(new TokenResponseData(userName, token), "登录成功");
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "前端注册界面使用，仅支持病人用户注册")
    public Response<?> register(@RequestParam(value = "userName") String userName,
                             @RequestParam(value = "password") String password) throws UserAlreadyExistsException {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(CryptoUtil.encrypt(password));
        user.setRole(User.userType.PATIENT);
        userService.addUser(user);
        return ResponseOK("注册成功");
    }

    @PostMapping("/verify")
    @ApiOperation(value = "鉴权", notes = "供其他系统验证token是否有效")
    public Response<AccessControlResponseData> verify(@RequestParam(value = "userName") String userName,
                           @RequestParam(value = "token") String token) throws UserNotFoundException, AuthErrorException {
        DecodedJWT decodedJWT = TokenUtil.decodeToken(token);
        if (!decodedJWT.getClaim("userName").asString().equals(userName)) {
            throw new AuthErrorException("用户名不匹配");
        }
        User user = userService.getUserByName(userName);
        return ResponseOK(new AccessControlResponseData(userName, user.getRole().toString()), "验证成功");
    }

}
