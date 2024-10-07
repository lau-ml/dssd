package dssd.server.repository;

import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByUsername(String username);


    Usuario findByEmail(String email);

    Usuario findByVerificationCode(String code);

    Usuario findByContraCode(String code);
}