package dssd.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dssd.server.DTO.MaterialDTO;
import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.exception.MaterialYaExistenteException;
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

    public Material obtenerMaterialPorId(Long id) {
        return materialRepository.findById(id).orElse(null);
    }

    public Material editarMaterial(Long id, MaterialDTO materialDTO) {
        Material materialExistente = materialRepository.findById(id).orElse(null);
        if (materialExistente == null) {
            return null;
        }

        if (materialRepository.existsByNombreAndIdNot(materialDTO.getNombre(), id)) {
            return null;
        }

        if (materialDTO.getPrecio() == null || materialDTO.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser nulo o negativo");
        }

        materialExistente.setPrecio(materialDTO.getPrecio());
        return materialRepository.save(materialExistente);
    }

    public PaginatedResponseDTO<MaterialDTO> obtenerMaterialesPaginados(Pageable pageable) {
        Page<Material> paginatedMaterials = materialRepository.findAllByIsDeletedFalse(pageable);
        List<MaterialDTO> content = paginatedMaterials.getContent().stream()
                .map(MaterialDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<MaterialDTO>(
                content,
                paginatedMaterials.getTotalPages(),
                paginatedMaterials.getTotalElements(),
                paginatedMaterials.getNumber(),
                paginatedMaterials.getSize());
    }

    public PaginatedResponseDTO<MaterialDTO> obtenerMaterialesFiltrados(Pageable pageable, String search) {
        Page<Material> materialesPaginados = materialRepository
                .findByNombreContainingIgnoreCaseAndIsDeletedFalse(search, pageable);

        List<MaterialDTO> content = materialesPaginados.getContent().stream()
                .map(MaterialDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                materialesPaginados.getTotalPages(),
                materialesPaginados.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public boolean eliminarMaterial(Long id) {
        Material materialExistente = materialRepository.findById(id).orElse(null);
        if (materialExistente == null) {
            return false;
        }

        materialExistente.setDeleted(true);
        materialRepository.save(materialExistente);

        return true;
    }

    @Transactional
    public MaterialDTO crearMaterial(MaterialDTO materialDTO) {

        Material materialExistente = materialRepository
                .findByNombreIgnoreCase(materialDTO.getNombre());

        if (materialExistente != null) {
            if (!materialExistente.isDeleted()) {
                throw new MaterialYaExistenteException(materialDTO.getNombre());
            } else {
                materialExistente.setDescripcion(materialDTO.getDescripcion());
                materialExistente.setDeleted(false);
                materialRepository.save(materialExistente);
                return new MaterialDTO(materialExistente);
            }
        }

        Material nuevoMaterial = new Material();
        nuevoMaterial.setNombre(materialDTO.getNombre());
        nuevoMaterial.setDescripcion(materialDTO.getDescripcion());
        nuevoMaterial.setDeleted(false);
        materialRepository.save(nuevoMaterial);

        return new MaterialDTO(nuevoMaterial);
    }

}
