package dssd.server.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.server.model.Material;
import dssd.server.repository.MaterialRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;
    @Transactional

    public List<Material> obtenerMateriales() {
        return materialRepository.findAll();
    }
}
