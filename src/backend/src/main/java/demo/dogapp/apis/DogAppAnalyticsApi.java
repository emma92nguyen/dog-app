package demo.dogapp.apis;

import demo.dogapp.entities.DogAppRequestLog;
import demo.dogapp.services.DogAppRequestLoggingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class DogAppAnalyticsApi {

    private final DogAppRequestLoggingService dogAppRequestLoggingService;

    public DogAppAnalyticsApi(DogAppRequestLoggingService dogAppRequestLoggingService) {
        this.dogAppRequestLoggingService = dogAppRequestLoggingService;
    }

    @GetMapping("/report/tracking")
    public List<DogAppRequestLog> getAllRequestsLast7Days() {
        LocalDateTime now = LocalDateTime.now();
        return this.dogAppRequestLoggingService.getAllRequestsBetween(now.minusDays(7), now);
    }
}
