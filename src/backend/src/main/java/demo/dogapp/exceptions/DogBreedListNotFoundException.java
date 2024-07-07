package demo.dogapp.exceptions;

public class DogBreedListNotFoundException extends RuntimeException {
    public DogBreedListNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
