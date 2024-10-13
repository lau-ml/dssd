package dssd.apiecocycle.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.repository.MaterialRepository;
import dssd.apiecocycle.repository.OrdenRepository;
import jakarta.transaction.Transactional;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Transactional
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Transactional
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElse(null);
    }

    @Transactional
    public Material getMaterialByName(String nameMaterial) {
        return materialRepository.findByNombre(nameMaterial).orElse(null);
    }

    public Set<CentroDeRecepcion> getProveedoresPorMaterial(Material material) {
        List<Orden> ordenes = ordenRepository.findByMaterialAndEstado(material, EstadoOrden.ENTREGADO);

        return ordenes.stream()
                .map(Orden::getCentroDeRecepcion)
                .collect(Collectors.toSet());
    }
}