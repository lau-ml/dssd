package dssd.server.controller;

import dssd.server.DTO.StockMaterialDTO;
import dssd.server.model.StockMaterial;
import dssd.server.service.StockMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-materiales")
public class StockMaterialController {

    @Autowired
    private StockMaterialService stockMaterialService;

    @PreAuthorize("hasAuthority('PERMISO_AGREGAR_STOCK')")
    @PutMapping("/agregar/{centroRecoleccionId}/{materialId}/{cantidad}")
    public ResponseEntity<?> agregarStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId,
            @PathVariable Integer cantidad) {
        StockMaterial stockMaterial = this.stockMaterialService.agregarStockMaterial(centroRecoleccionId, materialId, cantidad);
        return new ResponseEntity<>(StockMaterialDTO.builder().materialId(stockMaterial.getMaterial().getId()).cantidad(stockMaterial.getCantidad()).centroRecoleccionId(stockMaterial.getCentroRecoleccion().getId()).id(stockMaterial.getId()), HttpStatus.OK);

    }

    @PutMapping("/quitar/{centroRecoleccionId}/{materialId}/{cantidad}")
    @PreAuthorize("hasAuthority('PERMISO_QUITAR_STOCK')")

    public ResponseEntity<?> quitarStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId,
            @PathVariable Integer cantidad) {
        StockMaterial stockMaterial = this.stockMaterialService.quitarStockMaterial(centroRecoleccionId, materialId, cantidad);
        return new ResponseEntity<>(StockMaterialDTO.builder().materialId(stockMaterial.getMaterial().getId()).cantidad(stockMaterial.getCantidad()).centroRecoleccionId(stockMaterial.getCentroRecoleccion().getId()).id(stockMaterial.getId()), HttpStatus.OK);
    }

    @GetMapping("/{centroRecoleccionId}/{materialId}")
    @PreAuthorize("hasAuthority('PERMISO_VER_STOCK')")
    public ResponseEntity<?> getStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId) {
        StockMaterial stockMaterial = this.stockMaterialService.getStockMaterial(centroRecoleccionId, materialId);
        return new ResponseEntity<>(StockMaterialDTO.builder().materialId(stockMaterial.getMaterial().getId()).cantidad(stockMaterial.getCantidad()).centroRecoleccionId(stockMaterial.getCentroRecoleccion().getId()).id(stockMaterial.getId()), HttpStatus.OK);
    }
}
