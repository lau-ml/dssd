package dssd.server.repository;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import dssd.server.model.StockMaterial;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface StockMaterialRepository extends JpaRepository<StockMaterial, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    StockMaterial findByCentroRecoleccionAndMaterial(CentroRecoleccion centroRecoleccion, Material material);
}
