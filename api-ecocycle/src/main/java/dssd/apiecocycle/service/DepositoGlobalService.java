package dssd.apiecocycle.service;

import java.util.List;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.repository.DepositoGlobalRepository;

@Service
public class DepositoGlobalService {

    private final DepositoGlobalRepository depositoGlobalRepository;

    public DepositoGlobalService(DepositoGlobalRepository depositoGlobalRepository

    ) {
        this.depositoGlobalRepository = depositoGlobalRepository;
    }

    public DepositoGlobal newDepositoGlobal(String nombre, String email, String password, String telefono, String direccion) {
        DepositoGlobal centro = new DepositoGlobal(nombre,email, password, telefono, direccion);
        return depositoGlobalRepository.save(centro);
    }

    public List<DepositoGlobal> getAllDepositosGlobales() {
        return depositoGlobalRepository.findAll();
    }

    public DepositoGlobal getDepositoGlobalById(Long id) {
        return depositoGlobalRepository.findById(id).orElseThrow();
    }
}
