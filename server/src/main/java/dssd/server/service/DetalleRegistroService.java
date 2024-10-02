package dssd.server.service;

import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.model.*;
import dssd.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DetalleRegistroService {
    @Autowired
    private DetalleRegistroRepository detalleRegistroRepository;

    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private RecolectorRepository recolectorRepository;
    @Transactional

    public RegistroRecoleccionDTO agregarDetalleRegistro(DetalleRegistroDTO detalleRegistroDTO) {

        if (!detalleRegistroDTO.validar()) {
            throw new RuntimeException("Faltan datos.");
        }
        Material material = materialRepository.findById(detalleRegistroDTO.getMaterial().getId())
                .orElseThrow(() -> new RuntimeException("Material no encontrado."));

        Ubicacion ubicacion = ubicacionRepository.findById(detalleRegistroDTO.getUbicacion().getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada."));

        Optional<RegistroRecoleccion> registroRecoleccionOpt;

        Recolector recolector = recolectorRepository.findById(detalleRegistroDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Recolector no encontrado."));
        registroRecoleccionOpt = registroRecoleccionRepository.findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(recolector);

        RegistroRecoleccion registroRecoleccion;
        if (registroRecoleccionOpt.isPresent()) {
            registroRecoleccion = registroRecoleccionOpt.get();
        } else {
            registroRecoleccion = new RegistroRecoleccion();
            registroRecoleccion.setRecolector(recolector);
            registroRecoleccion.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId());
            registroRecoleccion.setCompletado(false);
            registroRecoleccionRepository.save(registroRecoleccion);
        }


        DetalleRegistro nuevoDetalle = new DetalleRegistro();
        nuevoDetalle.setCantidadRecolectada(detalleRegistroDTO.getCantidadRecolectada());
        nuevoDetalle.setMaterial(material);
        nuevoDetalle.setUbicacion(ubicacion);
        nuevoDetalle.setRegistroRecoleccion(registroRecoleccion);

        detalleRegistroRepository.save(nuevoDetalle);



        return new RegistroRecoleccionDTO(registroRecoleccion);
    }
}
