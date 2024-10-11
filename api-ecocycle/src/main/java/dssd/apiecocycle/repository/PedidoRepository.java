package dssd.apiecocycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
