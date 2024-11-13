package dssd.apisorteokubernetes.helpers;

import dssd.apisorteokubernetes.services.SorteoService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SorteoHelper {

    @Autowired
    private SorteoService sorteoService;

    @Scheduled(cron = "0 13 18 13 * ?", zone = "America/Argentina/Buenos_Aires")
    public void inhabilitarInscripciones() {
        sorteoService.inhabilitarInscripciones();
    }
    @Scheduled(cron = "30 13 18 13 * ?", zone = "America/Argentina/Buenos_Aires")
    public void realizarSorteo() {
        sorteoService.sortear();
    }

    @Scheduled(cron = "0 14 18 13 * ?", zone = "America/Argentina/Buenos_Aires")

    public void crearSorteo() {
        sorteoService.crearSorteo();
    }
}
