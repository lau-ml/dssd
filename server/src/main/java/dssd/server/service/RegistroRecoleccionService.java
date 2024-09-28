package dssd.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.server.exception.RegistroPendienteException;
import dssd.server.model.Recolector;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.repository.RecolectorRepository;
import dssd.server.repository.RegistroRecoleccionRepository;

@Service
public class RegistroRecoleccionService {
    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private RecolectorRepository recolectorRepository;

    public RegistroRecoleccion obtenerOcrearRegistro(Long idRecolector)
            throws RegistroPendienteException, RuntimeException {
        Optional<Recolector> recolectorOpt = recolectorRepository.findById(idRecolector);
        if (!recolectorOpt.isPresent()) {
            throw new RuntimeException("Recolector no encontrado.");
        }
        Recolector recolector = recolectorOpt.get();

        Optional<RegistroRecoleccion> registroNoCompletadoOpt = registroRecoleccionRepository
                .findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(recolector);

        // Si lo encuentro, lo retorno
        if (registroNoCompletadoOpt.isPresent()) {
            return registroNoCompletadoOpt.get();
        }

        // Si no, busco el ultimo registro completado
        Optional<RegistroRecoleccion> ultimoRegistroOpt = registroRecoleccionRepository
                .findTopByRecolectorOrderByFechaRecoleccionDesc(recolector);

        // Se crea un nuevo registro si no existe ninguno o si el ultimo ya fue pagado
        if (!ultimoRegistroOpt.isPresent() || (ultimoRegistroOpt.get().getPago() != null)) {
            RegistroRecoleccion nuevoRegistro = new RegistroRecoleccion();
            nuevoRegistro.setRecolector(recolector);
            nuevoRegistro.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId());
            nuevoRegistro.setCompletado(false);
            return registroRecoleccionRepository.save(nuevoRegistro);
        }

        // Al ultimo registro le falta ser validado
        throw new RegistroPendienteException("Tiene un registro pendiente de validación.");
    }

    public RegistroRecoleccion completarRegistroRecoleccion(Long id) {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(id).orElseThrow();
        registroRecoleccion.setCompletado(true);
        return registroRecoleccionRepository.save(registroRecoleccion);
    }
}
