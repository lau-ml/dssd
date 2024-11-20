package dssd.server.controller;

import dssd.server.DTO.StockMaterialDTO;
import dssd.server.model.StockMaterial;
import dssd.server.service.StockMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-materiales")
public class StockMaterialController {

    @Autowired
    private StockMaterialService stockMaterialService;

    @PutMapping("/agregar/{centroRecoleccionId}/{materialId}/{cantidad}")
    public ResponseEntity<?> agregarStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId,
            @PathVariable Integer cantidad) {
        StockMaterial stockMaterial = this.stockMaterialService.agregarStockMaterial(centroRecoleccionId, materialId, cantidad);
        return new ResponseEntity<>(StockMaterialDTO.builder().materialId(stockMaterial.getMaterial().getId()).cantidad(stockMaterial.getCantidad()).centroRecoleccionId(stockMaterial.getCentroRecoleccion().getId()).id(stockMaterial.getId()), HttpStatus.OK);

    }

    @PutMapping("/quitar/{centroRecoleccionId}/{materialId}/{cantidad}")
    public ResponseEntity<?> quitarStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId,
            @PathVariable Integer cantidad) {
        StockMaterial stockMaterial = this.stockMaterialService.quitarStockMaterial(centroRecoleccionId, materialId, cantidad);
        return new ResponseEntity<>(StockMaterialDTO.builder().materialId(stockMaterial.getMaterial().getId()).cantidad(stockMaterial.getCantidad()).centroRecoleccionId(stockMaterial.getCentroRecoleccion().getId()).id(stockMaterial.getId()), HttpStatus.OK);
    }

}
