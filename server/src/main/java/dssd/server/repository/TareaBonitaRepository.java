package dssd.server.repository;

import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.TareaBonita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TareaBonitaRepository extends JpaRepository<TareaBonita, Long>
{
    Optional<TareaBonita> findByRegistroRecoleccion(RegistroRecoleccion registroRecoleccion);
}
