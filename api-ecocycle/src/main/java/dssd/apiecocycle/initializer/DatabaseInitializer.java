package dssd.apiecocycle.initializer;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Permiso;
import dssd.apiecocycle.model.Rol;
import dssd.apiecocycle.repository.CentroDeRecepcionRepository;
import dssd.apiecocycle.repository.DepositoGlobalRepository;
import dssd.apiecocycle.repository.MaterialRepository;
import dssd.apiecocycle.repository.PermisoRepository;
import dssd.apiecocycle.repository.RolRepository;
import dssd.apiecocycle.service.CentroDeRecepcionService;
import dssd.apiecocycle.service.DepositoGlobalService;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final MaterialRepository materialRepository;
    private final CentroDeRecepcionRepository centroDeRecepcionRepository;
    private final DepositoGlobalRepository depositoGlobalRepository;
    private final PermisoRepository permisoRepository;
    private final CentroDeRecepcionService centroDeRecepcionService;
    private final DepositoGlobalService depositoGlobalService;
    private final RolRepository rolRepository;

    public DatabaseInitializer(MaterialRepository materialRepository,
            CentroDeRecepcionRepository centroDeRecepcionRepository,
            DepositoGlobalRepository depositoGlobalRepository,
            PermisoRepository permisoRepository,
            CentroDeRecepcionService centroDeRecepcionService,
            DepositoGlobalService depositoGlobalService,
            RolRepository rolRepository) {
        this.materialRepository = materialRepository;
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
        this.depositoGlobalRepository = depositoGlobalRepository;
        this.permisoRepository = permisoRepository;
        this.centroDeRecepcionService = centroDeRecepcionService;
        this.depositoGlobalService = depositoGlobalService;
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        long count = centroDeRecepcionRepository.count();
        if (count == 0) {
            try {
                List<Material> defaultMaterials = new ArrayList<>();
                defaultMaterials.add(new Material("Papel",
                        "Material reciclable derivado de productos como periódicos, revistas, y documentos impresos."));
                defaultMaterials.add(new Material("Plástico PET",
                        "Comúnmente usado en botellas de bebidas, es un plástico transparente y ligero que se recicla para fabricar nuevas botellas o fibras textiles."));
                defaultMaterials.add(new Material("Vidrio",
                        "Incluye botellas y frascos. El vidrio reciclado puede reutilizarse indefinidamente sin pérdida de calidad."));
                defaultMaterials.add(new Material("Aluminio",
                        "Latas de bebidas y alimentos hechas de aluminio, reciclable casi en su totalidad para fabricar nuevas latas y productos metálicos."));
                defaultMaterials.add(new Material("Cartón",
                        "Material reciclable utilizado para embalajes y cajas. Al reciclarse, se utiliza para fabricar nuevo cartón o papel reciclado."));
                defaultMaterials.add(new Material("Plástico HDPE",
                        "Material reciclable encontrado en envases de productos de limpieza y cosméticos. Se utiliza para fabricar tuberías, botellas y más."));
                defaultMaterials.add(new Material("Chatarra metálica",
                        "Restos de metales como hierro, acero o cobre, provenientes de productos electrónicos, electrodomésticos y vehículos. Se reutiliza en la industria metalúrgica."));

                materialRepository.saveAll(defaultMaterials);

                rolRepository.save(new Rol("ROLE_CENTER", "Centro de recepcion"));
                rolRepository.save(new Rol("ROLE_DEPOSIT", "Deposito global"));

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

                Rol rolCenter = rolRepository.findByNombre("ROLE_CENTER").get();
                Rol rolDeposit = rolRepository.findByNombre("ROLE_DEPOSIT").get();

                // Asignar permisos al rolCenter
                rolCenter.getPermisos().add(permisoGenerarOrden);
                rolCenter.getPermisos().add(permisoConsultarOrden);
                rolCenter.getPermisos().add(permisoConsultarPedido);

                // Asignar permisos al rolDeposit
                rolDeposit.getPermisos().add(permisoConsultarPedido);
                rolDeposit.getPermisos().add(permisoGenerarPedido);
                rolDeposit.getPermisos().add(permisoModificarPedido);

                // Guardar los roles actualizados en la base de datos
                rolRepository.save(rolCenter);
                rolRepository.save(rolDeposit);

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

                // Asignar el rol ROLE_CENTER a cada centro
                for (CentroDeRecepcion centro : defaultCentros) {
                    centro.setRol(rolCenter);
                    centroDeRecepcionRepository.save(centro);
                }

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

                // Asignar el rol ROLE_CENTER a cada centro
                for (DepositoGlobal centro : defaultDepositos) {
                    centro.setRol(rolDeposit);
                    depositoGlobalRepository.save(centro);
                }

                depositoGlobalRepository.saveAll(defaultDepositos);
            } catch (Exception e) {
                System.err.println("Error al inicializar la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
