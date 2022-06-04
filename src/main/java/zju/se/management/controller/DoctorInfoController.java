package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
public class DoctorInfoController extends BaseController {

    private final DoctorInfoService doctorInfoService;
    private final UserService userService;

    @Autowired
    public DoctorInfoController(DoctorInfoService doctorInfoService, UserService userService) {
        this.doctorInfoService = doctorInfoService;
        this.userService = userService;
    }

    @GetMapping("/hospital")
    public Response<List<String>> getHospital() {
        HashSet<String> hospitals = new HashSet<>();
        doctorInfoService.getAllDoctorInfos()
                         .forEach(doctorInfo -> hospitals.add(doctorInfo.getHospital()));
        return ResponseOK(hospitals.stream().toList(), "查询成功");
    }

    @GetMapping("/doctor/all")
    public Response<DoctorInfoListResponseData> getDoctorInfo() {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getAllDoctorInfos()),
                "查询成功");
    }

    @GetMapping("/doctor/dept/{dept}")
    public Response<DoctorInfoListResponseData> getDoctorInfoByDept(@PathVariable("dept") String dept) {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getDoctorInfoByDepartment(dept)),
                "查询成功");
    }

    @GetMapping("/doctor/hospital/{hospital}")
    public Response<DoctorInfoListResponseData> getDoctorInfoByHospital(@PathVariable("hospital") String hospital) {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getDoctorInfoByHospital(hospital)),
                "查询成功");
    }

    @GetMapping("/doctor")
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
    public Response<?> addDoctorInfo(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "department") String department,
            @RequestParam(value = "hospital") String hospital) throws BaseException {
        DoctorInfo doctorInfo = new DoctorInfo();
        User user = userService.getUserById(userId);
        doctorInfo.setUser(user);
        doctorInfo.setDepartment(department);
        doctorInfo.setHospital(hospital);
        doctorInfoService.addDoctorInfo(doctorInfo);
        return ResponseOK("添加成功");
    }

}
