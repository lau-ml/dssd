package dssd.server.service;

import dssd.server.exception.StockError;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import dssd.server.model.CantidadMaterial;
import dssd.server.repository.CantidadMaterialRepository;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockMaterialService {

    @Autowired
    private CantidadMaterialRepository stockMaterialRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CentroRecoleccionRepository centroRecoleccionRepository;

    @Transactional
    public CantidadMaterial agregarStockMaterial(Long centroRecoleccionId, Long materialId, Integer cantidad) {
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findById(centroRecoleccionId).orElseThrow(() -> new RuntimeException("Centro de recolección no encontrado."));
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new RuntimeException("Material no encontrado."));
        CantidadMaterial stockCentroMaterial= stockMaterialRepository.findByCentroRecoleccionAndMaterial(centroRecoleccion, material).orElseThrow(()->new StockError("Stock no encontrado."));
        return stockMaterialRepository.save(stockCentroMaterial);
    }

    @Transactional
    public CantidadMaterial quitarStockMaterial(Long centroRecoleccionId, Long materialId, Integer cantidad) {
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findById(centroRecoleccionId).orElseThrow(() -> new RuntimeException("Centro de recolección no encontrado."));
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new RuntimeException("Material no encontrado."));
        CantidadMaterial stockCentroMaterial= stockMaterialRepository.findByCentroRecoleccionAndMaterial(centroRecoleccion, material).orElseThrow( ()->new StockError("Stock no encontrado."));
        if (cantidad > stockCentroMaterial.getCantidad()) {
            throw new RuntimeException("No hay suficiente stock.");
        }
        stockCentroMaterial.setCantidad(stockCentroMaterial.getCantidad() - cantidad);
        return stockMaterialRepository.save(stockCentroMaterial);
    }


    @Transactional
    public CantidadMaterial getStockMaterial(Long centroRecoleccionId, Long materialId) {
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findById(centroRecoleccionId).orElseThrow(() -> new RuntimeException("Centro de recolección no encontrado."));
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new RuntimeException("Material no encontrado."));
        return stockMaterialRepository.findByCentroRecoleccionAndMaterial(centroRecoleccion, material).orElseThrow(()->new StockError("Stock no encontrado."));
    }
}
