package zju.se.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zju.se.management.entity.Arrange;
import zju.se.management.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArrangeRepository extends JpaRepository<Arrange, Integer> {


    boolean existsById(int id);

    List<Arrange> findAllByDayType(Arrange.dayEnum dayType);
}
