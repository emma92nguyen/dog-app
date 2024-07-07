package demo.dogapp.repositories;

import demo.dogapp.entities.DogAppRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DogAppRequestRepository extends JpaRepository<DogAppRequestLog, Long> {
    List<DogAppRequestLog> findByLogTimeBetweenOrderByLogTimeDesc(LocalDateTime start, LocalDateTime end);
}
