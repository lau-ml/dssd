package dssd.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dssd.server.exception.RegistroPendienteException;
import dssd.server.helpers.*;
import dssd.server.model.Recolector;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.repository.DetalleRegistroRepository;
import dssd.server.repository.RecolectorRepository;
import dssd.server.repository.RegistroRecoleccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroRecoleccionService {
    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private RecolectorRepository recolectorRepository;

    @Autowired
    private DetalleRegistroRepository detalleRegistroRepository;

    @Autowired
    private BonitaState bonitaState;

    @Transactional
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
    @Transactional

    public RegistroRecoleccion completarRegistroRecoleccion(Long id) throws JsonProcessingException {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(id).orElseThrow();
        registroRecoleccion.setCompletado(true);
        this.bonitaState.cargarActividadBonita();
        this.bonitaState.set_recoleccion_confirmar();
        this.bonitaState.asignarActividadBonita();
        this.bonitaState.completarActividadBonita();
        return registroRecoleccionRepository.save(registroRecoleccion);
    }
    @Transactional
    public void eliminarRegistroRecoleccion(Long id) throws JsonProcessingException {
        this.bonitaState.cargarActividadBonita();
        this.bonitaState.set_recoleccion_cancelar();
        this.bonitaState.asignarActividadBonita();
        this.bonitaState.completarActividadBonita();
        detalleRegistroRepository.deleteByRegistroRecoleccion(registroRecoleccionRepository.findById(id).get());
        registroRecoleccionRepository.deleteById(id);

    }
}
