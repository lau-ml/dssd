package dssd.apiecocycle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.repository.MaterialRepository;
import jakarta.transaction.Transactional;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

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
}