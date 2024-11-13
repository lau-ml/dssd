package dssd.apisorteokubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiSorteoKubernetesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSorteoKubernetesApplication.class, args);
    }

}
