package dssd.apiecocycle.service;

import dssd.apiecocycle.exceptions.ProveedoresException;
import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.repository.MaterialRepository;
import dssd.apiecocycle.repository.OrdenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private CentroDeRecepcionService centroDeRecepcionService;

    @Transactional
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Transactional
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Material no encontrado"));
    }

    @Transactional
    public Material getMaterialByName(String nameMaterial) {
        return materialRepository.findByNombre(nameMaterial).orElseThrow();
    }

    @Transactional
    public Set<CentroDeRecepcion> getProveedoresPorMaterial(Long materialId) {
        Material material = getMaterialById(materialId);
        return new HashSet<>(material.getProveedores());
    }

    @Transactional
    public Material createMaterial(String nombre, String descripcion) {
        Material material = new Material(nombre, descripcion);
        return materialRepository.save(material);
    }

    @Transactional
    public void updateMaterial(Material material) {

        materialRepository.save(material);
    }

    @Transactional
    public void agregarProveedor(Long materialId, Long centroId) {
        Material material = this.getMaterialById(materialId);
        CentroDeRecepcion centro = centroDeRecepcionService.getCentroDeRecepcionById(centroId);
        if (material.contieneProveedor(centro)) {
            throw new ProveedoresException("El proveedor ya se encuentra asociado al material");
        }
        material.addProveedor(centro);
        centro.addMaterial(material);
        updateMaterial(material);
    }
}
