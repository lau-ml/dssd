package dssd.server.service;

import dssd.server.model.Zona;
import dssd.server.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZonaService {
    @Autowired
    private ZonaRepository zonaRepository;
    public List<Zona> getAll() {
        return zonaRepository.findAll();
    }
}
