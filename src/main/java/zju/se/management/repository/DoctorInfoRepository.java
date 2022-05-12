package zju.se.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zju.se.management.entity.DoctorInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorInfoRepository extends JpaRepository<DoctorInfo, Integer> {
    void deleteById(int id);
    boolean existsById(int id);

}
