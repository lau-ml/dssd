package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.Centro;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Pedido;
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



    public Pedido obtenerPedido(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        if (centro.hasRole("ROLE_CENTER")) {
            return getPedidoById(id).orElseThrow();
        }
        if (centro.hasRole("ROLE_DEPOSIT")) {
            return getPedidoByIdAndDepositoGlobalId(id, centro.getId());
        }
        return getPedidoById(id).orElseThrow();

    }

    private Pedido getPedidoByIdAndDepositoGlobalId(Long id, Long id1) {
        return pedidoRepository.findByIdAndDepositoGlobal_Id(id, id1).orElseThrow();
    }

    public List<Pedido> getPedidosByMaterialName(String nameMaterial) throws CentroInvalidoException {
        Material material = materialService.getMaterialByName(nameMaterial);
        Centro centro = centroService.recuperarCentro();
        if (centro.hasPermission("CONSULTAR_TODOS_PEDIDOS")) {
            return getPedidosByMaterial(material);
        }
        if (centro.hasPermission("CONSULTAR_PEDIDO_PROPIO")) {
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
