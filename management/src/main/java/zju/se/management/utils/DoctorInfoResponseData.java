package zju.se.management.utils;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import zju.se.management.entity.DoctorInfo;

@Getter
@Setter
public class DoctorInfoResponseData extends ResponseData {
    private final int id;
    private final String userName;
    private final String realName;
    private final String hospital;
    private final String department;

    public DoctorInfoResponseData(@NotNull DoctorInfo doctorInfo) {
        this.id = doctorInfo.getUser().getId();
        this.userName = doctorInfo.getUser().getUserName();
        this.realName = doctorInfo.getUser().getRealName();
        this.department = doctorInfo.getDepartment();
        this.hospital = doctorInfo.getHospital();
    }
}
