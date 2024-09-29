package dssd.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.model.DetalleRegistro;
import dssd.server.model.Material;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.Ubicacion;
import dssd.server.repository.DetalleRegistroRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.repository.RegistroRecoleccionRepository;
import dssd.server.repository.UbicacionRepository;

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

        public RegistroRecoleccionDTO agregarDetalleRegistro(DetalleRegistroDTO detalleRegistroDTO) {
                Optional<RegistroRecoleccion> registroRecoleccionOpt = registroRecoleccionRepository
                                .findById(detalleRegistroDTO.getIdRegistroRecoleccion());
                if (!registroRecoleccionOpt.isPresent()) {
                        throw new RuntimeException("Registro de recolección no encontrado.");
                }

                RegistroRecoleccion registroRecoleccion = registroRecoleccionOpt.get();

                Material material = materialRepository.findById(detalleRegistroDTO.getMaterial().getId())
                                .orElseThrow(() -> new RuntimeException("Material no encontrado."));

                Ubicacion ubicacion = ubicacionRepository.findById(detalleRegistroDTO.getUbicacion().getId())
                                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada."));

                DetalleRegistro nuevoDetalle = new DetalleRegistro();
                nuevoDetalle.setCantidadRecolectada(detalleRegistroDTO.getCantidadRecolectada());
                nuevoDetalle.setMaterial(material);
                nuevoDetalle.setUbicacion(ubicacion);
                nuevoDetalle.setRegistroRecoleccion(registroRecoleccion);

                detalleRegistroRepository.save(nuevoDetalle);

                registroRecoleccion = registroRecoleccionRepository
                                .findById(detalleRegistroDTO.getIdRegistroRecoleccion())
                                .get();

                return new RegistroRecoleccionDTO(registroRecoleccion);
        }
}
