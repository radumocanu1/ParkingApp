package unibuc.ro.ParkingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import unibuc.ro.ParkingApp.configuration.SecurityConfig;
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableScheduling

public class ParkingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingAppApplication.class, args);
	}

}
