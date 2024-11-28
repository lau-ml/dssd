package dssd.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.MaterialDTO;
import dssd.server.DTO.PaginatedResponseDTO;
// import dssd.server.exception.MaterialYaExistenteException;
import dssd.server.model.Material;
import dssd.server.service.MaterialService;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/get-materials")
    public ResponseEntity<?> obtenerMateriales() {
        try {
            List<Material> materiales = materialService.obtenerMateriales();
            List<MaterialDTO> materialesDTO = materiales.stream()
                    .map(MaterialDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(materialesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_MATERIALES')")
    @GetMapping("/get-materials-paginated")
    public ResponseEntity<?> obtenerMaterialesPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ordenColumna,
            @RequestParam(defaultValue = "true") boolean ordenAscendente) {
        try {

            Sort sort = Sort.unsorted();
            if (ordenColumna != null && !ordenColumna.isEmpty()) {
                String[] ordenColumnaSplit = ordenColumna.split(",");
                if (ordenColumnaSplit.length == 2) {
                    String campo = ordenColumnaSplit[0];
                    String direccion = ordenColumnaSplit[1];

                    Sort.Direction direction = "asc".equalsIgnoreCase(direccion) ? Sort.Direction.ASC
                            : Sort.Direction.DESC;
                    sort = Sort.by(direction, campo);
                }
            }
            Pageable pageable = PageRequest.of(page, size, sort);
            PaginatedResponseDTO<MaterialDTO> materialesPaginados;

            if (search != null && !search.trim().isEmpty()) {
                materialesPaginados = materialService.obtenerMaterialesFiltrados(pageable, search);
            } else {
                materialesPaginados = materialService.obtenerMaterialesPaginados(pageable);
            }

            return ResponseEntity.ok(materialesPaginados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-material/{id}")
    public ResponseEntity<?> obtenerMaterialPorId(@PathVariable Long id) {
        try {
            Material material = materialService.obtenerMaterialPorId(id);
            if (material == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
            }
            MaterialDTO materialDTO = new MaterialDTO(material);
            return ResponseEntity.ok(materialDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EDITAR_MATERIALES')")
    @PutMapping("/edit-material/{id}")
    public ResponseEntity<?> editarMaterial(@PathVariable Long id, @RequestBody MaterialDTO materialDTO) {
        try {
            Material materialActualizado = materialService.editarMaterial(id,
                    materialDTO);
            if (materialActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado o nombre duplicado");
            }
            return ResponseEntity.ok(new MaterialDTO(materialActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // @PreAuthorize("hasAuthority('PERMISO_ELIMINAR_MATERIALES')")
    // @DeleteMapping("/delete-material/{id}")
    // public ResponseEntity<Map<String, Object>> eliminarMaterial(@PathVariable
    // Long id) {
    // Map<String, Object> response = new HashMap<>();
    // try {
    // boolean eliminado = materialService.eliminarMaterial(id);
    // if (eliminado) {
    // response.put("message", "Material eliminado correctamente");
    // return ResponseEntity.ok(response);
    // } else {
    // response.put("message", "Material no encontrado");
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    // }
    // } catch (RuntimeException e) {
    // response.put("error", e.getMessage());
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
    // }

    // @PreAuthorize("hasAuthority('PERMISO_CREAR_MATERIALES')")
    // @PostMapping("/create-material")
    // public ResponseEntity<?> crearMaterial(@RequestBody MaterialDTO materialDTO)
    // {
    // try {
    // MaterialDTO materialCreado = materialService.crearMaterial(materialDTO);
    // return ResponseEntity.status(HttpStatus.CREATED).body(materialCreado);
    // } catch (MaterialYaExistenteException e) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    // } catch (RuntimeException e) {
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    // }
    // }
}
