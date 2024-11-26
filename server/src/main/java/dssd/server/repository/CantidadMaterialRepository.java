package dssd.server.repository;

import dssd.server.model.CantidadMaterial;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CantidadMaterialRepository extends JpaRepository<CantidadMaterial, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CantidadMaterial> findByCentroRecoleccionAndMaterial(CentroRecoleccion centroRecoleccion, Material material);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CantidadMaterial> findByCentroRecoleccionAndMaterialAndPrimeraVez(CentroRecoleccion centroRecoleccion, Material material, boolean primeraVez);
}
