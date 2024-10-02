package dssd.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Recolector;
import org.springframework.transaction.annotation.Transactional;;

@Service
public class CentroRecoleccionService {

    @Autowired
    private CentroRecoleccionRepository centroRecoleccionRepository;
    @Transactional

    public List<Recolector> getRecolectoresByCentro(Long centroId) {
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findById(centroId)
                .orElseThrow(() -> new RuntimeException("Centro de recolección no encontrado"));

        return centroRecoleccion.getRecolectores();
    }
}
