package zju.se.management.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zju.se.management.repository.DoctorInfoRepository;

import java.util.List;

@Service
public class DoctorInfoService {

    private final DoctorInfoRepository doctorInfoRepository;

    @Autowired
    public DoctorInfoService(DoctorInfoRepository doctorInfoRepository) {
        this.doctorInfoRepository = doctorInfoRepository;
    }

}
