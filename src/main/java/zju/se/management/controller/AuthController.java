package zju.se.management.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.authentication.TokenUtil;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;
import zju.se.management.utils.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
@Api(protocols = "http", consumes = "application/json", produces = "application/json", tags = "认证接口")
public class AuthController extends BaseController {
    private final Producer captchaProducer;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AuthController(UserService userService, Producer captchaProducer, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.captchaProducer = captchaProducer;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "管理前端使用")
    public Response<?> login(
            HttpSession session,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "captchaKey") String captchaKey,
            @RequestParam(value = "captchaCode") String captchaCode) throws BaseException {
        Response<?> captchaResult = captcha(captchaKey, captchaCode);
        if (captchaResult.getCode() < 0) {
            return captchaResult;
        } else {
            boolean validationResult = CryptoUtil.validate(password, userService.getPasswordByName(userName));
            if (!validationResult) {
                throw new AuthErrorException("密码错误");
            }
            String token = TokenUtil.getToken(userService.getUserByName(userName));
            session.setAttribute("token", token);
            return ResponseOK(new TokenResponseData(userName, token), "登录成功");
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "管理前端使用")
    public Response<?> register(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "realName") String realName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "captchaKey") String captchaKey,
            @RequestParam(value = "captchaCode") String captchaCode) throws UserAlreadyExistsException {
        Response<?> captchaResult = captcha(captchaKey, captchaCode);
        if (captchaResult.getCode() < 0) {
            return captchaResult;
        } else {
            User user = new User();
            user.setUserName(userName);
            user.setRealName(realName);
            user.setPassword(CryptoUtil.encrypt(password));
            user.setRole(User.userType.PATIENT);
            userService. addUser(user);
            return ResponseOK("注册成功");
        }
    }

    @GetMapping("/verify")
    @ApiOperation(value = "鉴权，验证登录是否有效")
    public Response<AccessControlResponseData> verify(
            HttpServletRequest req, HttpServletResponse res) throws BaseException {
        res.setHeader("Cache-Control", "no-cache, no-store");
        String token = req.getHeader("token");
        if(token == null || token.equals("")){
            throw new AuthErrorException("未登录");
        }
        DecodedJWT decodedJWT = TokenUtil.decodeToken(token);
        String userName = decodedJWT.getClaim("userName").asString();
        String role = decodedJWT.getClaim("role").asString();
        int userId = decodedJWT.getClaim("id").asInt();
        return ResponseOK(new AccessControlResponseData(userId, userName, role.toUpperCase()), "验证成功");
    }

    @GetMapping("/logout")
    @ApiOperation(value = "用户登出", notes = "各系统可用")
    public Response<?> logout(HttpSession session, HttpServletResponse res) {
        res.setHeader("Cache-Control", "no-cache, no-store");
        if(session.getAttribute("token") == null) {
            session.invalidate();
            return ResponseOK("登出成功");
        } else{
            return ResponseError("已经登出");
        }
    }

    @GetMapping("/captcha")
    @ApiOperation(value = "获取验证码", notes = "管理前端使用")
    public Response<CaptchaResponseData> captcha(HttpServletResponse res) throws IOException {
        res.setHeader("Cache-Control", "no-cache, no-store");
        String key = UUID.randomUUID().toString();
        String code = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(code);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        Base64.Encoder base64 = Base64.getEncoder();
        String base64Img = "data:image/jpeg;base64," + base64.encodeToString(os.toByteArray());
        redisTemplate.opsForValue().set(key, code);
        redisTemplate.expire(key, 3, TimeUnit.MINUTES);
        return ResponseOK(new CaptchaResponseData(key, base64Img),"生成成功");
    }

    @PostMapping("/captcha")
    @ApiOperation(value = "验证验证码", notes = "管理前端使用")
    public Response<?> captcha(
            @RequestParam(value = "key") @NotNull String key,
            @RequestParam(value = "code") String code) {
        if(Boolean.FALSE.equals(redisTemplate.hasKey(key))){
            return ResponseError("验证码已过期");
        } else {
            String answer = redisTemplate.opsForValue().get(key);
            redisTemplate.expire(key, 0, TimeUnit.SECONDS);
            assert answer != null;
            if (!answer.equals(code)) {
                return ResponseError("验证码错误");
            } else {
                return ResponseOK("验证成功");
            }
        }
    }

    @PostMapping("/changePassword")
    @ApiOperation(value = "修改密码", notes = "管理前端使用")
    public Response<?> changePassword(
            @RequestParam(value = "userName") @NotNull String userName,
            @RequestParam(value = "oldPassword") @NotNull String oldPassword,
            @RequestParam(value = "newPassword") @NotNull String newPassword) throws UserNotFoundException, AuthErrorException {
        boolean validationResult = CryptoUtil.validate(oldPassword, userService.getPasswordByName(userName));
        if (!validationResult) {
            throw new AuthErrorException("密码错误");
        }
        userService.setPasswordByName(userName, CryptoUtil.encrypt(newPassword));
        return ResponseOK("修改成功");
    }

}
