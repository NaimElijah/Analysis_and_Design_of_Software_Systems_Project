package DomainLayer.exception;

public class InvalidInput extends RuntimeException {
    public InvalidInput(String message) {
        super(message);
    }
}
