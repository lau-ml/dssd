package dssd.apisorteokubernetes.initializer;

import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.models.SorteoModel;
import dssd.apisorteokubernetes.repositories.InscripcionRepository;
import dssd.apisorteokubernetes.repositories.SorteoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseInitializer implements ApplicationRunner {

    @Autowired
    private SorteoRepository sorteoRepository;

    @Autowired
    InscripcionRepository inscripcionRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        String task = System.getenv("TASK");
        if (task == null) {
            SorteoModel sorteo = SorteoModel.builder().fechaSorteo(LocalDate.parse("2024-11-13")).activo(true).build();
            sorteoRepository.save(sorteo);
            List<InscripcionModel> inscripciones = new ArrayList<>();
            inscripciones.add(InscripcionModel.builder().numeroInscripcionSorteo(2L).centro(2L).sorteo(sorteo).build());
            inscripciones.add(InscripcionModel.builder().numeroInscripcionSorteo(1L).centro(1L).sorteo(sorteo).build());
            inscripciones.add(InscripcionModel.builder().numeroInscripcionSorteo(3L).centro(3L).sorteo(sorteo).build());
            sorteo.setInscripciones(inscripciones);
            inscripcionRepository.saveAll(inscripciones);
            sorteoRepository.save(sorteo);
        }
    }


}
