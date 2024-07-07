package demo.dogapp;

import demo.dogapp.apis.DogAppAnalyticsApi;
import demo.dogapp.configs.WebSecurityTestConfig;
import demo.dogapp.entities.DogAppRequestLog;
import demo.dogapp.services.DogAppRequestLoggingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebSecurityTestConfig.class)
@WebMvcTest(DogAppAnalyticsApi.class)
public class DogAppAnalyticsApiTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DogAppRequestLoggingService dogAppRequestLoggingService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldReturnListOfRequests_whenLoggedInWithAdminRole() throws Exception {
        DogAppRequestLog log1 = DogAppRequestLog.builder()
                .id(1)
                .sessionId("ECA053B545ABCAFFD2C099842654E724")
                .host("0:0:0:0:0:0:0:1")
                .clientIp("0:0:0:0:0:0:0:1")
                .requestUrl("http://localhost:8080/breeds/detail/buhund-norwegian")
                .logTime(LocalDateTime.parse("2024-07-07T18:05:15.317343"))
                .build();

        DogAppRequestLog log2 = DogAppRequestLog.builder()
                .id(2)
                .sessionId("ECA053B545ABCAFFD2C099842654E724")
                .host("0:0:0:0:0:0:0:1")
                .clientIp("0:0:0:0:0:0:0:1")
                .requestUrl("http://localhost:8080/admin/report/tracking")
                .logTime(LocalDateTime.parse("2024-07-07T18:05:15.317343"))
                .build();

        when(dogAppRequestLoggingService.getAllRequestsBetween(any(), any()))
                .thenReturn(List.of(log1, log2));
        mockMvc.perform(get("/admin/report/tracking"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 1, " +
                        "'sessionId': 'ECA053B545ABCAFFD2C099842654E724'," +
                        "'host': '0:0:0:0:0:0:0:1', " +
                        "'clientIp': '0:0:0:0:0:0:0:1', " +
                        "'requestUrl': 'http://localhost:8080/breeds/detail/buhund-norwegian', " +
                        "'logTime': '2024-07-07T18:05:15.317343'}," +
                        "{'id': 2, " +
                        "'sessionId': 'ECA053B545ABCAFFD2C099842654E724', " +
                        "'host': '0:0:0:0:0:0:0:1', " +
                        "'clientIp': '0:0:0:0:0:0:0:1', " +
                        "'requestUrl': 'http://localhost:8080/admin/report/tracking', " +
                        "'logTime': '2024-07-07T18:05:15.317343'}]"))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void shouldReturnStatusUnauthorized_whenNoAuthentication() throws Exception {
        DogAppRequestLog log1 = DogAppRequestLog.builder()
                .id(1)
                .host("0:0:0:0:0:0:0:1")
                .clientIp("0:0:0:0:0:0:0:1")
                .requestUrl("http://localhost:8080/breeds/detail/buhund-norwegian")
                .logTime(LocalDateTime.parse("2024-07-07T18:05:15.317343"))
                .build();

        when(dogAppRequestLoggingService.getAllRequestsBetween(any(), any()))
                .thenReturn(List.of(log1));
        mockMvc.perform(get("/admin/report/tracking"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnStatusForbidden_whenLoggedInWithUserRole() throws Exception {
        DogAppRequestLog log1 = DogAppRequestLog.builder()
                .id(1)
                .host("0:0:0:0:0:0:0:1")
                .clientIp("0:0:0:0:0:0:0:1")
                .requestUrl("http://localhost:8080/breeds/detail/buhund-norwegian")
                .logTime(LocalDateTime.parse("2024-07-07T18:05:15.317343"))
                .build();

        SecurityContextHolder.clearContext();

        when(dogAppRequestLoggingService.getAllRequestsBetween(any(), any()))
                .thenReturn(List.of(log1));
        mockMvc.perform(get("/admin/report/tracking"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


}
