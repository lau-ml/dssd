package dssd.apiecocycle.initializer;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;
import dssd.apiecocycle.repository.DepositoGlobalRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    private final CentroDeRecepcionRepository centroDeRecepcionRepository;
    private final DepositoGlobalRepository depositoGlobalRepository;

    public DatabaseInitializer(CentroDeRecepcionRepository centroDeRecepcionRepository,
            DepositoGlobalRepository depositoGlobalRepository) {
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
        this.depositoGlobalRepository = depositoGlobalRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        long count = centroDeRecepcionRepository.count();
        if (count == 0) {
            // Centros de Recepción
            List<CentroDeRecepcion> defaultCentros = new ArrayList<>();
            defaultCentros.add(new CentroDeRecepcion("mailCentro1@ecocycle.com", "221-22224", "Calle falsa 123"));
            defaultCentros.add(new CentroDeRecepcion("mailCentro2@ecocycle.com", "221-11114", "Calle verdadera 123"));
            defaultCentros.add(new CentroDeRecepcion("mailCentro3@ecocycle.com", "221-44444", "Calle alguna 123"));
            centroDeRecepcionRepository.saveAll(defaultCentros);

            // Depósitos Globales
            List<DepositoGlobal> defaultDepositos = new ArrayList<>();
            defaultDepositos.add(new DepositoGlobal("global1@ecocycle.com", "123-4567", "Av. Siempreviva 742"));
            defaultDepositos.add(new DepositoGlobal("global2@ecocycle.com", "123-8901", "Av. Las Rosas 100"));
            defaultDepositos.add(new DepositoGlobal("global3@ecocycle.com", "987-6543", "Calle Los Álamos 333"));
            depositoGlobalRepository.saveAll(defaultDepositos);
        }

    }
}
