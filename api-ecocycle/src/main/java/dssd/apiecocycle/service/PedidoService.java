package dssd.apiecocycle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public List<Pedido> getOrdersByMaterial(Material material) {
        return pedidoRepository.findByMaterial(material);
    }

}
