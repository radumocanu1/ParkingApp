package unibuc.ro.ParkingApp.configuration.OIDC.Keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Value("${server-url}")
    String keycloakUrl;
    @Value("${clientID}")
    String clientID;
    @Bean
    Keycloak keycloak() {
        System.out.println(keycloakUrl);
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm("master")
                .clientId(clientID)
                .grantType(OAuth2Constants.PASSWORD)
                .username("root")
                .password("root")
                .build();
    }
}
