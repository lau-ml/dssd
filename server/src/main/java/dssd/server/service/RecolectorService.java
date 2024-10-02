package dssd.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecolectorService {
    @Transactional

    public String obtenerRecolectores() {
        return "Recolectores";
    }
}
