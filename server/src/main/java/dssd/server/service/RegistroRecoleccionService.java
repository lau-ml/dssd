package dssd.server.service;

import dssd.server.exception.RegistroPendienteException;
import dssd.server.model.Recolector;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.repository.RecolectorRepository;
import dssd.server.repository.RegistroRecoleccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistroRecoleccionService {
    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private RecolectorRepository recolectorRepository;

    public RegistroRecoleccion obtenerRegistro(Long idRecolector)
            throws RegistroPendienteException, RuntimeException {
        Optional<Recolector> recolectorOpt = recolectorRepository.findById(idRecolector);
        if (!recolectorOpt.isPresent()) {
            throw new RuntimeException("Recolector no encontrado.");
        }
        Recolector recolector = recolectorOpt.get();

        Optional<RegistroRecoleccion> registroNoCompletadoOpt = registroRecoleccionRepository
                .findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(recolector);


        registroRecoleccionRepository.findTopByRecolectorOrderByFechaRecoleccionDesc(recolector).ifPresent(registro -> {
            if (registro.isCompletado() && !registro.isVerificado()) {
                throw new RegistroPendienteException("Tiene un registro pendiente de validación.");
            }
        });
        return registroNoCompletadoOpt.get();
    }

    public RegistroRecoleccion completarRegistroRecoleccion(Long id) {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(id).orElseThrow();
        registroRecoleccion.setCompletado(true);
        return registroRecoleccionRepository.save(registroRecoleccion);
    }
}
