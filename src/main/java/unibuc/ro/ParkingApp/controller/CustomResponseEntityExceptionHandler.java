package unibuc.ro.ParkingApp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.exception.ChatNotFound;
import unibuc.ro.ParkingApp.exception.ListingNotFound;
import unibuc.ro.ParkingApp.exception.OIDCUserNotFound;
import unibuc.ro.ParkingApp.exception.UserNotFound;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleUserNotFoundError(UserNotFound ex) {
        return new ResponseEntity<>(String.format(ApplicationConstants.USER_NOT_FOUND_TEMPLATE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OIDCUserNotFound.class)
    public ResponseEntity<String> handleOIDCUserNotFound(OIDCUserNotFound ex) {
        return new ResponseEntity<>(String.format(ApplicationConstants.OIDC_USER_NOT_FOUND_TEMPLATE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ChatNotFound.class)
    public ResponseEntity<String> handleChatNotFound(ChatNotFound ex) {
        return new ResponseEntity<>(String.format(ApplicationConstants.CHAT_NOT_FOUND_TEMPLATE), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ListingNotFound.class)
    public ResponseEntity<String> handleListingNotFoundError(ListingNotFound ex) {
        return new ResponseEntity<>(String.format(ApplicationConstants.LISTING_NOT_FOUND_TEMPLATE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}

