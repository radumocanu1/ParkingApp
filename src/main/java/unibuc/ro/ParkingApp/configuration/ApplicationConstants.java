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
    String PARKING_SPOT_RENTED = "Vesti bune, %s ! Cineva a inchiriat locul de parcare <%s> de pe %s pana pe %s! (numarul masinii %s).";
    String PAYMENT_REJECTED= "Ne pare rau, se pare ca a fost o problema la procesarea platii... Va rugam sa incercati din nou, mai tarziu. Daca problema persista puteti incerca sa utilizati alt card pentru efectuarea tranzactiei si sa verificati soldul curent";
    String GENERIC_WELCOME_MESSAGE_TEMPLATE="Bine ai venit la Parco, %S! Acum te poti bucura de toate functionalitatile aplicatiei noastre! Aici vei primi actualizari cu privire la anunturile tale (inchiriate + de inchiriat). Spor la parcat!";
    String GENERIC_START_PARKING_MESSAGE="Verifica sectiunea Locuri inchiriate, un nou loc a devenit disponibil si poti incepe sa parchezi pe el chiar de acum!";
    String GENERIC_STOP_PARKING_MESSAGE="perioada de inchiriere a locului de parcare <%s> a expirat... Va rugam sa lasati un feedback in sectiunea anunturi inchiriate!";
    String GENERIC_PARKING_SPOT_FREED="Masina cu numarul %s a incheiat de astazi parcarea pe locul dumneavoasta";

    String GENERIC_PARKING_SPOT_OCCUPIED="Masina cu numarul %s va incepe de astazi sa parcheze pe locul de parcare postat de dumneavoastra <%s>. Intrega perioada inchiriata: %s zile";

}
