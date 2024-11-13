package dssd.apisorteokubernetes.services;

import dssd.apisorteokubernetes.exceptions.SorteoExceptions;
import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.models.SorteoModel;
import dssd.apisorteokubernetes.repositories.InscripcionRepository;
import dssd.apisorteokubernetes.repositories.SorteoRepository;
import dssd.apisorteokubernetes.requests.CentroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InscripcionService {
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private SorteoRepository sorteoRepository;

    @Transactional
    public InscripcionModel inscribirse(CentroDTO centro) throws SorteoExceptions {
        SorteoModel sorteo = sorteoRepository.findTopByActivoTrueOrderByFechaSorteoDesc();
        if (sorteo == null) {
            throw new SorteoExceptions("No hay sorteos disponibles");
        }
        if (inscripcionRepository.findByCentroAndSorteo(centro.getIdCentro(), sorteo).isPresent()) {
            throw new SorteoExceptions("Ya se encuentra inscripto en el sorteo");
        }
        Optional<Long> numeroSorteo = inscripcionRepository.findLatestNumeroSorteoOfActualSorteo(sorteo);
        Long numero = numeroSorteo.orElse(0L) + 1;
        InscripcionModel inscripcion = InscripcionModel.builder().sorteo(sorteo).centro(centro.getIdCentro()).numeroInscripcionSorteo(numero).build();
        return inscripcionRepository.save(inscripcion);
    }
}
