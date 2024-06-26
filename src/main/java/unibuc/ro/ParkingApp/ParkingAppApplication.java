package unibuc.ro.ParkingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import unibuc.ro.ParkingApp.configuration.SecurityConfig;
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })

public class ParkingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingAppApplication.class, args);
	}

}
