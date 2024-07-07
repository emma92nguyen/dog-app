package demo.dogapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dog-app-request-logs")
public class DogAppRequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String sessionId;
    private String host;
    private String clientIp;
    private String requestUrl;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime logTime;
}
