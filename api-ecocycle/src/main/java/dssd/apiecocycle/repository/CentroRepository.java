package dssd.apiecocycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Centro;

import java.util.Optional;

public interface CentroRepository extends JpaRepository<Centro, Long> {

    Optional<Centro> findByEmail(String email);
}
