package dssd.server.repository;

import dssd.server.model.LoginBonita;
import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginBonitaRepository extends JpaRepository<LoginBonita, Long> {
    Optional<LoginBonita> findByUsuario_Username(String username);
}
