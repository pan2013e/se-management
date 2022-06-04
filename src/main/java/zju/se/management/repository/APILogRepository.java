package zju.se.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zju.se.management.entity.APILog;

import java.util.List;

public interface APILogRepository extends JpaRepository<APILog, Integer> {
    List<APILog> findAPILogsByStatus(String status);
    int countAPILogsByStatus(String status);
}
