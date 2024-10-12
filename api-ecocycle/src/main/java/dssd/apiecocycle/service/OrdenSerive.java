package dssd.apiecocycle.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.repository.OrdenRepository;

@Service
public class OrdenSerive {

    @Autowired
    private OrdenRepository ordenRepository;

    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> getOrdersByPedido(Optional<Pedido> pedido) {
        return ordenRepository.findByPedido(pedido);
    }

}
