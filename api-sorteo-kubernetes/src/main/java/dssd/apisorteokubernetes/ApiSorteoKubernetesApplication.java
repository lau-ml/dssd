package dssd.apisorteokubernetes;

import dssd.apisorteokubernetes.helpers.SorteoHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiSorteoKubernetesApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiSorteoKubernetesApplication.class, args);
        SorteoHelper helper = context.getBean(SorteoHelper.class);
        String task = System.getenv("TASK");

        if (task != null) {
            switch (task) {
                case "realizarSorteo":
                    helper.realizarSorteo();
                    break;
                case "inhabilitarInscripciones":
                    helper.inhabilitarInscripciones();
                    break;
                case "crearSorteo":
                    helper.crearSorteo();
                    break;
                default:
                    System.out.println("Tarea no reconocida");
            }
        }

    }

}
