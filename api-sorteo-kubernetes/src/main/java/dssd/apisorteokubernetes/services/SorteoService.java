package dssd.apisorteokubernetes.services;

import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.models.SorteoModel;
import dssd.apisorteokubernetes.repositories.InscripcionRepository;
import dssd.apisorteokubernetes.repositories.SorteoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SorteoService {

    @Autowired
    private SorteoRepository sorteoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Transactional
    public void sortear() {
        SorteoModel sorteo = sorteoRepository.findTopByActivoFalseOrderByFechaSorteoDesc();
        Optional<InscripcionModel> inscripcionGanadora = inscripcionRepository.findRandomInscripcionBySorteo(sorteo);
        inscripcionGanadora.ifPresent(ganador -> {
            sorteo.setInscripcionGanadora(ganador);
            sorteoRepository.save(sorteo);
        });

    }

    @Transactional
    public void inhabilitarInscripciones() {
        SorteoModel sorteo = sorteoRepository.findTopByActivoTrueOrderByFechaSorteoDesc();
        sorteo.setActivo(false);
        sorteoRepository.save(sorteo);
    }

    @Transactional
    public void crearSorteo() {
        LocalDate fechaSorteo = LocalDate.now().plusMonths(1);
        SorteoModel sorteo = SorteoModel.builder().activo(true).fechaSorteo(fechaSorteo).build();
        sorteoRepository.save(sorteo);
    }

}
