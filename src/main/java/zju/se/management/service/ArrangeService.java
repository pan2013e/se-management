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

    public List<List<Date>> getArrangesByDoctorId(int doctorId) {
        List<List<Date>> list = new ArrayList<>();
        for(int i=0;i<7;i++){
            List<Arrange> temp = arrangeRepository.findAllByUserIdAndDayType(doctorId,Arrange.dayEnum.valueOf(weekDays[i]));
            List<Date> date = new ArrayList<>();
            if(!temp.isEmpty()){
                for (Arrange arrange : temp) {
                    int k;
                    for (k = 0; k < date.size(); k += 2) {
                        if (arrange.getStart_time().compareTo(date.get(k)) < 0) {
                            break;
                        }
                    }
                    date.add(k, arrange.getStart_time());
                    date.add(k + 1, arrange.getEnd_time());
                }
            }
            list.add(date);
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

    public void resetArrange(int userId, Arrange.dayEnum day){
        arrangeRepository.deleteAllByUserIdAndDayType(userId, day);
    }

    public void deleteArrangeByUserId(int userId){
        arrangeRepository.deleteAllByUserId(userId);
    }
}
