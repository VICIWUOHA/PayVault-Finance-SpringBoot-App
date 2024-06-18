package vicmicroservices.payvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
//@EnableJdbcRepositories(basePackages = "vicmicroservices.payvault")
public class PayVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayVaultApplication.class, args);
	}

}
