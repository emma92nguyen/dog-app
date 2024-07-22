package demo.dogapp.apis;

import demo.dogapp.dtos.DogBreedDto;
import demo.dogapp.services.DogAppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController()
@RequestMapping("/breeds")
public class DogAppApi {

    private final DogAppService dogAppService;

    public DogAppApi(DogAppService dogAppService) {
        this.dogAppService = dogAppService;
    }

    @GetMapping("/list/all")
    public ResponseEntity<Map<String, Object>> getAllBreeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> response = dogAppService.getPaginatedData(page, size);
            if (response.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DogBreedDto> getDogBreedById(@PathVariable String id) {
        Optional<DogBreedDto> dogBreedOpt = dogAppService.getDogBreedById(id);
        return dogBreedOpt.map(breed -> new ResponseEntity<>(breed, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
