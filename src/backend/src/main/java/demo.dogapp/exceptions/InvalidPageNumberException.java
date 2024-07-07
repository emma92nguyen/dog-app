package demo.dogapp.exceptions;

public class InvalidPageNumberException extends RuntimeException{
    public InvalidPageNumberException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
