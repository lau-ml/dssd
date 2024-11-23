package dssd.server.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dssd.server.DTO.OrdenDeDistribucionDTO;
import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.model.OrdenDeDistribucion;
import dssd.server.repository.OrdenDeDistribucionRepository;

@Service
public class OrdenDeDistribucionService {
    @Autowired
    private OrdenDeDistribucionRepository ordenDeDistribucionRepository;

    public PaginatedResponseDTO<OrdenDeDistribucionDTO> obtenerOrdenesPaginadasYFiltradas(
            Pageable pageable, String search, String estadoStr, Long centroRecoleccionId) {
        Page<OrdenDeDistribucion> ordenesPage;
        OrdenDeDistribucion.EstadoOrden estado = null;

        if (estadoStr != null && !estadoStr.trim().isEmpty()) {
            try {
                estado = OrdenDeDistribucion.EstadoOrden.valueOf(estadoStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de orden no válido: " + estadoStr);
            }
        }

        if ((search != null && !search.trim().isEmpty()) && estado != null) {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCaseAndEstado(
                            centroRecoleccionId, search, estado, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCase(
                            centroRecoleccionId, search, pageable);
        } else if (estado != null) {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionIdAndEstado(centroRecoleccionId, estado, pageable);
        } else {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionId(centroRecoleccionId, pageable);
        }

        List<OrdenDeDistribucionDTO> ordenesDTOs = ordenesPage.getContent().stream()
                .map(OrdenDeDistribucionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                ordenesDTOs,
                ordenesPage.getTotalPages(),
                ordenesPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public void cambiarEstado(Long id, OrdenDeDistribucion.EstadoOrden nuevoEstado) {
        OrdenDeDistribucion orden = ordenDeDistribucionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + id));

        if (esTransicionValida(orden.getEstado(), nuevoEstado)) {
            orden.setEstado(nuevoEstado);
            ordenDeDistribucionRepository.save(orden);
        } else {
            throw new IllegalStateException("Transición de estado no permitida");
        }
    }

    private boolean esTransicionValida(OrdenDeDistribucion.EstadoOrden estadoActual,
            OrdenDeDistribucion.EstadoOrden nuevoEstado) {
        Map<OrdenDeDistribucion.EstadoOrden, List<OrdenDeDistribucion.EstadoOrden>> transicionesValidas = Map.of(
                OrdenDeDistribucion.EstadoOrden.ACEPTADA, List.of(OrdenDeDistribucion.EstadoOrden.EN_PREPARACION),
                OrdenDeDistribucion.EstadoOrden.EN_PREPARACION, List.of(OrdenDeDistribucion.EstadoOrden.PREPARADO),
                OrdenDeDistribucion.EstadoOrden.PREPARADO, List.of(OrdenDeDistribucion.EstadoOrden.ENVIADO));

        return transicionesValidas.getOrDefault(estadoActual, List.of()).contains(nuevoEstado);
    }
}
