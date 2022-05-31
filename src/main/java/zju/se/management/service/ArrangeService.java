package zju.se.management.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zju.se.management.entity.Arrange;
import zju.se.management.entity.DoctorInfo;
import zju.se.management.repository.ArrangeRepository;
import zju.se.management.repository.DoctorInfoRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ArrangeService {

    private final ArrangeRepository arrangeRepository;
    private final DoctorInfoRepository doctorInfoRepository;

    private final String[] weekDays = {
            "SUNDAY",
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY"
    };

    @Autowired
    public ArrangeService(ArrangeRepository arrangeRepository,DoctorInfoRepository doctorInfoRepository) {
        this.arrangeRepository = arrangeRepository;
        this.doctorInfoRepository=doctorInfoRepository;
    }

    private int getWeekday() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public List<Arrange> getAllArranges() {
        int w = getWeekday();
        if (w < 0){
            w = 0;
        }
        return arrangeRepository.findAllByDayType(Arrange.dayEnum.valueOf(weekDays[w]));
    }

    public List<Arrange> getArrangesByDoctorId(int doctorId) {
        List<Arrange> list = new ArrayList<>();
        for(int i=0;i<7;i++){
            List<Arrange> temp=arrangeRepository.findAllByUserIdAndDayType(doctorId,Arrange.dayEnum.valueOf(weekDays[i]));
            if(!temp.isEmpty()){
                list.add(temp.get(0));
            }
        }
        return list;
    }

    public void addArrange(@NotNull Arrange arrange){
        arrangeRepository.save(arrange);
    }

    public List<Arrange> getAllDepartmentArranges(String department){
        List<Arrange> list = new ArrayList<>();
        List<Arrange> arranges=getAllArranges();
        for(Arrange arrange:arranges){
            DoctorInfo doctorInfo=doctorInfoRepository.findByUserId(arrange.getUser().getId()).get(0);
            if(doctorInfo.getDepartment().equals(department)){
                list.add(arrange);
            }
        }
        return list;
    }
}
