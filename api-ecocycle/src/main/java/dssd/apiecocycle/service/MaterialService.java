package dssd.apiecocycle.service;

import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.ProveedoresException;
import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;
import dssd.apiecocycle.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

   @Autowired
    private CentroService centroService;

   @Autowired
    private CentroDeRecepcionRepository centroDeRecepcionRepository;

    @Transactional(readOnly = true)
    public Page<Material> getAllMaterials(String nombre, String descripcion, int i, int pageSize) {
        return materialRepository.findAll(
                nombre, descripcion, PageRequest.of(i, pageSize)
        );
    }

    @Transactional(readOnly = true)
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Material no encontrado"));
    }

    @Transactional
    public Material getMaterialByName(String nameMaterial) {
        return materialRepository.findByNombre(nameMaterial).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<CentroDeRecepcion> getProveedoresPorMaterial(Long materialId, String email,String telefono, String direccion,int page, int pageSize) {
       return centroDeRecepcionRepository.findByMaterialId(materialId, email, telefono, direccion, PageRequest.of(page, pageSize));

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
    public void agregarProveedor(Long materialId) throws CentroInvalidoException {
        Material material = this.getMaterialById(materialId);
        CentroDeRecepcion centro = (CentroDeRecepcion) centroService.recuperarCentro();
        if (material.contieneProveedor(centro)) {
            throw new ProveedoresException("El proveedor ya se encuentra asociado al material");
        }
        material.addProveedor(centro);
        centro.addMaterial(material);
        updateMaterial(material);
    }
}
