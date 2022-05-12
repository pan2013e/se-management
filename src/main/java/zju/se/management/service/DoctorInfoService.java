package zju.se.management.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zju.se.management.entity.DoctorInfo;
import zju.se.management.repository.DoctorInfoRepository;
import zju.se.management.utils.DoctorInfoAlreadyExistsException;
import zju.se.management.utils.DoctorInfoNotFoundException;

import java.util.List;

@Service
public class DoctorInfoService {

    private final DoctorInfoRepository doctorInfoRepository;

    @Autowired
    public DoctorInfoService(DoctorInfoRepository doctorInfoRepository) {
        this.doctorInfoRepository = doctorInfoRepository;
    }
    @Deprecated
    public List<DoctorInfo> getAllDoctorInfos() {
        return doctorInfoRepository.findAll();
    }
    public DoctorInfo getDoctorInfoById(int id) throws DoctorInfoNotFoundException {
        return doctorInfoRepository.findById(id).orElseThrow(DoctorInfoNotFoundException::new);
    }
    public void deleteDoctorInfoById(int id) throws DoctorInfoNotFoundException {
        if(!isExist(id)) {
            throw new DoctorInfoNotFoundException();
        }
        doctorInfoRepository.deleteById(id);
    }

    public void addDoctorInfo(@NotNull DoctorInfo doctorInfo)throws DoctorInfoAlreadyExistsException{
        if(isExist(doctorInfo.getId())) {
            throw new DoctorInfoAlreadyExistsException();
        }
        doctorInfoRepository.save(doctorInfo);
    }

    private boolean isExist(int id) {
        return doctorInfoRepository.existsById(id);
    }
}
