package unibuc.ro.ParkingApp.exception;

public class OperationForbidden extends RuntimeException {
    public OperationForbidden(String message) {
        super(message);
    }
}
