package demo.dogapp;

import demo.dogapp.apis.DogAppApi;
import demo.dogapp.configs.WebSecurityTestConfig;
import demo.dogapp.dtos.DogBreedDto;
import demo.dogapp.services.services.DogAppRequestLoggingService;
import demo.dogapp.services.services.DogAppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebSecurityTestConfig.class)
@WebMvcTest(DogAppApi.class)
public class DogAppApiTests {

    @MockBean
    private DogAppService dogAppService;

    @MockBean
    private DogAppRequestLoggingService dogAppRequestLoggingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPaginatedData() throws Exception {
        List<String> dogBreedList = Arrays.asList(
                "affenpinscher",
                "african",
                "airedale",
                "akita",
                "appenzeller",
                "australian-kelpie",
                "australian-shepherd",
                "bakharwal-indian",
                "basenji",
                "beagle"
        );
        Map<String, Object> paginatedData = new HashMap<>();
        paginatedData.put("dogBreeds", dogBreedList);
        paginatedData.put("currentPage", "0");
        paginatedData.put("totalItems", "100");
        paginatedData.put("totalPages", "10");

        when(dogAppService.getPaginatedData(0, 10)).thenReturn(paginatedData);
        mockMvc.perform(get("/breeds/list/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalItems").value(100))
                .andExpect(jsonPath("$.totalPages").value(10))
                .andDo(print());
    }

    @Test
    void shouldReturnDogBreed_whenBreedDataExists() throws Exception {
        DogBreedDto dogBreed = DogBreedDto.builder()
                .breed("Akita")
                .imageUrls(Arrays.asList("https://images.dog.ceo/breeds/akita/512px-Ainu-Dog.jpg"))
                .build();
        when(dogAppService.getDogBreedById("akita")).thenReturn(Optional.of(dogBreed));

        mockMvc.perform(get("/breeds/detail/akita"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnDogBreedNotFound_whenBreedDataNotExists() throws Exception {
        when(dogAppService.getDogBreedById("akita")).thenReturn(Optional.empty());
        mockMvc.perform(get("/breeds/detail/akita"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
