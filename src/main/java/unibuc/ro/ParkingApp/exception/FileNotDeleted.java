package unibuc.ro.ParkingApp.exception;

public class FileNotDeleted extends RuntimeException{
    public FileNotDeleted(String message) {
        super(message);
    }
}
