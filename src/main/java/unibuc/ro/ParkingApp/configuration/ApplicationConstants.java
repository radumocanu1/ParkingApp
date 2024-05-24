package unibuc.ro.ParkingApp.configuration;

public interface ApplicationConstants {
    String USER_NOT_FOUND_TEMPLATE = "User with UUID %s was not found in DB";
    String OIDC_USER_NOT_FOUND_TEMPLATE = "OIDC User with sub claim %s was not found in DB";
    String CHAT_NOT_FOUND_TEMPLATE = "Chat not found in DB";
    String LISTING_NOT_FOUND_TEMPLATE = "Listing with UUID %s was not found in DB";
    String PROFILE_PICTURE_UPDATED = "The user with sub %s successfully updated the profile picture";
    String LISTING_PHOTO_ADDED = "New photo has been successfully added to listing with id -> %s";
    String MESSAGE_SENT = "Message was sent";
    String PENDING_PAYMENT_NOT_FOUND = "Pending payment was not found in DB";
    String PAYMENT_ACCEPTED = "Plata efectuata a fost acceptata! Va rugam verificati sectiunea <Locuri inchiriate>";
    String PAYMENT_REJECTED= "Ne pare rau, se pare ca a fost o problema la procesarea platii... Va rugam sa incercati din nou, mai tarziu. Daca problema persista puteti incerca sa utilizati alt card pentru efectuarea tranzactiei si sa verificati soldul curent";
    String GENERIC_WELCOME_MESSAGE_TEMPLATE="Bine ai venit la Parco, %S! Poti face X,Y,Z!";

}
