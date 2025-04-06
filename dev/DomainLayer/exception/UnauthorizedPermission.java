package DomainLayer.exception;

public class UnauthorizedPermission extends RuntimeException {
    public UnauthorizedPermission(String message) {
        super(message);
    }
}
