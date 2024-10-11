package dssd.apiecocycle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByMaterial(Material material);

}
