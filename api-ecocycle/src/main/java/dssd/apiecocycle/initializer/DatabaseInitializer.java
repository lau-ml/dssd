package dssd.apiecocycle.initializer;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.model.Permiso;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;
import dssd.apiecocycle.repository.DepositoGlobalRepository;
import dssd.apiecocycle.repository.PermisoRepository;
import dssd.apiecocycle.service.CentroDeRecepcionService;
import dssd.apiecocycle.service.DepositoGlobalService;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    private final CentroDeRecepcionRepository centroDeRecepcionRepository;
    private final DepositoGlobalRepository depositoGlobalRepository;
    private final PermisoRepository permisoRepository;
    private final CentroDeRecepcionService centroDeRecepcionService;
    private final DepositoGlobalService depositoGlobalService;

    public DatabaseInitializer(CentroDeRecepcionRepository centroDeRecepcionRepository,
            DepositoGlobalRepository depositoGlobalRepository,
            PermisoRepository permisoRepository,
            CentroDeRecepcionService centroDeRecepcionService,
            DepositoGlobalService depositoGlobalService) {
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
        this.depositoGlobalRepository = depositoGlobalRepository;
        this.permisoRepository = permisoRepository;
        this.centroDeRecepcionService = centroDeRecepcionService;
        this.depositoGlobalService = depositoGlobalService;
    }

    @Override
    public void run(ApplicationArguments args) {
        long count = centroDeRecepcionRepository.count();
        if (count == 0) {
            try {
                // Permisos
                Permiso permisoGenerarOrden = new Permiso("GENERAR_ORDEN", "Permite generar órdenes");
                Permiso permisoConsultarOrden = new Permiso("CONSULTAR_ORDEN", "Permite consultar órdenes");
                Permiso permisoConsultarPedido = new Permiso("CONSULTAR_PEDIDO", "Permite consultar pedidos");
                Permiso permisoGenerarPedido = new Permiso("GENERAR_PEDIDO", "Permite generar pedidos");
                Permiso permisoModificarPedido = new Permiso("MODIFICAR_PEDIDO", "Permite modificar pedidos");

                permisoRepository.save(permisoGenerarOrden);
                permisoRepository.save(permisoConsultarOrden);
                permisoRepository.save(permisoConsultarPedido);
                permisoRepository.save(permisoGenerarPedido);
                permisoRepository.save(permisoModificarPedido);

                // Asignar permisos a Centros de Recepción
                Set<Permiso> permisosCentroRecepcion = new HashSet<>();
                permisosCentroRecepcion.add(permisoGenerarOrden);
                permisosCentroRecepcion.add(permisoConsultarOrden);
                permisosCentroRecepcion.add(permisoConsultarPedido);

                // Centros de Recepción
                List<CentroDeRecepcion> defaultCentros = new ArrayList<>();
                defaultCentros.add(
                        centroDeRecepcionService.newCentroDeRecepcion("mailCentro1@ecocycle.com", "123456", "221-22224",
                                "Calle falsa 123"));
                defaultCentros.add(
                        centroDeRecepcionService.newCentroDeRecepcion("mailCentro2@ecocycle.com", "123456", "221-11114",
                                "Calle verdadera 123"));
                defaultCentros.add(
                        centroDeRecepcionService.newCentroDeRecepcion("mailCentro3@ecocycle.com", "123456", "221-44444",
                                "Calle alguna 123"));

                // Me tira error por los permisos, despues se vera

                // List<CentroDeRecepcion> defaultCentros = new ArrayList<>();
                // defaultCentros.add(
                // centroDeRecepcionService.newCentroDeRecepcion("mailCentro1@ecocycle.com",
                // "123456", "221-22224",
                // "Calle falsa 123", permisosCentroRecepcion));
                // defaultCentros.add(
                // centroDeRecepcionService.newCentroDeRecepcion("mailCentro2@ecocycle.com",
                // "123456", "221-11114",
                // "Calle verdadera 123", permisosCentroRecepcion));
                // defaultCentros.add(
                // centroDeRecepcionService.newCentroDeRecepcion("mailCentro3@ecocycle.com",
                // "123456", "221-44444",
                // "Calle alguna 123", permisosCentroRecepcion));
                centroDeRecepcionRepository.saveAll(defaultCentros);

                // Asignar permisos a Depósitos Globales
                Set<Permiso> permisosDepositoGlobal = new HashSet<>();
                permisosDepositoGlobal.add(permisoGenerarPedido);
                permisosDepositoGlobal.add(permisoConsultarPedido);
                permisosDepositoGlobal.add(permisoModificarPedido);

                // Depósitos Globales
                List<DepositoGlobal> defaultDepositos = new ArrayList<>();
                defaultDepositos
                        .add(depositoGlobalService.newDepositoGlobal("global1@ecocycle.com", "123456", "123-4567",
                                "Av. Siempreviva 742"));
                defaultDepositos
                        .add(depositoGlobalService.newDepositoGlobal("global2@ecocycle.com", "123456", "123-8901",
                                "Av. Las Rosas 100"));
                defaultDepositos
                        .add(depositoGlobalService.newDepositoGlobal("global3@ecocycle.com", "123456", "987-6543",
                                "Calle Los Álamos 333"));

                // Me tira error por los permisos, despues se vera

                // List<DepositoGlobal> defaultDepositos = new ArrayList<>();
                // defaultDepositos
                // .add(depositoGlobalService.newDepositoGlobal("global1@ecocycle.com",
                // "123456", "123-4567",
                // "Av. Siempreviva 742",
                // permisosDepositoGlobal));
                // defaultDepositos
                // .add(depositoGlobalService.newDepositoGlobal("global2@ecocycle.com",
                // "123456", "123-8901",
                // "Av. Las Rosas 100",
                // permisosDepositoGlobal));
                // defaultDepositos
                // .add(depositoGlobalService.newDepositoGlobal("global3@ecocycle.com",
                // "123456", "987-6543",
                // "Calle Los Álamos 333", permisosDepositoGlobal));
                depositoGlobalRepository.saveAll(defaultDepositos);
            } catch (Exception e) {
                System.err.println("Error al inicializar la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
