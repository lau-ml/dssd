package dssd.apiecocycle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.DepositoGlobal;

public interface DepositoGlobalRepository extends JpaRepository<DepositoGlobal, Long> {

    Optional<DepositoGlobal> findByEmail(String string);

}
