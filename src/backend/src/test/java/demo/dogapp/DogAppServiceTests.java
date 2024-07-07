package demo.dogapp;

import demo.dogapp.dtos.DogBreedDto;
import demo.dogapp.services.DogAppService;
import demo.dogapp.services.DogCEOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DogAppServiceTests {

    @Mock
    CacheManager cacheManager;

    @Mock
    private DogCEOService dogCEOService;

    @InjectMocks
    private DogAppService dogAppService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void shouldReturnListOfAllBreeds() {
        List<String> allBreeds = Arrays.asList("affenpinscher",
                "african",
                "airedale",
                "akita",
                "appenzeller",
                "australian-kelpie",
                "australian-shepherd",
                "bakharwal-indian",
                "basenji",
                "beagle");
        given(dogCEOService.getAllBreeds()).willReturn(allBreeds);

        Map<String, Object> result = dogAppService.getPaginatedData(0, 10);
        assertThat(result.get("totalItems")).isEqualTo(10);
        assertThat(result.get("totalPages")).isEqualTo(1);
        assertThat(result.get("currentPage")).isEqualTo(0);

        List breeds = (List) result.get("dogBreeds");
        assertThat(breeds).hasSize(10);
        assertThat(breeds.get(0)).isEqualTo("affenpinscher");
        assertThat(breeds.get(5)).isEqualTo("australian-kelpie");
        assertThat(breeds.get(9)).isEqualTo("beagle");

        verify(dogCEOService, times(1)).getAllBreeds();
    }

    @Test
    void shouldReturnBreedData_whenBreedDataExists() {
        DogBreedDto dogBreed = DogBreedDto.builder()
                .breed("akita")
                .imageUrls(List.of("https://images.dog.ceo/breeds/akita/512px-Ainu-Dog.jpg"))
                .build();
        given(dogCEOService.getBreedByName("akita")).willReturn(Optional.of(dogBreed));

        Optional<DogBreedDto> result = dogAppService.getDogBreedById("akita");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getBreed()).isEqualTo("akita");
        assertThat(result.get().getImageUrls()).hasSize(1);
        assertThat(result.get().getImageUrls().getFirst()).isEqualTo("https://images.dog.ceo/breeds/akita/512px-Ainu-Dog.jpg");

        verify(dogCEOService, times(1)).getBreedByName("akita");
    }

    @Test
    void shouldReturnEmpty_whenBreedDataNotExists() {
        given(dogCEOService.getBreedByName(anyString())).willReturn(Optional.empty());

        Optional<DogBreedDto> result = dogAppService.getDogBreedById("akita");
        assertThat(result.isEmpty()).isTrue();

        verify(dogCEOService, times(1)).getBreedByName("akita");
    }
}
