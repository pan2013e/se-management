package zju.se.management.utils;

import lombok.*;
import zju.se.management.entity.DoctorInfo;

import java.util.Date;

@Getter
@Setter
public class DoctorInfoResponseData extends ResponseData {
    private final int id;

    private final String hospital;

    private final String department;

    public DoctorInfoResponseData(DoctorInfo doctorInfo) {
        this.id = doctorInfo.getId();
        this.department=doctorInfo.getDepartment();
        this.hospital=doctorInfo.getHospital();
    }
}
