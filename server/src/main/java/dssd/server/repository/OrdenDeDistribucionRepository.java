package dssd.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.OrdenDeDistribucion;

public interface OrdenDeDistribucionRepository extends JpaRepository<OrdenDeDistribucion, Long> {

    Page<OrdenDeDistribucion> findByCentroRecoleccionId(Long centroRecoleccionId, Pageable pageable);

    Page<OrdenDeDistribucion> findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCase(
            Long centroRecoleccionId, String materialNombre, Pageable pageable);

    Page<OrdenDeDistribucion> findByCentroRecoleccionIdAndEstado(
            Long centroRecoleccionId, OrdenDeDistribucion.EstadoOrden estado, Pageable pageable);

    Page<OrdenDeDistribucion> findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCaseAndEstado(
            Long centroRecoleccionId, String materialNombre, OrdenDeDistribucion.EstadoOrden estado, Pageable pageable);
}
