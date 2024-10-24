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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    public Pedido crearPedido(CreatePedidoDTO createPedidoDTO) throws CentroInvalidoException {
        Material material = materialService.getMaterialById(createPedidoDTO.getMaterialId());
        List<Pedido> pedido = pedidoRepository.findAllByMaterial_IdAndDepositoGlobal_Id(material.getId(), centroService.recuperarCentro().getId());
        if(pedido.stream().anyMatch(p -> !p.isAbastecido())){
            throw new CantidadException("Ya existe un pedido pendiente para el material seleccionado");
        }
        if (createPedidoDTO.getCantidad() < 1) {
            throw new CantidadException("La cantidad del pedido debe ser mayor a cero");
        }
        DepositoGlobal depositoGlobal = (DepositoGlobal) centroService.recuperarCentro();
        Pedido newPedido = new Pedido(material, createPedidoDTO.getCantidad(), depositoGlobal);
        return savePedido(newPedido);
    }

    public void updateCantSupplied(Pedido pedido, Long cantidad) {
        pedido.setCantidadAbastecida((int) (pedido.getCantidadAbastecida() + cantidad));
        if (pedido.getCantidadAbastecida() == pedido.getCantidad()) {
            pedido.setAbastecido(true);
        }
        savePedido(pedido);
    }


    public Page<Pedido> getMisPedidos(int i, int pageSize, String materialNombre, Boolean abastecido, LocalDate fechaPedido, LocalDateTime lastUpdate, Integer cantidad) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        return pedidoRepository.findAllByParamsAndDepositoGlobal_Id(PageRequest.of(i, pageSize), materialNombre, abastecido, fechaPedido, lastUpdate, cantidad, centro.getId());
    }

    public Page<Pedido> getAllPedidos(int page, int pageSize, String materialNombre, Boolean abastecido, LocalDate fechaPedido,LocalDateTime lastUpdate, Integer cantidad) {
        return pedidoRepository.findAllByParams(PageRequest.of(page, pageSize), materialNombre, abastecido, fechaPedido,lastUpdate, cantidad);
    }
}
