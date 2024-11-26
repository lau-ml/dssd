package dssd.server.repository;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findByRegistroRecoleccion_Recolector_Id(
            Long recolectorId, Pageable pageable);

    Page<Pago> findByRegistroRecoleccion_Recolector_IdAndEstado(
            Long recolectorId, Pago.EstadoPago estado, Pageable pageable);

    Page<Pago> findByRegistroRecoleccion_Recolector_IdAndEstadoAndFechaPagoBetween(
            Long recolectorId, Pago.EstadoPago estado, Date fechaDesde, Date fechaHasta, Pageable pageable);

    Page<Pago> findByRegistroRecoleccion_Recolector_IdAndEstadoAndFechaEmisionBetween(
            Long recolectorId, Pago.EstadoPago estado, Date fechaDesde, Date fechaHasta, Pageable pageable);

    Page<Pago> findByRegistroRecoleccion_Recolector_IdAndFechaPagoBetween(
            Long recolectorId, Date fechaDesde, Date fechaHasta, Pageable pageable);

    Page<Pago> findByRegistroRecoleccion_Recolector_IdAndFechaEmisionBetween(
            Long recolectorId, Date fechaDesde, Date fechaHasta, Pageable pageable);
}
