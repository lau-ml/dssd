package dssd.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.OrdenDeDistribucion;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdenDeDistribucionRepository extends JpaRepository<OrdenDeDistribucion, Long> {

    Page<OrdenDeDistribucion> findByCentroRecoleccionId(Long centroRecoleccionId, Pageable pageable);

    List<OrdenDeDistribucion> findByAndFechaCreacionBetween(
            Long centroRecoleccionId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    Page<OrdenDeDistribucion> findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCase(
            Long centroRecoleccionId, String materialNombre, Pageable pageable);

    Page<OrdenDeDistribucion> findByCentroRecoleccionIdAndEstado(
            Long centroRecoleccionId, OrdenDeDistribucion.EstadoOrden estado, Pageable pageable);

    Page<OrdenDeDistribucion> findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCaseAndEstado(
            Long centroRecoleccionId, String materialNombre, OrdenDeDistribucion.EstadoOrden estado, Pageable pageable);
}
