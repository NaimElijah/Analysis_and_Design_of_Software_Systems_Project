package DomainLayer.exception;

public class UnauthorizedPermissionException extends RuntimeException {
    public UnauthorizedPermissionException(String message) {
        super(message);
    }
}
