package dssd.apiecocycle.service;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;
import dssd.apiecocycle.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CentroDeRecepcionService {

    @Autowired
    private CentroDeRecepcionRepository centroDeRecepcionRepository;
    @Autowired
    private RolRepository rolRepository;

    public CentroDeRecepcionService(CentroDeRecepcionRepository centroDeRecepcionRepository
    ) {
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
    }

    @Transactional
    public CentroDeRecepcion newCentroDeRecepcion(String nombre, String email, String password, String telefono, String direccion) {
        CentroDeRecepcion centro = new CentroDeRecepcion(nombre, email, password, telefono, direccion, rolRepository.findByNombre("ROLE_CENTRO_RECEPCION").orElseThrow());
        return centroDeRecepcionRepository.save(centro);
    }

    @Transactional

    public Page<CentroDeRecepcion> getAllCentrosDeRecepcion(String email, String telefono, String direccion, int page, int pageSize) {
        return centroDeRecepcionRepository.findAll(
                email, telefono, direccion, PageRequest.of(page, pageSize)
        );
    }

    @Transactional(readOnly = true)

    public CentroDeRecepcion getCentroDeRecepcionById(Long id) {
        return centroDeRecepcionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Centro de recepción no encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<CentroDeRecepcion> getCentroById(Long centroDeRecepcionId) {
        return centroDeRecepcionRepository.findById(centroDeRecepcionId);
    }
}
