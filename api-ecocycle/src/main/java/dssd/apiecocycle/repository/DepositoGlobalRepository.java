package dssd.apiecocycle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.DepositoGlobal;
import org.springframework.data.jpa.repository.Query;

public interface DepositoGlobalRepository extends JpaRepository<DepositoGlobal, Long> {

    Optional<DepositoGlobal> findByEmail(String string);

    @Query(
            value =
                    "SELECT c FROM DepositoGlobal c WHERE " +
                            "(:email IS NULL OR c.email ILIKE CONCAT('%', :email, '%')) " +
                            "AND (:telefono IS NULL OR c.telefono LIKE CONCAT('%', :telefono, '%')) " +
                            "AND (:direccion IS NULL OR c.direccion ILIKE CONCAT('%', :direccion, '%'))"
    )
    Page<DepositoGlobal> findAll(String email, String telefono, String direccion, PageRequest of);
}
