package dssd.apiecocycle.service;

import dssd.apiecocycle.requests.CreatePedidoRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CentroService centroService;

    @Transactional(readOnly = true)
    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }


    @Transactional
    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }


    @Transactional(readOnly = true)
    public Pedido obtenerPedido(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        if (centro.hasRole("ROLE_CENTRO_RECEPCION")) {
            return getPedidoById(id).orElseThrow();
        }
        if (centro.hasRole("ROLE_DEPOSITO_GLOBAL")) {
            return getPedidoByIdAndDepositoGlobalId(id, centro.getId());
        }
        return getPedidoById(id).orElseThrow();

    }

    private Pedido getPedidoByIdAndDepositoGlobalId(Long id, Long id1) {
        return pedidoRepository.findByIdAndDepositoGlobal_Id(id, id1).orElseThrow();
    }

@Transactional
    public Pedido crearPedido(CreatePedidoRequest createPedidoRequest) throws CentroInvalidoException {
        Material material = materialService.getMaterialById(createPedidoRequest.getMaterialId());
        Centro centro = centroService.recuperarCentro();
        List<Pedido> pedido = pedidoRepository.findAllByMaterial_IdAndDepositoGlobal_Id(material.getId(), centro.getId());
        if (pedido.stream().anyMatch(p -> !p.isAbastecido())) {
            throw new CantidadException("Ya existe un pedido pendiente para el material seleccionado");
        }
        DepositoGlobal depositoGlobal = (DepositoGlobal) centro;
        Pedido newPedido = new Pedido(material, createPedidoRequest.getCantidad(), depositoGlobal);
        return savePedido(newPedido);
    }

    @Transactional
    public void updateCantSupplied(Pedido pedido, Long cantidad) {
        pedido.setCantidadAbastecida(pedido.getCantidadAbastecida() + cantidad);
        if (Objects.equals(pedido.getCantidadAbastecida(), pedido.getCantidad())) {
            pedido.setAbastecido(true);
        }
        savePedido(pedido);
    }


    @Transactional(readOnly = true)
    public Page<Pedido> getMisPedidos(int i, int pageSize, String materialNombre, Boolean abastecido, LocalDate fechaPedido, LocalDate lastUpdate, Integer cantidad) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        return pedidoRepository.findAllByParamsAndDepositoGlobal_Id(PageRequest.of(i, pageSize), materialNombre, abastecido, fechaPedido, lastUpdate, cantidad, centro.getId());
    }

    @Transactional(readOnly = true)
    public Page<Pedido> getAllPedidos(int page, int pageSize, String materialNombre, Boolean abastecido, LocalDate fechaPedido, LocalDate lastUpdate, Integer cantidad) {
        return pedidoRepository.findAllByParams(PageRequest.of(page, pageSize), materialNombre, abastecido, fechaPedido, lastUpdate, cantidad);
    }
}
