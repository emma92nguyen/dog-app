package demo.dogapp.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class DogBreedDto {

    private String breed;
    private List<String> imageUrls;

}
