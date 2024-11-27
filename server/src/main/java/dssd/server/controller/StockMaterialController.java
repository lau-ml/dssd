package dssd.server.controller;

import dssd.server.DTO.StockMaterialDTO;
import dssd.server.exception.StockError;
import dssd.server.model.CantidadMaterial;
import dssd.server.response.ErrorResponse;
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
        try {
            CantidadMaterial cantidadMaterial = this.stockMaterialService.agregarStockMaterial(centroRecoleccionId, materialId, cantidad);
            return new ResponseEntity<>(StockMaterialDTO.builder().materialId(cantidadMaterial.getMaterial().getId()).cantidad(cantidadMaterial.getCantidad()).centroRecoleccionId(cantidadMaterial.getCentroRecoleccion().getId()).id(cantidadMaterial.getId()).build(), HttpStatus.OK);
        }
        catch (StockError e) {
            return new ResponseEntity<>(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/quitar/{centroRecoleccionId}/{materialId}/{cantidad}")
    @PreAuthorize("hasAuthority('PERMISO_QUITAR_STOCK')")

    public ResponseEntity<?> quitarStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId,
            @PathVariable Integer cantidad) {
        try {
            CantidadMaterial cantidadMaterial = this.stockMaterialService.quitarStockMaterial(centroRecoleccionId, materialId, cantidad);
            return new ResponseEntity<>(StockMaterialDTO.builder().materialId(cantidadMaterial.getMaterial().getId()).cantidad(cantidadMaterial.getCantidad()).centroRecoleccionId(cantidadMaterial.getCentroRecoleccion().getId()).id(cantidadMaterial.getId()).build(), HttpStatus.OK);
        } catch (StockError e) {
            return new ResponseEntity<>(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{centroRecoleccionId}/{materialId}")
    @PreAuthorize("hasAuthority('PERMISO_VER_STOCK')")
    public ResponseEntity<?> getStockMaterial(
            @PathVariable Long centroRecoleccionId,
            @PathVariable Long materialId) {
        try {
            CantidadMaterial cantidadMaterial = this.stockMaterialService.getStockMaterial(centroRecoleccionId, materialId);
            return new ResponseEntity<>(StockMaterialDTO.builder().materialId(cantidadMaterial.getMaterial().getId()).cantidad(cantidadMaterial.getCantidad()).centroRecoleccionId(cantidadMaterial.getCentroRecoleccion().getId()).id(cantidadMaterial.getId()).build(), HttpStatus.OK);
        } catch (StockError e) {
            return new ResponseEntity<>(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_PRIMERA_VEZ')")
    @GetMapping("/primera-vez/{centroRecoleccionId}/{materialId}")
    public ResponseEntity<?> primeraVez(@PathVariable Long centroRecoleccionId, @PathVariable Long materialId) {
        try {
            CantidadMaterial cantidadMaterial = this.stockMaterialService.primeraVez(centroRecoleccionId, materialId);
            return new ResponseEntity<>(StockMaterialDTO.builder().materialId(cantidadMaterial.getMaterial().getId()).cantidad(cantidadMaterial.getCantidad()).centroRecoleccionId(cantidadMaterial.getCentroRecoleccion().getId()).id(cantidadMaterial.getId()).build(), HttpStatus.OK);
        }catch (StockError e) {
            return new ResponseEntity<>(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EDITAR_PRIMERA_VEZ')")
    @PutMapping("/primera-vez/{centroRecoleccionId}/{materialId}")
    public ResponseEntity<?> primeraVezEditar(@PathVariable Long centroRecoleccionId, @PathVariable Long materialId) {
        try {
            CantidadMaterial cantidadMaterial = this.stockMaterialService.setPrimeraVez(centroRecoleccionId, materialId);
            return new ResponseEntity<>(StockMaterialDTO.builder().materialId(cantidadMaterial.getMaterial().getId()).cantidad(cantidadMaterial.getCantidad()).centroRecoleccionId(cantidadMaterial.getCentroRecoleccion().getId()).id(cantidadMaterial.getId()).build(), HttpStatus.OK);
        }catch (StockError e) {
            return new ResponseEntity<>(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }
}

