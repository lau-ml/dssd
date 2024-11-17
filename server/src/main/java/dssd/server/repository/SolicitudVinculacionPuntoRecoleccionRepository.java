package dssd.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.SolicitudVinculacionPuntoRecoleccion;
import dssd.server.model.Usuario;

@Repository
public interface SolicitudVinculacionPuntoRecoleccionRepository
        extends JpaRepository<SolicitudVinculacionPuntoRecoleccion, Long> {

    List<SolicitudVinculacionPuntoRecoleccion> findByRecolector_IdAndEstado(Long recolectorId,
            SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud estado);

    List<SolicitudVinculacionPuntoRecoleccion> findByRecolectorAndEstado(Usuario recolector,
            SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud estado);

    List<SolicitudVinculacionPuntoRecoleccion> findByEstado(
            SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud estado);

    Optional<SolicitudVinculacionPuntoRecoleccion> findByRecolector_IdAndPuntoDeRecoleccion_Id(Long recolectorId,
            Long puntoDeRecoleccionId);

    Optional<SolicitudVinculacionPuntoRecoleccion> findByRecolectorAndPuntoDeRecoleccion(Usuario recolector,
            PuntoDeRecoleccion puntoDeRecoleccion);

    @Query("SELECT s.puntoDeRecoleccion.id FROM SolicitudVinculacionPuntoRecoleccion s WHERE s.recolector = :recolector AND s.estado = 'PENDIENTE'")
    List<Long> findPuntosConSolicitudPorRecolector(@Param("recolector") Usuario recolector);
}
