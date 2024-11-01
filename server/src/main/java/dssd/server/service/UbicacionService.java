package dssd.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.server.model.Ubicacion;
import dssd.server.repository.UbicacionRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UbicacionService {
    @Autowired
    private UbicacionRepository ubicacionRepository;
    @Transactional

    public List<Ubicacion> obtenerUbicaciones() {
        return ubicacionRepository.findAll();
    }
}
