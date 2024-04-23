package unibuc.ro.ParkingApp.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
                new DelegatingJwtGrantedAuthoritiesConverter(
                        new JwtGrantedAuthoritiesConverter(),
                        new KeycloakJwtRolesConverter());

        httpSecurity.oauth2ResourceServer().jwt().jwtAuthenticationConverter(
                jwt -> new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt)));
        httpSecurity
                .cors().and()
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/user").hasAuthority(KeycloakJwtRolesConverter.PREFIX_RESOURCE_ROLE + "admin")
                .requestMatchers("/admin").hasAuthority(KeycloakJwtRolesConverter.PREFIX_RESOURCE_ROLE + "rest-api_admin")
                .anyRequest().permitAll()
        );
//        httpSecurity.csrf().disable();
//        httpSecurity.authorizeHttpRequests().anyRequest().permitAll();

        return httpSecurity.build();
    }
}
