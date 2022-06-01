package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.entity.Arrange;
import zju.se.management.entity.User;
import zju.se.management.service.ArrangeService;
import zju.se.management.service.UserService;
import zju.se.management.utils.ArrangeListResponseData;
import zju.se.management.utils.BaseException;
import zju.se.management.utils.Response;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class ArrangeController extends BaseController {

    private final ArrangeService arrangeService;
    private final UserService userService;
    @Autowired
    public ArrangeController(ArrangeService arrangeService,UserService userService) {
        this.arrangeService = arrangeService;
        this.userService=userService;
    }

    @GetMapping("/arrange/{id}")
    public Response<ArrangeListResponseData> getArrange(@PathVariable("id") int id) {
        return ResponseOK(new ArrangeListResponseData(arrangeService.getArrangesByDoctorId(id)), "查询成功");
    }

    @PostMapping("/arrange")
    public Response<?> addArrange(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "start_time") String start_time,
            @RequestParam(value = "end_time") String end_time,
            @RequestParam(value = "dayType") String dayType) throws BaseException {
        Arrange arrange = new Arrange();
        User user = userService.getUserById(id);
        arrange.setUser(user);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try{
            arrange.setStart_time(format.parse(start_time));
            arrange.setEnd_time(format.parse(end_time));
        }
        catch (Exception e){
            throw new BaseException("时间格式错误");
        }
        arrange.setDayType(Arrange.dayEnum.valueOf(dayType));
        arrangeService.addArrange(arrange);
        return ResponseOK("添加成功");
    }
}
