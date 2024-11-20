package dssd.server.repository;

import java.util.List;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.Rol;
import dssd.server.model.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByIdAndRol(Long id, Rol rol);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmailAndVerificationCode(String email, String code);

    Usuario findByEmail(String email);

    Usuario findByVerificationCode(String code);

    Usuario findByContraCode(String code);

    List<Usuario> findByRolAndCentroRecoleccion(Rol rol, CentroRecoleccion centroRecoleccion);

    List<Usuario> findByRol_Nombre(String rol);

    Page<Usuario> findByPuntosDeRecoleccionContaining(PuntoDeRecoleccion punto, Pageable pageable);

    Page<Usuario> findByPuntosDeRecoleccionContainingAndNombreContainingIgnoreCase(PuntoDeRecoleccion punto,
            String search,
            Pageable pageable);

    Page<Usuario> findByPuntosDeRecoleccionNotContaining(PuntoDeRecoleccion puntoDeRecoleccion, Pageable pageable);

    Page<Usuario> findByPuntosDeRecoleccionNotContainingAndNombreContainingIgnoreCase(
            PuntoDeRecoleccion puntoDeRecoleccion, String nombre, Pageable pageable);
}
