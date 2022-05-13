package zju.se.management.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zju.se.management.entity.Arrange;
import zju.se.management.repository.ArrangeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ArrangeService {

    private final ArrangeRepository arrangeRepository;

    @Autowired
    public ArrangeService(ArrangeRepository arrangeRepository) {
        this.arrangeRepository = arrangeRepository;
    }
    @Deprecated
    public List<Arrange> getAllArranges() {
        Date now = new Date();
        String[] weekDays = {
                "SUNDAY",
                "MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY",
                "SATURDAY"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return arrangeRepository.findAllByDayType(Arrange.dayEnum.valueOf(weekDays[w]));
    }
    public void addArrange(@NotNull Arrange arrange){
        arrangeRepository.save(arrange);
    }

}
