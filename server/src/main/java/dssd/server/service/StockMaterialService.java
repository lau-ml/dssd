package dssd.server.service;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import dssd.server.model.StockMaterial;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.repository.StockMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockMaterialService {

    @Autowired
    private StockMaterialRepository stockMaterialRepository;

    private MaterialRepository materialRepository;

    @Autowired
    private CentroRecoleccionRepository centroRecoleccionRepository;

    @Transactional
    public StockMaterial agregarStockMaterial(Long centroRecoleccionId, Long materialId, Integer cantidad) {
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findById(centroRecoleccionId).orElseThrow(() -> new RuntimeException("Centro de recolección no encontrado."));
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new RuntimeException("Material no encontrado."));
        StockMaterial stockCentroMaterial= stockMaterialRepository.findByCentroRecoleccionAndMaterial(centroRecoleccion, material);
        stockCentroMaterial.setCantidad(stockCentroMaterial.getCantidad() + cantidad);
        return stockMaterialRepository.save(stockCentroMaterial);
    }

    @Transactional
    public StockMaterial quitarStockMaterial(Long centroRecoleccionId, Long materialId, Integer cantidad) {
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findById(centroRecoleccionId).orElseThrow(() -> new RuntimeException("Centro de recolección no encontrado."));
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new RuntimeException("Material no encontrado."));
        StockMaterial stockCentroMaterial= stockMaterialRepository.findByCentroRecoleccionAndMaterial(centroRecoleccion, material);
        stockCentroMaterial.setCantidad(stockCentroMaterial.getCantidad() - cantidad);
        return stockMaterialRepository.save(stockCentroMaterial);
    }
}
