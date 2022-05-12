package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.entity.DoctorInfo;
import zju.se.management.service.DoctorInfoService;
import zju.se.management.utils.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class DoctorInfoController extends BaseController {

    private final DoctorInfoService doctorInfoService;

    @Autowired
    public DoctorInfoController(DoctorInfoService doctorInfoService) {
        this.doctorInfoService = doctorInfoService;
    }

    @GetMapping("/doctorInfo")
    @Deprecated
    public Response<DoctorInfoListResponseData> getDoctorInfo() {
        return ResponseOK(new DoctorInfoListResponseData(doctorInfoService.getAllDoctorInfos()), "查询成功");
    }

    @PostMapping("/doctorInfo")
    public Response<?> addDoctorInfo(
            @RequestParam(value = "department") String department,
            @RequestParam(value = "hospital") String hospital){
        DoctorInfo doctorInfo = new DoctorInfo();
        doctorInfo.setDepartment(department);
        doctorInfo.setHospital(hospital);
        doctorInfoService.addDoctorInfo(doctorInfo);
        return ResponseOK("添加成功");
    }

}
