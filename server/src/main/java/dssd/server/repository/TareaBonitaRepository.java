package dssd.server.repository;

import dssd.server.model.OrdenDeDistribucion;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.TareaBonita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TareaBonitaRepository extends JpaRepository<TareaBonita, Long>
{
    Optional<TareaBonita> findByRegistroRecoleccionAndRegistroRecoleccion_Completado(RegistroRecoleccion registroRecoleccion, boolean b);


    void deleteByRegistroRecoleccion(RegistroRecoleccion registroRecoleccion);

    Optional<TareaBonita> findByRegistroRecoleccion_Id(Long registroRecoleccionId);

    Optional<TareaBonita> findByOrdenDeDistribucionIdAndEstado(Long orden, OrdenDeDistribucion.EstadoOrden estadoOrden);
}
