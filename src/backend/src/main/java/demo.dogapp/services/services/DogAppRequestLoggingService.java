package demo.dogapp.services.services;

import demo.dogapp.entities.DogAppRequestLog;
import demo.dogapp.repositories.repositories.DogAppRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DogAppRequestLoggingService {

    private final DogAppRequestRepository dogAppRequestRepository;

    public DogAppRequestLoggingService(DogAppRequestRepository dogAppRequestRepository) {
        this.dogAppRequestRepository = dogAppRequestRepository;
    }

    public void logDogAppRequest(final DogAppRequestLog dogAppRequestLog) {
        this.dogAppRequestRepository.save(dogAppRequestLog);
    }

    public List<DogAppRequestLog> getAllRequestsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return this.dogAppRequestRepository.findByLogTimeBetweenOrderByLogTimeDesc(startDate, endDate);
    }
}
