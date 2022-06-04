package zju.se.management.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.entity.User;
import zju.se.management.service.UserService;
import zju.se.management.utils.Response;
import zju.se.management.utils.ResponseData;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
@Api(protocols = "http", consumes = "application/json", produces = "application/json", tags = "统计数据接口")
public class StatisticsController extends BaseController {

    private final UserService userService;

    @Autowired
    public StatisticsController(UserService userService) {
        this.userService = userService;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Statistics extends ResponseData {
        private int number;
    }

    @GetMapping("/doctors")
    public Response<Statistics> getDoctorsNumber() {
        return ResponseOK(new Statistics(userService.getUserByRole(User.userType.DOCTOR).size()),"查询成功");
    }

    @GetMapping("/patients")
    public Response<Statistics> getPatientsNumber() {
        return ResponseOK(new Statistics(userService.getUserByRole(User.userType.PATIENT).size()),"查询成功");
    }

}
