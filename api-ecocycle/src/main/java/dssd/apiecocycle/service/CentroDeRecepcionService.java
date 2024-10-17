package dssd.apiecocycle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;

@Service
public class CentroDeRecepcionService {

    @Autowired
    private CentroDeRecepcionRepository centroDeRecepcionRepository;
    // private final PasswordEncoder passwordEncoder;

    public CentroDeRecepcionService(CentroDeRecepcionRepository centroDeRecepcionRepository
    // ,PasswordEncoder passwordEncoder
    ) {
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    public CentroDeRecepcion newCentroDeRecepcion(String nombre, String email, String password, String telefono, String direccion) {
        // String hashedPassword = passwordEncoder.encode(password);

        CentroDeRecepcion centro = new CentroDeRecepcion(nombre ,email, password, telefono, direccion);
        return centroDeRecepcionRepository.save(centro);
    }

    public List<CentroDeRecepcion> getAllCentrosDeRecepcion() {
        return centroDeRecepcionRepository.findAll();
    }

    public CentroDeRecepcion getCentroDeRecepcionById(Long id) {
        return centroDeRecepcionRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Centro de recepción no encontrado"));
    }

    public Optional<CentroDeRecepcion> getCentroById(Long centroDeRecepcionId) {
        return centroDeRecepcionRepository.findById(centroDeRecepcionId);
    }
}
