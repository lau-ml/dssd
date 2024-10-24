package dssd.apiecocycle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.CentroDeRecepcion;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CentroDeRecepcionRepository extends JpaRepository<CentroDeRecepcion, Long> {

    @Query(
            value =
                    "SELECT c FROM CentroDeRecepcion c WHERE " +
                            "(:email IS NULL OR c.email LIKE CONCAT('%', :email, '%')) " +
                            "AND (:telefono IS NULL OR c.telefono LIKE CONCAT('%', :telefono, '%')) " +
                            "AND (:direccion IS NULL OR c.direccion LIKE CONCAT('%', :direccion, '%'))"
    )
    Page<CentroDeRecepcion> findAll(String email, String telefono, String direccion, PageRequest of);

    @Query("SELECT c FROM CentroDeRecepcion c " +
            "INNER JOIN c.materiales m " +
            "WHERE (:materialId IS NULL OR m.id = :materialId) " +
            "AND (:email IS NULL OR c.email LIKE CONCAT('%', :email, '%')) " +
            "AND (:telefono IS NULL OR c.telefono LIKE CONCAT('%', :telefono, '%')) " +
            "AND (:direccion IS NULL OR c.direccion LIKE CONCAT('%', :direccion, '%'))")
    Page<CentroDeRecepcion> findByMaterialId(Long materialId, String email, String telefono, String direccion, PageRequest of);
}
