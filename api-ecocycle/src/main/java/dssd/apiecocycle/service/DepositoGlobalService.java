package dssd.apiecocycle.service;

import java.util.List;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.repository.DepositoGlobalRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositoGlobalService {

    private final DepositoGlobalRepository depositoGlobalRepository;

    public DepositoGlobalService(DepositoGlobalRepository depositoGlobalRepository

    ) {
        this.depositoGlobalRepository = depositoGlobalRepository;
    }

    @Transactional
    public DepositoGlobal newDepositoGlobal(String nombre, String email, String password, String telefono, String direccion) {
        DepositoGlobal centro = new DepositoGlobal(nombre,email, password, telefono, direccion);
        return depositoGlobalRepository.save(centro);
    }

    @Transactional(readOnly = true)
    public Page<DepositoGlobal> getAllDepositosGlobales(String email, String telefono, String direccion, int i, int pageSize) {
        return depositoGlobalRepository.findAll(
                email, telefono, direccion, PageRequest.of(i, pageSize)
        );
    }

    @Transactional(readOnly = true)
    public DepositoGlobal getDepositoGlobalById(Long id) {
        return depositoGlobalRepository.findById(id).orElseThrow();
    }
}
