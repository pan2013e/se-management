package zju.se.management.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zju.se.management.entity.Arrange;
import zju.se.management.repository.ArrangeRepository;

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
        return arrangeRepository.findAll();
    }
    public void addArrange(@NotNull Arrange arrange){
        arrangeRepository.save(arrange);
    }
}
