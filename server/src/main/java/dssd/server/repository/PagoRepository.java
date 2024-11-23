package dssd.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findByRegistroRecoleccion_Recolector_IdAndMontoContainingIgnoreCase(
            Long recolectorId,
            String search,
            Pageable pageable);

    Page<Pago> findByRegistroRecoleccion_Recolector_Id(
            Long recolectorId,
            Pageable pageable);
}
