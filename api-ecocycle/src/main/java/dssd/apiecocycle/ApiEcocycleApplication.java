package dssd.apiecocycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class ApiEcocycleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiEcocycleApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Ecocycle Global Deposits API")
						.version("1.0.0")
						.description(
								"Esta API forma parte del proyecto Ecocycle y permite la gestión de depósitos globales de reciclaje. A través de esta API, los depósitos pueden consultar las necesidades de materiales, registrar órdenes de distribución y consolidar su relación con la red global de recicladores. También incluye funcionalidades para la consulta de existencias y estadísticas sobre los materiales reciclados.")
						.termsOfService("http://swagger.io/terms/")
						.contact(new Contact()
								.name("Equipo de Soporte de Ecocycle")
								.email("lautaromoller345@gmail.com")
								.url("http://ecocycle.com"))
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

}
