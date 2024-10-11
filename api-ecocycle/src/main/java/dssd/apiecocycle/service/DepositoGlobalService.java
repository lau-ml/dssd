package dssd.apiecocycle.service;

import java.util.Set;
import java.util.List;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.model.Permiso;
import dssd.apiecocycle.repository.DepositoGlobalRepository;

@Service
public class DepositoGlobalService {

    private final DepositoGlobalRepository depositoGlobalRepository;
    // private final PasswordEncoder passwordEncoder;

    public DepositoGlobalService(DepositoGlobalRepository depositoGlobalRepository
    // ,PasswordEncoder passwordEncoder
    ) {
        this.depositoGlobalRepository = depositoGlobalRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    public DepositoGlobal newDepositoGlobal(String email, String password, String telefono, String direccion) {
        // String hashedPassword = passwordEncoder.encode(password);

        DepositoGlobal centro = new DepositoGlobal(email, password, telefono, direccion);
        return depositoGlobalRepository.save(centro);
    }

    public DepositoGlobal newDepositoGlobal(String email, String password, String telefono, String direccion,
            Permiso permiso) {
        // String hashedPassword = passwordEncoder.encode(password);

        DepositoGlobal centro = new DepositoGlobal(email, password, telefono, direccion, permiso);
        return depositoGlobalRepository.save(centro);
    }

    public DepositoGlobal newDepositoGlobal(String email, String password, String telefono, String direccion,
            Set<Permiso> permisos) {
        // String hashedPassword = passwordEncoder.encode(password);

        DepositoGlobal centro = new DepositoGlobal(email, password, telefono, direccion, permisos);
        return depositoGlobalRepository.save(centro);
    }

    public List<DepositoGlobal> getAllDepositosGlobales() {
        return depositoGlobalRepository.findAll();
    }
}