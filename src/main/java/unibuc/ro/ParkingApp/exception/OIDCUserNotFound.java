package unibuc.ro.ParkingApp.exception;

public class OIDCUserNotFound extends RuntimeException{
    public OIDCUserNotFound(String message) {
        super(message);
    }
}