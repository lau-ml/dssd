package dssd.apiecocycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
