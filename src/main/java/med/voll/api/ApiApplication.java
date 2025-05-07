package med.voll.api;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ApiApplication.class, args);

		LoggerFactory.getLogger(ApiApplication.class).info(
			"\n\n" +
			"#################################################\n" + 
			"#                                               #\n" + 
			"# Spring Boot application started successfully. #\n" + 
			"#                                               #\n" + 
			"#################################################\n" +
			"\n"
		);
	}

}
