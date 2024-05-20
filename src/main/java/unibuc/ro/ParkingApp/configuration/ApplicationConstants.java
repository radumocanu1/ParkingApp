package unibuc.ro.ParkingApp.configuration;

public interface ApplicationConstants {
    String USER_NOT_FOUND_TEMPLATE = "User with UUID %s was not found in DB";
    String OIDC_USER_NOT_FOUND_TEMPLATE = "OIDC User with sub claim %s was not found in DB";
    String LISTING_NOT_FOUND_TEMPLATE = "Listing with UUID %s was not found in DB";
    String PROFILE_PICTURE_UPDATED = "The user with sub %s successfully updated the profile picture";
    String LISTING_PHOTO_ADDED = "New photo has been successfully added to listing with id -> %s";
    String MESSAGE_SENT = "Message was sent";

}
