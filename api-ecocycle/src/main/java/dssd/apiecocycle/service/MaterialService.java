package dssd.apiecocycle.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.Material;
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
    public void agregarProveedor(Material material, CentroDeRecepcion centro) {
        material.addProveedor(centro);
        centro.addMaterial(material);
        updateMaterial(material);
    }
}
