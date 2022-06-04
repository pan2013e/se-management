package zju.se.management.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.authentication.CryptoUtil;
import zju.se.management.entity.DoctorInfo;
import zju.se.management.entity.User;
import zju.se.management.service.DoctorInfoService;
import zju.se.management.service.UserService;
import zju.se.management.utils.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
@Api(protocols = "http", consumes = "application/json", produces = "application/json", tags = "医生接口")
public class DoctorInfoController extends BaseController {

    private final DoctorInfoService doctorInfoService;
    private final UserService userService;

    @Autowired
    public DoctorInfoController(DoctorInfoService doctorInfoService, UserService userService) {
        this.doctorInfoService = doctorInfoService;
        this.userService = userService;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class nameList extends ResponseData {
        List<String> names;
    }

    @GetMapping("/hospital")
    @ApiOperation(value = "获取所有医院名称")
    public Response<nameList> getHospital() {
        HashSet<String> hospitals = new HashSet<>();
        doctorInfoService.getAllDoctorInfos()
                         .forEach(doctorInfo -> hospitals.add(doctorInfo.getHospital()));
        return ResponseOK(new nameList(hospitals.stream().toList()), "查询成功");
    }

    @GetMapping("/dept")
    @ApiOperation(value = "获取所有科室名称")
    public Response<nameList> getDepartment() {
        HashSet<String> departments = new HashSet<>();
        doctorInfoService.getAllDoctorInfos()
                         .forEach(doctorInfo -> departments.add(doctorInfo.getDepartment()));
        return ResponseOK(new nameList(departments.stream().toList()), "查询成功");
    }

    @GetMapping("/doctor/all")
    @ApiOperation(value = "获取所有医生信息")
    public Response<DoctorInfoListResponseData> getDoctorInfo() {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getAllDoctorInfos()),
                "查询成功");
    }

    @GetMapping("/doctor/dept/{dept}")
    @ApiOperation(value = "获取某个科室的所有医生")
    public Response<DoctorInfoListResponseData> getDoctorInfoByDept(@PathVariable("dept") String dept) {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getDoctorInfoByDepartment(dept)),
                "查询成功");
    }

    @GetMapping("/doctor/hospital/{hospital}")
    @ApiOperation(value = "获取某个医院的所有医生")
    public Response<DoctorInfoListResponseData> getDoctorInfoByHospital(@PathVariable("hospital") String hospital) {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getDoctorInfoByHospital(hospital)),
                "查询成功");
    }

    @GetMapping("/doctor")
    @ApiOperation(value = "获取某个医院的某个科室所有医生的信息", notes = "医院和科室如果有一项为空,则返回所有医生信息")
    public Response<DoctorInfoListResponseData> getDoctorInfoByHospitalAndDept(
            @RequestParam("hospital") String hospital,
            @RequestParam("department") String department) {
        if(hospital == null || department == null) {
            return getDoctorInfo();
        }
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getDoctorInfoByHospitalAndDepartment(hospital, department)),
                "查询成功");
    }

    @PostMapping("/doctor")
    @ApiOperation(value = "添加医生信息", notes = "管理前端使用")
    public Response<?> addDoctorInfo(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "realName") String realName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "department") String department,
            @RequestParam(value = "hospital") String hospital) throws BaseException {
        DoctorInfo doctorInfo = new DoctorInfo();
        User user = new User();
        user.setUserName(userName);
        user.setRealName(realName);
        user.setRole(User.userType.DOCTOR);
        user.setPassword(CryptoUtil.encrypt(password));
        userService.addUser(user);
        doctorInfo.setUser(user);
        doctorInfo.setDepartment(department);
        doctorInfo.setHospital(hospital);
        doctorInfoService.addDoctorInfo(doctorInfo);
        return ResponseOK("添加成功");
    }

    @DeleteMapping("/doctor/{id}")
    @ApiOperation(value = "删除医生信息", notes = "管理前端使用")
    public Response<?> deleteDoctorInfo(@PathVariable("id") String id) throws BaseException {
        userService.deleteUserById(Integer.parseInt(id));
        doctorInfoService.deleteDoctorInfoById(Integer.parseInt(id));
        return ResponseOK("删除成功");
    }

    @DeleteMapping("/doctor")
    @ApiOperation(value = "删除医生信息", notes = "管理前端使用")
    public Response<?> deleteDoctorInfo(
            @RequestParam(value = "id") String[] id) throws BaseException {
        for(String s : id) {
            userService.deleteUserById(Integer.parseInt(s));
            doctorInfoService.deleteDoctorInfoById(Integer.parseInt(s));
        }
        return ResponseOK("删除成功");
    }

}
