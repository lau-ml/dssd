package dssd.server.repository;

import dssd.server.model.Rol;
import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByIdAndRol(Long id, Rol rol);
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmailAndVerificationCode(String email, String code);


    Usuario findByEmail(String email);

    Usuario findByVerificationCode(String code);

    Usuario findByContraCode(String code);
}
