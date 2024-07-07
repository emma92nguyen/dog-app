package demo.dogapp.services.services;

import demo.dogapp.dtos.DogBreedDto;
import demo.dogapp.exceptions.DogBreedListNotFoundException;
import demo.dogapp.exceptions.DogBreedNotFoundException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DogCEOService {

    private final WebClient webClient;

    private static final String DOG_CEO_BASE_URL = "https://dog.ceo/api";
    private static final String ALL_DOG_BREEDS_API = "/breeds/list/all";
    private static final String DOG_BREED_API = "/breed/";

    private static final String CACHE_DOG_BREED = "dog_breed";
    public static final String CACHE_ALL_BREEDS = "all_breeds";

    public DogCEOService(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = this.configureConnection(webClientBuilder);
        this.webClient =
                webClientBuilder
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .baseUrl(DOG_CEO_BASE_URL)
                        .build();
    }

    private HttpClient configureConnection(WebClient.Builder webClientBuilder) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofMillis(10000))
                .doOnConnected(
                        conn ->
                                conn.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                                        .addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS)));
    }

    @Cacheable(value = CACHE_ALL_BREEDS, unless = "#result.empty")
    public List<String> getAllBreeds() {
        try {
            Optional<String> response = this.webClient
                    .get()
                    .uri(ALL_DOG_BREEDS_API)
                    .retrieve()
                    .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new DogBreedListNotFoundException("Dog breed list not found")))
                    .bodyToMono(String.class)
                    .blockOptional();

            List<String> allBreeds = response.map(this::flattenBreedList).orElseGet(ArrayList::new);
            Collections.sort(allBreeds);
            return allBreeds;
        } catch (DogBreedListNotFoundException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Cacheable(value = CACHE_DOG_BREED, key = "#breedName", unless = "#result == null")
    public Optional<DogBreedDto> getBreedByName(final String breedName) {
        try {
            String breedNameUrl = breedName;
            if (breedNameUrl != null && breedNameUrl.contains("-")) {
                breedNameUrl = breedNameUrl.replace("-", "/");
            }
            List<String> imageUrls = this.getBreedImagesByName(breedNameUrl)
                    .map(this::flattenImageList)
                    .orElseGet(ArrayList::new);
            if (!imageUrls.isEmpty()) {
                return Optional.of(DogBreedDto.builder().breed(breedName).imageUrls(imageUrls).build());
            }
        } catch (DogBreedNotFoundException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<String> getBreedImagesByName(final String breedName) throws DogBreedNotFoundException {
        return this.webClient
                .get()
                .uri(DOG_BREED_API + breedName + "/images")
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new DogBreedNotFoundException("Dog breed images not found for: " + breedName)))
                .bodyToMono(String.class).blockOptional();
    }

    private List<String> flattenBreedList(final String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject breedsObject = jsonObject.getJSONObject("message");
        List<String> breeds = new ArrayList<>();

        for (String breed : breedsObject.keySet()) {
            if (breedsObject.getJSONArray(breed).isEmpty()) {
                breeds.add(breed);
            }
            for (Object subBreed : breedsObject.getJSONArray(breed)) {
                breeds.add(breed + "-" + subBreed.toString());
            }
        }

        return breeds;
    }

    private List<String> flattenImageList(final String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray imagesArray = jsonObject.getJSONArray("message");
        List<String> images = new ArrayList<>();
        imagesArray.forEach(image -> images.add(image.toString()));
        return images;
    }

}
