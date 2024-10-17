package dssd.apiecocycle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByMaterial(Material material);

    List<Pedido> findByMaterialAndAbastecido(Material material, boolean abastecido);

    Optional<Pedido> findByIdAndDepositoGlobal_Id(Long id, Long id1);

    List<Pedido> findAByMaterialAndDepositoGlobal_Id(Material material, Long id);
}
