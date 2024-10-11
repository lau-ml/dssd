package dssd.apiecocycle.service;

import java.util.Set;
import java.util.List;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.Permiso;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;

@Service
public class CentroDeRecepcionService {

    private final CentroDeRecepcionRepository centroDeRecepcionRepository;
    // private final PasswordEncoder passwordEncoder;

    public CentroDeRecepcionService(CentroDeRecepcionRepository centroDeRecepcionRepository
    // ,PasswordEncoder passwordEncoder
    ) {
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    public CentroDeRecepcion newCentroDeRecepcion(String email, String password, String telefono, String direccion) {
        // String hashedPassword = passwordEncoder.encode(password);

        CentroDeRecepcion centro = new CentroDeRecepcion(email, password, telefono, direccion);
        return centroDeRecepcionRepository.save(centro);
    }

    public CentroDeRecepcion newCentroDeRecepcion(String email, String password, String telefono, String direccion,
            Permiso permiso) {
        // String hashedPassword = passwordEncoder.encode(password);

        CentroDeRecepcion centro = new CentroDeRecepcion(email, password, telefono, direccion, permiso);
        return centroDeRecepcionRepository.save(centro);
    }

    public CentroDeRecepcion newCentroDeRecepcion(String email, String password, String telefono, String direccion,
            Set<Permiso> permisos) {
        // String hashedPassword = passwordEncoder.encode(password);

        CentroDeRecepcion centro = new CentroDeRecepcion(email, password, telefono, direccion, permisos);
        return centroDeRecepcionRepository.save(centro);
    }

    public List<CentroDeRecepcion> getAllCentrosDeRecepcion() {
        return centroDeRecepcionRepository.findAll();
    }
}