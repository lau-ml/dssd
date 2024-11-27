package dssd.apiecocycle.initializer;

import dssd.apiecocycle.model.*;
import dssd.apiecocycle.repository.*;
import dssd.apiecocycle.service.CentroDeRecepcionService;
import dssd.apiecocycle.service.DepositoGlobalService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;

    private final MaterialRepository materialRepository;
    private final CentroDeRecepcionRepository centroDeRecepcionRepository;
    private final DepositoGlobalRepository depositoGlobalRepository;
    private final PermisoRepository permisoRepository;
    private final CentroDeRecepcionService centroDeRecepcionService;
    private final DepositoGlobalService depositoGlobalService;
    private final RolRepository rolRepository;
    private final PedidoRepository pedidoRepository;

    private final OrdenRepository ordenRepository;

    public DatabaseInitializer(MaterialRepository materialRepository,
                               CentroDeRecepcionRepository centroDeRecepcionRepository,
                               DepositoGlobalRepository depositoGlobalRepository,
                               PermisoRepository permisoRepository,
                               CentroDeRecepcionService centroDeRecepcionService,
                               DepositoGlobalService depositoGlobalService,
                               RolRepository rolRepository,
                               PedidoRepository pedidoRepository,
                               PasswordEncoder passwordEncoder,
                               OrdenRepository ordenRepository) {
        this.materialRepository = materialRepository;
        this.centroDeRecepcionRepository = centroDeRecepcionRepository;
        this.depositoGlobalRepository = depositoGlobalRepository;
        this.permisoRepository = permisoRepository;
        this.centroDeRecepcionService = centroDeRecepcionService;
        this.depositoGlobalService = depositoGlobalService;
        this.rolRepository = rolRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
        this.ordenRepository = ordenRepository;
    }

    @Override
    @Transactional
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
                rolRepository.save(new Rol("ROLE_CENTRO_RECEPCION", "Centro de recepcion"));
                rolRepository.save(new Rol("ROLE_DEPOSITO_GLOBAL", "Deposito global"));

                // Permisos
                Permiso permisoGenerarOrden = new Permiso("GENERAR_ORDEN", "Permite generar órdenes");
                Permiso permisoConsultarOrdenProveedores = new Permiso("CONSULTAR_ORDEN_PROVEEDOR",
                        "Permite consultar órdenes");
                Permiso permisoConsultarOrdenDeposito = new Permiso("CONSULTAR_ORDEN_DEPOSITO", "Permite consultar órdenes");

                Permiso permisoConsultarPedidoPropio = new Permiso("CONSULTAR_PEDIDO_PROPIO",
                        "Permite consultar pedidos");
                Permiso permisoConsultarTodosPedidos = new Permiso("CONSULTAR_TODOS_PEDIDOS",
                        "Permite consultar todos los pedidos");
                Permiso permisoGenerarPedido = new Permiso("GENERAR_PEDIDO", "Permite generar pedidos");
                Permiso permisoModificarPedido = new Permiso("MODIFICAR_PEDIDO",
                        "Permite modificar pedidos");
                Permiso obtenerCentrosDeRecepcion = new Permiso("OBTENER_CENTROS_DE_RECEPCION",
                        "Permite obtener todos los centros de recepción");
                Permiso obtenerDepositosGlobales = new Permiso("OBTENER_DEPOSITOS_GLOBALES",
                        "Permite obtener todos los depósitos globales");
                Permiso obtenerProveedoresPorMaterial = new Permiso("OBTENER_PROVEEDORES_POR_MATERIAL",
                        "Permite obtener los proveedores de un material específico");
                Permiso obtenerMateriales = new Permiso("OBTENER_MATERIALES",
                        "Permite obtener todos los materiales reciclables");
                Permiso entregarOrden = new Permiso("ENTREGAR_ORDEN", "Permite entregar una orden");
                Permiso rechazarOrden = new Permiso("RECHAZAR_ORDEN", "Permite rechazar una orden");
                Permiso permisoConsultarOrdenesPedido = new Permiso("CONSULTAR_ORDENES_PEDIDO",
                        "Permite consultar órdenes");
                Permiso aceptarOrden = new Permiso("ACEPTAR_ORDEN", "Permite aceptar una orden");
                Permiso inscribirProveedor = new Permiso("INSCRIBIR_PROVEEDOR",
                        "Permite inscribir un proveedor");
                Permiso permisoPrepararOrden = new Permiso("PREPARAR_ORDEN", "Permite preparar una orden");
                Permiso permisoEnviarOrden = new Permiso("ENVIAR_ORDEN", "Permite enviar una orden");
                permisoRepository.save(obtenerCentrosDeRecepcion);
                permisoRepository.save(obtenerDepositosGlobales);
                permisoRepository.save(obtenerProveedoresPorMaterial);
                permisoRepository.save(obtenerMateriales);
                permisoRepository.save(entregarOrden);
                permisoRepository.save(rechazarOrden);
                permisoRepository.save(permisoConsultarOrdenesPedido);
                permisoRepository.save(permisoGenerarOrden);
                permisoRepository.save(permisoConsultarOrdenDeposito);
                permisoRepository.save(permisoConsultarPedidoPropio);
                permisoRepository.save(permisoGenerarPedido);
                permisoRepository.save(permisoModificarPedido);
                permisoRepository.save(aceptarOrden);
                permisoRepository.save(inscribirProveedor);
                permisoRepository.save(permisoConsultarTodosPedidos);
                permisoRepository.save(permisoConsultarOrdenProveedores);
                permisoRepository.save(permisoPrepararOrden);
                permisoRepository.save(permisoEnviarOrden);
                Rol rolCenter = rolRepository.findByNombre("ROLE_CENTRO_RECEPCION").get();
                Rol rolDeposit = rolRepository.findByNombre("ROLE_DEPOSITO_GLOBAL").get();

                // Asignar permisos al rolCenter
                rolCenter.getPermisos().add(permisoGenerarOrden);
                rolCenter.getPermisos().add(permisoConsultarOrdenProveedores);
                rolCenter.getPermisos().add(obtenerMateriales);
                rolCenter.getPermisos().add(inscribirProveedor);
                rolCenter.getPermisos().add(permisoConsultarTodosPedidos);
                rolCenter.getPermisos().add(permisoPrepararOrden);
                rolCenter.getPermisos().add(permisoEnviarOrden);
                rolCenter.getPermisos().add(obtenerDepositosGlobales);
                // Asignar permisos al rolDeposit
                rolDeposit.getPermisos().add(permisoConsultarPedidoPropio);
                rolDeposit.getPermisos().add(permisoGenerarPedido);
                rolDeposit.getPermisos().add(permisoModificarPedido);
                rolDeposit.getPermisos().add(obtenerCentrosDeRecepcion);
                rolDeposit.getPermisos().add(obtenerProveedoresPorMaterial);
                rolDeposit.getPermisos().add(obtenerMateriales);
                rolDeposit.getPermisos().add(entregarOrden);
                rolDeposit.getPermisos().add(rechazarOrden);
                rolDeposit.getPermisos().add(aceptarOrden);
                rolDeposit.getPermisos().add(permisoConsultarOrdenesPedido);
                rolDeposit.getPermisos().add(permisoConsultarOrdenDeposito);
                // Guardar los roles actualizados en la base de datos
                rolRepository.save(rolCenter);
                rolRepository.save(rolDeposit);

                // Centros de Recepción
                List<CentroDeRecepcion> defaultCentros = new ArrayList<>();
                defaultCentros.add(
                        centroDeRecepcionService.newCentroDeRecepcion("EcoPunto Verde",
                                "mailcentro1@ecocycle.com", passwordEncoder.encode("123456"), "2211234567",
                                "Calle falsa 123"));
                defaultCentros.add(
                        centroDeRecepcionService.newCentroDeRecepcion("Recolección Sostenible",
                                "mailcentro2@ecocycle.com", passwordEncoder.encode("123456"), "2217654321",
                                "Calle verdadera 123"));
                defaultCentros.add(
                        centroDeRecepcionService.newCentroDeRecepcion("Centro EcoAmigo",
                                "mailcentro3@ecocycle.com", passwordEncoder.encode("123456"), "2211111111",
                                "Calle alguna 123"));

                // Asignar el rol ROLE_CENTRO_RECEPCION a cada centro
                for (CentroDeRecepcion centro : defaultCentros) {
                    centro.setRol(rolCenter);
                    centroDeRecepcionRepository.save(centro);
                }

                // Depósitos Globales
                List<DepositoGlobal> defaultDepositos = new ArrayList<>();
                defaultDepositos
                        .add(depositoGlobalService.newDepositoGlobal("global1", "global1@ecocycle.com",
                                passwordEncoder.encode("123456"), "2212222222",
                                "Av. Siempreviva 742"));
                defaultDepositos
                        .add(depositoGlobalService.newDepositoGlobal("global2", "global2@ecocycle.com",
                                passwordEncoder.encode("123456"), "2213333333",
                                "Av. Las Rosas 100"));
                defaultDepositos
                        .add(depositoGlobalService.newDepositoGlobal("global3", "global3@ecocycle.com",
                                passwordEncoder.encode("123456"), "2214444444",
                                "Calle Los Álamos 333"));

                // Asignar el rol ROLE_CENTRO_RECEPCION a cada centro
                for (DepositoGlobal centro : defaultDepositos) {
                    centro.setRol(rolDeposit);
                    depositoGlobalRepository.save(centro);
                }

                depositoGlobalRepository.saveAll(defaultDepositos);

                // Recuperar algunos materiales
                Material papel = materialRepository.findByNombre("Papel").orElseThrow();
                Material plasticoPET = materialRepository.findByNombre("Plástico PET").orElseThrow();
                Material vidrio = materialRepository.findByNombre("Vidrio").orElseThrow();

                // Recuperar algunos depósitos globales
                DepositoGlobal deposito1 = depositoGlobalRepository.findByEmail("global1@ecocycle.com")
                        .orElseThrow();
                DepositoGlobal deposito2 = depositoGlobalRepository.findByEmail("global2@ecocycle.com")
                        .orElseThrow();

                // Crear pedidos
                List<Pedido> defaultPedidos = new ArrayList<>();
                defaultPedidos.add(new Pedido(papel, 100L, deposito1));
                defaultPedidos.add(new Pedido(plasticoPET, 200L, deposito2));
                defaultPedidos.add(new Pedido(vidrio, 150L, deposito1));
                defaultPedidos.add(new Pedido(papel, 79L, deposito2));
                defaultPedidos.add(new Pedido(plasticoPET, 100L, deposito1));
                defaultPedidos.forEach(pedido ->
                {
                    pedido.setFecha(java.time.LocalDate.of(2024, 10, 25));
                    pedido.setLastUpdate(java.time.LocalDate.of(2024, 10, 25));
                });
                pedidoRepository.saveAll(defaultPedidos);


                Orden orden = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(0))
                        .material(papel)
                        .estado(EstadoOrden.PENDIENTE)
                        .cantidad(100L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25))
                        .pedido(defaultPedidos.get(0))
                        .build();
                Orden orden2 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(1))
                        .material(plasticoPET)
                        .estado(EstadoOrden.PENDIENTE)
                        .cantidad(50L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25))
                        .pedido(defaultPedidos.get(1))
                        .build();
                Orden orden3 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(1))
                        .material(vidrio)
                        .estado(EstadoOrden.PENDIENTE)
                        .cantidad(100L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(2))
                        .build();
                Orden orden4 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(0))
                        .material(vidrio)
                        .estado(EstadoOrden.PENDIENTE)
                        .cantidad(100L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(2))
                        .build();
                Orden orden5 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(2))
                        .material(plasticoPET)
                        .estado(EstadoOrden.RECHAZADA)
                        .cantidad(100L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(1))
                        .build();
                Orden orden6 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(2))
                        .material(papel)
                        .estado(EstadoOrden.ACEPTADA)
                        .cantidad(100L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(0))
                        .cantidadAceptada(20L)
                        .build();

                Orden orden7 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(0))
                        .material(plasticoPET)
                        .estado(EstadoOrden.PREPARANDO)
                        .cantidad(20L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(1))
                        .cantidadAceptada(20L)
                        .build();

                Orden orden8 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(1))
                        .material(papel)
                        .estado(EstadoOrden.PREPARADA)
                        .cantidad(20L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(0))
                        .cantidadAceptada(20L)
                        .build();

                Orden orden9 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(2))
                        .material(vidrio)
                        .estado(EstadoOrden.ENVIADA)
                        .cantidad(20L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(2))
                        .cantidadAceptada(20L)
                        .build();

                Orden orden10 = Orden.builder()
                        .centroDeRecepcion(defaultCentros.get(0))
                        .material(papel)
                        .estado(EstadoOrden.ENTREGADA)
                        .cantidad(20L)
                        .fecha(java.time.LocalDate.of(2024, 10, 25))
                        .lastUpdate(java.time.LocalDate.of(2024, 10, 25)).pedido(defaultPedidos.get(3))
                        .cantidadAceptada(20L)
                        .build();

                ordenRepository.save(orden);
                ordenRepository.save(orden2);
                ordenRepository.save(orden3);
                ordenRepository.save(orden4);
                ordenRepository.save(orden5);
                ordenRepository.save(orden6);
                ordenRepository.save(orden7);
                ordenRepository.save(orden8);
                ordenRepository.save(orden9);
                ordenRepository.save(orden10);
                defaultPedidos.get(0).addCantidadAbastecida(orden6.getCantidadAceptada());
                defaultPedidos.get(1).addCantidadAbastecida(orden7.getCantidadAceptada());
                defaultPedidos.get(0).addCantidadAbastecida(orden8.getCantidadAceptada());
                defaultPedidos.get(2).addCantidadAbastecida(orden9.getCantidadAceptada());
                defaultPedidos.get(3).addCantidadAbastecida(orden10.getCantidadAceptada());
                pedidoRepository.save(defaultPedidos.get(0));
                pedidoRepository.save(defaultPedidos.get(1));
                pedidoRepository.save(defaultPedidos.get(2));
                pedidoRepository.save(defaultPedidos.get(3));
                papel.addProveedor(defaultCentros.get(0));
                papel.addProveedor(defaultCentros.get(1));
                papel.addProveedor(defaultCentros.get(2));
                plasticoPET.addProveedor(defaultCentros.get(0));
                plasticoPET.addProveedor(defaultCentros.get(1));
                plasticoPET.addProveedor(defaultCentros.get(2));
                vidrio.addProveedor(defaultCentros.get(0));
                vidrio.addProveedor(defaultCentros.get(1));
                vidrio.addProveedor(defaultCentros.get(2));
                materialRepository.save(papel);
                materialRepository.save(plasticoPET);
                materialRepository.save(vidrio);

            } catch (Exception e) {
                System.err.println("Error al inicializar la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
