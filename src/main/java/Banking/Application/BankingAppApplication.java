package Banking.Application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@SpringBootApplication
@OpenAPIDefinition(
		info= @Info(
				title = "Banking app",
				description="Backend Rest APIs for Banking app",
				version = "v1.1",
				contact= @Contact
						(
						name = "Kimutai Kemboi Nehemiah",
						email= "nehemiahkimutai32@gmail.com",
						url = "https://github.com/Banking_app"
				                 )
		),
		externalDocs=@ExternalDocumentation(
				description = "Spring Academy",
				url="www.spring.co.ke"
		)
)
@Component
public class BankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

}
