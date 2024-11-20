package dssd.server.repository;

import dssd.server.model.RegistroRecoleccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import dssd.server.model.DetalleRegistro;
import dssd.server.model.PuntoDeRecoleccion;

public interface DetalleRegistroRepository extends JpaRepository<DetalleRegistro, Long> {

    void deleteByRegistroRecoleccion(RegistroRecoleccion registroRecoleccion);

    List<DetalleRegistro> findByPuntoRecoleccion(PuntoDeRecoleccion puntoDeRecoleccion);
}
