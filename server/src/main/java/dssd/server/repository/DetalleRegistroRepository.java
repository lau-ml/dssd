package dssd.server.repository;

import dssd.server.model.RegistroRecoleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import dssd.server.model.DetalleRegistro;

public interface DetalleRegistroRepository extends JpaRepository<DetalleRegistro, Long> {



    void deleteByRegistroRecoleccion(RegistroRecoleccion registroRecoleccion);
}
