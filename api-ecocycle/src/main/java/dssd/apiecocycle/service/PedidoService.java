package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.*;
import dssd.apiecocycle.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CentroService centroService;
    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> getPedidosByMaterial(Material material) {
        return pedidoRepository.findByMaterial(material);
    }


    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }


    public List<Pedido> getpedidosByMaterialAndAbastecido(Material material, boolean b) {
        return pedidoRepository.findByMaterialAndAbastecido(material, b);
    }

    public Pedido obtenerPedido(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Pedido pedido = centro.getPedidoById(id);
        if (pedido == null) {
            throw new NoSuchElementException("Pedido no encontrado");
        }
        return pedido;
    }


    public List<Pedido> getPedidosByMaterialName(String nameMaterial) throws CentroInvalidoException {
        Material material = materialService.getMaterialByName(nameMaterial);
        Centro centro = centroService.recuperarCentro();
        if (centro.onlyMinePedidos()) {
            return getpedidosByMaterialAndDepositoGlobalId(material, centro.getId());
        }
        return getPedidosByMaterial(material);
    }

    private List<Pedido> getpedidosByMaterialAndDepositoGlobalId(Material material, Long id) {
        return pedidoRepository.findAByMaterialAndDepositoGlobal_Id(material, id);
    }


    public Pedido crearPedido(CreatePedidoDTO createPedidoDTO) throws CentroInvalidoException {
        Material material = materialService.getMaterialById(createPedidoDTO.getMaterialId());
        if (material == null) {
            throw new NoSuchElementException("Material no encontrado");
        }
        if (createPedidoDTO.getCantidad() < 1) {
            throw new CantidadException("La cantidad del pedido debe ser mayor a cero");
        }
        DepositoGlobal depositoGlobal = (DepositoGlobal) centroService.recuperarCentro();
        Pedido newPedido = new Pedido(material, createPedidoDTO.getCantidad(), depositoGlobal);
        return savePedido(newPedido);
    }

    public Pedido updateCantSupplied(Pedido pedido, int cantidad) {
        pedido.setCantidadAbastecida(pedido.getCantidadAbastecida() + cantidad);
        if (pedido.getCantidadAbastecida() >= pedido.getCantidad()) {
            pedido.setAbastecido(true);
        }
        return savePedido(pedido);
    }

}
