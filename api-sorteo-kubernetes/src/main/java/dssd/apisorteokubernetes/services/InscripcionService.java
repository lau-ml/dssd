package dssd.apisorteokubernetes.services;

import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.repositories.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {
    @Autowired
    private InscripcionRepository inscripcionRepository;

    public InscripcionModel inscribirse(Long idCentro) {
        Long numero = inscripcionRepository.findLatestNumeroSorteo();
        InscripcionModel sorteo = InscripcionModel.builder().centro(idCentro).build();
        return inscripcionRepository.save(sorteo);
    }
}
