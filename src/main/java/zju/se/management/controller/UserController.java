package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.entity.User;
import zju.se.management.entity.Arrange;
import zju.se.management.service.UserService;
import zju.se.management.service.ArrangeService;
import zju.se.management.utils.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class UserController extends BaseController {

    private final UserService userService;
    private final ArrangeService arrangeService;

    @Autowired
    public UserController(UserService userService,ArrangeService arrangeService) {
        this.arrangeService = arrangeService;
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

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }

    private static Date randomDate(String beginDate, String endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);

            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(),end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        if(User.userType.valueOf(role.toUpperCase())==User.userType.DOCTOR){
            String[] weekDays = {
                    "SUNDAY",
                    "MONDAY",
                    "TUESDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY",
                    "SATURDAY"};
            Random random=new Random();
            for(int i=0;i<7;i++){
                if(random.nextBoolean()){
                    Date start_time = randomDate("1970.01.01 8:30","1970.01.01 10:30");
                    Date end_time = randomDate("1970.01.01 14:30","1970.01.01 16:30");
                    Arrange arrange = new Arrange();
                    arrange.setId(user.getId());
                    arrange.setStart_time(start_time);
                    arrange.setEnd_time(end_time);
                    arrange.setDayType(Arrange.dayEnum.valueOf(weekDays[i]));
                    arrangeService.addArrange(arrange);
                }

            }

        }
        return ResponseOK("添加成功");
    }

}
