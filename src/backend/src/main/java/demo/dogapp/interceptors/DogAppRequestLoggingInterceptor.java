package demo.dogapp.interceptors;

import demo.dogapp.entities.DogAppRequestLog;
import demo.dogapp.services.services.DogAppRequestLoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DogAppRequestLoggingInterceptor implements HandlerInterceptor {

    private final DogAppRequestLoggingService dogAppRequestLoggingService;

    public DogAppRequestLoggingInterceptor(DogAppRequestLoggingService dogAppRequestLoggingService) {
        this.dogAppRequestLoggingService = dogAppRequestLoggingService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        DogAppRequestLog dogAppRequestLog = DogAppRequestLog.builder()
                .sessionId(sessionId)
                .host(request.getRemoteHost())
                .clientIp(request.getRemoteAddr())
                .requestUrl(request.getRequestURL().toString())
                .logTime(LocalDateTime.now())
                .build();
        this.dogAppRequestLoggingService.logDogAppRequest(dogAppRequestLog);
        return true;
    }
}
