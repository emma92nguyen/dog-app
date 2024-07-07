package demo.dogapp.exceptions;

public class DogBreedNotFoundException extends RuntimeException {
    public DogBreedNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
