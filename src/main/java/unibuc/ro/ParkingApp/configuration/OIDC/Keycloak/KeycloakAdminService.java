package unibuc.ro.ParkingApp.configuration.OIDC.Keycloak;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
@Log
public class KeycloakAdminService {
    @Autowired
    Keycloak keycloak;
    @Value("${realm}")
    String REALM_NAME;

    public void deleteUser(String userId){
        log.info("Deleting keycloak user with subject: "+userId);
        UsersResource usersResource = keycloak.realm(REALM_NAME).users();
        Response response = usersResource.delete(userId);
        if (response.getStatus() == 204) {
            log.info("Keycloak user was successfully deleted.");
        } else {
            log.severe("Error when trying to delete user, code: " + response.getStatus());
        }
    }

    public void updateUser(String userId, String newUsername, String newEmail) {
        UserResource userResource = keycloak.realm(REALM_NAME).users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        updateEmail(userRepresentation, newEmail);
        updateUsername(userRepresentation, newUsername);
        userResource.update(userRepresentation);

    }
    private void updateUsername(UserRepresentation userRepresentation, String newUsername) {
        if (newUsername != null) {
            userRepresentation.setUsername(newUsername);
        }
    }
    private void updateEmail(UserRepresentation userRepresentation, String newEmail) {
        if (newEmail != null) {
            userRepresentation.setEmail(newEmail);
        }
    }




}
