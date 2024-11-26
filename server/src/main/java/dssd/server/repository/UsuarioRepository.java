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

    Page<Usuario> findByPuntosDeRecoleccionContainingAndRolAndActivo(
            PuntoDeRecoleccion punto,
            Rol rol,
            Pageable pageable,
            boolean activo);

    Page<Usuario> findByPuntosDeRecoleccionContainingAndNombreContainingIgnoreCaseAndRolAndActivo(
            PuntoDeRecoleccion punto,
            String search,
            Pageable pageable,
            Rol rol,
            boolean activo);

    Page<Usuario> findByPuntosDeRecoleccionNotContainingAndRolAndActivo(
            PuntoDeRecoleccion puntoDeRecoleccion,
            Rol rol,
            Pageable pageable,
            boolean activo);

    Page<Usuario> findByPuntosDeRecoleccionNotContainingAndNombreContainingIgnoreCaseAndRolAndActivo(
            PuntoDeRecoleccion puntoDeRecoleccion,
            String nombre,
            Pageable pageable,
            Rol rol,
            boolean activo);

    Page<Usuario> findByNombreContainingIgnoreCaseAndRol(String search, Rol rol, Pageable pageable);

    Page<Usuario> findByRol(Rol rol, Pageable pageable);

    Page<Usuario> findByRolAndCentroRecoleccionAndActivo(Rol rol, CentroRecoleccion centroRecoleccion, boolean activo,
            Pageable pageable);

    Page<Usuario> findByRolAndCentroRecoleccionAndActivoAndNombreContainingIgnoreCaseOrDniContainingIgnoreCase(
            Rol rol,
            CentroRecoleccion centroRecoleccion,
            boolean activo,
            String searchNombre,
            String searchDni,
            Pageable pageable);

}
