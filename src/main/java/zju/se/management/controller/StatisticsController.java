package zju.se.management.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.entity.APILog;
import zju.se.management.entity.User;
import zju.se.management.service.APILogService;
import zju.se.management.service.UserService;
import zju.se.management.utils.Response;
import zju.se.management.utils.ResponseData;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
@Api(protocols = "http", consumes = "application/json", produces = "application/json", tags = "统计数据接口")
public class StatisticsController extends BaseController {

    private final UserService userService;
    private final APILogService apiLogService;

    @Autowired
    public StatisticsController(UserService userService, APILogService apiLogService) {
        this.userService = userService;
        this.apiLogService = apiLogService;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Statistics extends ResponseData {
        private int number;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class ListAPILogs extends ResponseData {
        private List<APILog> logs;
    }

    @GetMapping("/doctors")
    public Response<Statistics> getDoctorsNumber() {
        return ResponseOK(new Statistics(userService.getUserByRole(User.userType.DOCTOR).size()),"查询成功");
    }

    @GetMapping("/patients")
    public Response<Statistics> getPatientsNumber() {
        return ResponseOK(new Statistics(userService.getUserByRole(User.userType.PATIENT).size()),"查询成功");
    }

    @GetMapping("/admins")
    public Response<Statistics> getAdminsNumber() {
        return ResponseOK(new Statistics(userService.getUserByRole(User.userType.ADMIN).size()),"查询成功");
    }

    @GetMapping("/users")
    public Response<Statistics> getUsersNumber() {
        return ResponseOK(new Statistics(userService.getAllUsers().size()),"查询成功");
    }

    @GetMapping("/apicounts")
    public Response<Statistics> getApiCounts() {
        return ResponseOK(new Statistics(apiLogService.findAll().size()),"查询成功");
    }

    @GetMapping("/apicounts/{type}")
    public Response<Statistics> getApiCountsByType(@PathVariable String type) {
        return ResponseOK(new Statistics(apiLogService.countByStatus(type.toUpperCase())),"查询成功");
    }

    @GetMapping("/apilogs")
    public Response<ListAPILogs> getApiLogs() {
        return ResponseOK(new ListAPILogs(apiLogService.findAll()),"查询成功");
    }

    @DeleteMapping("/apilogs")
    public Response<?> deleteApiLogs() {
        apiLogService.deleteAll();
        return ResponseOK("删除成功");
    }

}
