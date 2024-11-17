package dssd.server.initializer;

import dssd.server.model.*;
import dssd.server.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final CentroRecoleccionRepository centroRecoleccionRepository;
    private final PuntoDeRecoleccionRepository puntoDeRecoleccionRepository;
    private final RegistroRecoleccionRepository registroRecoleccionRepository;
    private final DetalleRegistroRepository detalleRegistroRepository;

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;

    public DatabaseInitializer(MaterialRepository materialRepository,
            UsuarioRepository usuarioRepository,
            CentroRecoleccionRepository centroRecoleccionRepository,
            PuntoDeRecoleccionRepository puntoDeRecoleccionRepository,
            PasswordEncoder passwordEncoder,
            PermisoRepository permisoRepository,
            RolRepository rolRepository,
            RegistroRecoleccionRepository registroRecoleccionRepository,
            DetalleRegistroRepository detalleRegistroRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.centroRecoleccionRepository = centroRecoleccionRepository;
        this.puntoDeRecoleccionRepository = puntoDeRecoleccionRepository;
        this.passwordEncoder = passwordEncoder;
        this.permisoRepository = permisoRepository;
        this.rolRepository = rolRepository;
        this.registroRecoleccionRepository = registroRecoleccionRepository;
        this.detalleRegistroRepository = detalleRegistroRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

        long count = materialRepository.count();
        if (count == 0) {
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

            // Cargar recolectores por defecto
            List<Usuario> defaultRecolectores = new ArrayList<>();
            defaultRecolectores.add(new Usuario("Juan", "Pérez", "juan.perez@ecocycle.com",
                    passwordEncoder.encode("123456"), "juanperez", "12345678"));
            defaultRecolectores.add(new Usuario("María", "Gómez", "maria.gomez@ecocycle.com",
                    passwordEncoder.encode("123456"), "mariagomez", "87654321"));
            defaultRecolectores.add(new Usuario("Carlos", "López", "carlos.lopez@ecocycle.com",
                    passwordEncoder.encode("123456"), "carloslopez", "45678901"));
            defaultRecolectores.add(new Usuario("Pedro", "Sánchez", "pedro.sanchez@ecocycle.com",
                    passwordEncoder.encode("123456"), "pedrosanchez", "23456701"));
            defaultRecolectores.add(new Usuario("Lucía", "Ramírez", "lucia.ramirez@ecocycle.com",
                    passwordEncoder.encode("123456"), "luciaramirez", "34567802"));
            defaultRecolectores.add(new Usuario("José", "García", "jose.garcia@ecocycle.com",
                    passwordEncoder.encode("123456"), "josegarcia", "45678903"));
            defaultRecolectores.add(new Usuario("Laura", "Ortiz", "laura.ortiz@ecocycle.com",
                    passwordEncoder.encode("123456"), "lauraortiz", "56789004"));
            defaultRecolectores.add(new Usuario("Miguel", "Morales", "miguel.morales@ecocycle.com",
                    passwordEncoder.encode("123456"), "miguelmorales", "67890105"));

            // Cargar empleados del centro de recolección por defecto
            List<Usuario> empleadosCentro = new ArrayList<>();
            empleadosCentro.add(new Usuario("Ana", "Frank", "ana.frank@ecocycle.com",
                    passwordEncoder.encode("123456"), "anafrank", "23456789"));
            empleadosCentro.add(new Usuario("Luis", "Martínez", "luis.martinez@ecocycle.com",
                    passwordEncoder.encode("123456"), "luismartinez", "34567890"));
            empleadosCentro.add(new Usuario("Sofía", "Fernández", "sofia.fernandez@ecocycle.com",
                    passwordEncoder.encode("123456"), "sofiafernandez", "56789012"));

            Usuario admin = new Usuario("admin", "ecocycle", "admin@ecocycle.com",
                    passwordEncoder.encode("123456"), "admin", "21256779");

            // Cargar centros de recolección por defecto
            List<CentroRecoleccion> defaultCentrosRecoleccion = new ArrayList<>();
            defaultCentrosRecoleccion
                    .add(new CentroRecoleccion("EcoPunto Verde",
                            "centro_recolecion1@ecocycle.com",
                            "(221) 242412"));
            defaultCentrosRecoleccion
                    .add(new CentroRecoleccion("Recolección Sostenible",
                            "centro_recolecion2@ecocycle.com",
                            "(221) 242211"));
            defaultCentrosRecoleccion
                    .add(new CentroRecoleccion("Centro EcoAmigo",
                            "centro_recolecion3@ecocycle.com",
                            "(221) 124242"));

            centroRecoleccionRepository.saveAll(defaultCentrosRecoleccion);

            // Cargar puntos de recolección por defecto
            List<PuntoDeRecoleccion> defaultPuntosDeRecoleccion = new ArrayList<>();
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("EcoGreen S.A.", "Calle Verde 101", "555-0001"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Reciclados del Norte", "Avenida del Reciclaje 202",
                            "555-0002"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Central Ambiental SRL", "Calle Ecológica 303",
                            "555-0003"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Green Solutions", "Calle Sustentable 404",
                            "555-0004"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Reutilizadora del Oeste",
                            "Avenida Reutilización 505", "555-0005"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Sustentabilidad Total", "Calle Verde 606",
                            "555-0006"));
            defaultPuntosDeRecoleccion.add(
                    new PuntoDeRecoleccion("Reciclaje Verde", "Avenida Verde 707", "555-0007"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("EcoAmigos", "Calle Amistad 808", "555-0008"));
            defaultPuntosDeRecoleccion.add(
                    new PuntoDeRecoleccion("Planeta Limpio", "Calle Limpieza 909", "555-0009"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("RecycloTech", "Avenida Tecnología 1010",
                            "555-0010"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Verde Esperanza", "Calle Esperanza 1111",
                            "555-0011"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Cuidado Ambiental S.A.", "Calle Cuidado 1212",
                            "555-0012"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Soluciones Ecológicas", "Avenida Soluciones 1313",
                            "555-0013"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Naturaleza Renovada", "Calle Naturaleza 1414",
                            "555-0014"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Reciclaje y Más", "Calle Reciclaje 1515",
                            "555-0015"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Punto Verde", "Calle Ecología 1616", "555-0016"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Reciclaje Total", "Avenida Reciclaje 1717",
                            "555-0017"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("EcoCentro", "Calle Centro 1818", "555-0018"));
            defaultPuntosDeRecoleccion.add(
                    new PuntoDeRecoleccion("Verde y Limpio", "Calle Limpieza 1919", "555-0019"));
            defaultPuntosDeRecoleccion
                    .add(new PuntoDeRecoleccion("Recolección Ecológica", "Avenida Ecológica 2020",
                            "555-0020"));

            puntoDeRecoleccionRepository.saveAll(defaultPuntosDeRecoleccion);

            // Asignar recolectores a centros de recolección
            defaultRecolectores.get(0).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            defaultRecolectores.get(1).setCentroRecoleccion(defaultCentrosRecoleccion.get(1));
            defaultRecolectores.get(2).setCentroRecoleccion(defaultCentrosRecoleccion.get(2));
            defaultRecolectores.get(3).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            defaultRecolectores.get(4).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            defaultRecolectores.get(5).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            defaultRecolectores.get(6).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            defaultRecolectores.get(7).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            usuarioRepository.saveAll(defaultRecolectores);

            // Asignar recolectores a puntos de recolección
            Random random = new Random();
            for (Usuario recolector : defaultRecolectores) {
                int numPuntos = 3 + random.nextInt(2);
                Collections.shuffle(defaultPuntosDeRecoleccion);
                List<PuntoDeRecoleccion> puntosAsignados = defaultPuntosDeRecoleccion.subList(0,
                        numPuntos);

                recolector.setPuntosDeRecoleccion(puntosAsignados);
            }

            // Crear registros de recolección para algunos recolectores
            List<RegistroRecoleccion> registrosRecoleccion = new ArrayList<>();
            RegistroRecoleccion registro1 = new RegistroRecoleccion();
            registro1.setRecolector(defaultRecolectores.get(0));
            registro1.setIdCentroRecoleccion(defaultCentrosRecoleccion.get(0).getId());
            registro1.setCompletado(true);
            registro1.setVerificado(false);

            RegistroRecoleccion registro2 = new RegistroRecoleccion();
            registro2.setRecolector(defaultRecolectores.get(1));
            registro2.setIdCentroRecoleccion(defaultCentrosRecoleccion.get(1).getId());
            registro2.setCompletado(true);
            registro2.setVerificado(false);

            registrosRecoleccion.add(registro1);
            registrosRecoleccion.add(registro2);

            registroRecoleccionRepository.saveAll(registrosRecoleccion);

            // Crear detalles de registro para cada recolector
            List<DetalleRegistro> detallesRegistro = new ArrayList<>();
            PuntoDeRecoleccion punto1 = defaultRecolectores.get(0).getPuntosDeRecoleccion().get(0);
            PuntoDeRecoleccion punto2 = defaultRecolectores.get(0).getPuntosDeRecoleccion().get(1);
            DetalleRegistro detalle1 = new DetalleRegistro();
            detalle1.setCantidadRecolectada(20);
            detalle1.setRegistroRecoleccion(registro1);
            detalle1.setPuntoRecoleccion(punto1);
            detalle1.setMaterial(defaultMaterials.get(0));

            DetalleRegistro detalle2 = new DetalleRegistro();
            detalle2.setCantidadRecolectada(15);
            detalle2.setRegistroRecoleccion(registro1);
            detalle2.setPuntoRecoleccion(punto2);
            detalle2.setMaterial(defaultMaterials.get(0));

            PuntoDeRecoleccion punto3 = defaultRecolectores.get(1).getPuntosDeRecoleccion().get(0);
            PuntoDeRecoleccion punto4 = defaultRecolectores.get(1).getPuntosDeRecoleccion().get(1);
            DetalleRegistro detalle3 = new DetalleRegistro();
            detalle3.setCantidadRecolectada(20);
            detalle3.setRegistroRecoleccion(registro2);
            detalle3.setPuntoRecoleccion(punto3);
            detalle3.setMaterial(defaultMaterials.get(1));

            DetalleRegistro detalle4 = new DetalleRegistro();
            detalle4.setCantidadRecolectada(15);
            detalle4.setRegistroRecoleccion(registro2);
            detalle4.setPuntoRecoleccion(punto4);
            detalle4.setMaterial(defaultMaterials.get(2));

            detallesRegistro.add(detalle1);
            detallesRegistro.add(detalle2);

            detalleRegistroRepository.saveAll(detallesRegistro);

            rolRepository.save(new Rol("ROLE_EMPLEADO", "Empleado"));
            rolRepository.save(new Rol("ROLE_ADMIN", "Administrador"));
            rolRepository.save(new Rol("ROLE_RECOLECTOR", "Recolector"));

            permisoRepository.save(new Permiso("PERMISO_VER_USUARIOS", "Ver usuarios"));
            permisoRepository.save(new Permiso("PERMISO_VER_RECOLECTORES", "Ver usuarios"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_USUARIOS", "Editar usuarios"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_USUARIOS", "Eliminar usuarios"));

            permisoRepository.save(new Permiso("PERMISO_VER_REGISTROS_RECOLECCION",
                    "Ver registros de recolección"));
            permisoRepository
                    .save(new Permiso("PERMISO_EDITAR_REGISTROS_RECOLECCION",
                            "Editar registros de recolección"));
            permisoRepository
                    .save(new Permiso("PERMISO_ELIMINAR_REGISTROS_RECOLECCION",
                            "Eliminar registros de recolección"));
            permisoRepository
                    .save(new Permiso("PERMISO_CREAR_REGISTROS_RECOLECCION",
                            "Crear registros de recolección"));
            permisoRepository
                    .save(new Permiso("PERMISO_REGISTRAR_MATERIALES_ENTREGADOS",
                            "Registrar los materiales entregados por el recolector"));
            permisoRepository.save(
                    new Permiso("PERMISO_CANCELAR_REGISTROS_RECOLECCION",
                            "Ver registros de recolección verificados"));

            permisoRepository.save(
                    new Permiso("PERMISO_VER_DETALLES_REGISTROS", "Ver detalles de registros"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_DETALLES_REGISTROS",
                    "Editar detalles de registros"));
            permisoRepository
                    .save(new Permiso("PERMISO_ELIMINAR_DETALLES_REGISTROS",
                            "Eliminar detalles de registros"));
            permisoRepository.save(
                    new Permiso("PERMISO_CREAR_DETALLES_REGISTROS", "Crear detalles de registros"));

            permisoRepository.save(new Permiso("PERMISO_VER_MATERIALES", "Ver materiales"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_MATERIALES", "Editar materiales"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_MATERIALES", "Eliminar materiales"));
            permisoRepository.save(new Permiso("PERMISO_CREAR_MATERIALES", "Crear materiales"));

            permisoRepository.save(
                    new Permiso("PERMISO_VER_CENTROS_RECOLECCION", "Ver centros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_CENTROS_RECOLECCION",
                    "Editar centros de recolección"));
            permisoRepository
                    .save(new Permiso("PERMISO_ELIMINAR_CENTROS_RECOLECCION",
                            "Eliminar centros de recolección"));

            permisoRepository
                    .save(new Permiso("PERMISO_VER_PUNTOS_RECOLECCIONES",
                            "Ver puntos de recolecciones del sistema"));
            permisoRepository
                    .save(new Permiso("PERMISO_VER_MIS_PUNTOS_RECOLECCIONES",
                            "Ver mis puntos de recoleccion"));
            permisoRepository
                    .save(new Permiso("PERMISO_ELIMINAR_MI_PUNTO_RECOLECCION",
                            "Eliminar mi punto de recoleccion"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_PUNTO_RECOLECCION",
                    "Editar puntos de recolección"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_PUNTO_RECOLECCION",
                    "Eliminar punto de recolección"));

            permisoRepository.save(
                    new Permiso("PERMISO_VER_ORDENES_DISTRIBUCION", "Ver órdenes de distribución"));
            permisoRepository
                    .save(new Permiso("PERMISO_EDITAR_ORDENES_DISTRIBUCION",
                            "Editar órdenes de distribución"));
            permisoRepository
                    .save(new Permiso("PERMISO_ELIMINAR_ORDENES_DISTRIBUCION",
                            "Eliminar órdenes de distribución"));

            permisoRepository.save(new Permiso("PERMISO_VER_CANTIDADES_MATERIALES",
                    "Ver cantidades de materiales"));
            permisoRepository
                    .save(new Permiso("PERMISO_EDITAR_CANTIDADES_MATERIALES",
                            "Editar cantidades de materiales"));
            permisoRepository
                    .save(new Permiso("PERMISO_ELIMINAR_CANTIDADES_MATERIALES",
                            "Eliminar cantidades de materiales"));

            permisoRepository.save(new Permiso("PERMISO_VER_ROLES", "Ver roles"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_ROLES", "Editar roles"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_ROLES", "Eliminar roles"));

            permisoRepository.save(new Permiso("PERMISO_VER_PERMISOS", "Ver permisos"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_PERMISOS", "Editar permisos"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_PERMISOS", "Eliminar permisos"));

            Rol rolEmpleado = rolRepository.findByNombre("ROLE_EMPLEADO").get();
            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN").get();
            Rol rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR").get();

            // Permisos para el rol ADMIN
            List<Permiso> permisosAdmin = Arrays.asList(
                    permisoRepository.findByNombre("PERMISO_VER_USUARIOS").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_USUARIOS").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_USUARIOS").get(),
                    permisoRepository.findByNombre("PERMISO_VER_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_CREAR_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_VER_CENTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_CENTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_CENTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_VER_PUNTOS_RECOLECCIONES").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_PUNTO_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_PUNTO_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_VER_ORDENES_DISTRIBUCION").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_ORDENES_DISTRIBUCION").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_ORDENES_DISTRIBUCION").get(),
                    permisoRepository.findByNombre("PERMISO_VER_CANTIDADES_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_CANTIDADES_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_CANTIDADES_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_VER_ROLES").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_ROLES").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_ROLES").get(),
                    permisoRepository.findByNombre("PERMISO_VER_PERMISOS").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_PERMISOS").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_PERMISOS").get());
            rolAdmin.setPermisos(permisosAdmin);

            // Permisos para el rol EMPLEADO
            List<Permiso> permisosEmpleado = Arrays.asList(
                    permisoRepository.findByNombre("PERMISO_VER_RECOLECTORES").get(),
                    permisoRepository.findByNombre("PERMISO_VER_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_VER_ORDENES_DISTRIBUCION").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_ORDENES_DISTRIBUCION").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_ORDENES_DISTRIBUCION").get(),
                    permisoRepository.findByNombre("PERMISO_REGISTRAR_MATERIALES_ENTREGADOS")
                            .get());
            rolEmpleado.setPermisos(permisosEmpleado);

            // Permisos para el rol RECOLECTOR
            List<Permiso> permisosRecolector = Arrays.asList(
                    permisoRepository.findByNombre("PERMISO_VER_REGISTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_REGISTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_REGISTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_CREAR_REGISTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_CANCELAR_REGISTROS_RECOLECCION").get(),
                    permisoRepository.findByNombre("PERMISO_VER_DETALLES_REGISTROS").get(),
                    permisoRepository.findByNombre("PERMISO_EDITAR_DETALLES_REGISTROS").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_DETALLES_REGISTROS").get(),
                    permisoRepository.findByNombre("PERMISO_CREAR_DETALLES_REGISTROS").get(),
                    permisoRepository.findByNombre("PERMISO_VER_MATERIALES").get(),
                    permisoRepository.findByNombre("PERMISO_VER_MIS_PUNTOS_RECOLECCIONES").get(),
                    permisoRepository.findByNombre("PERMISO_ELIMINAR_MI_PUNTO_RECOLECCION").get());
            rolRecolector.setPermisos(permisosRecolector);

            rolRepository.save(rolAdmin);
            rolRepository.save(rolEmpleado);
            rolRepository.save(rolRecolector);

            defaultRecolectores.forEach(recolector -> {
                recolector.setRol(rolRecolector);
                usuarioRepository.save(recolector);
            });

            admin.setRol(rolAdmin);
            usuarioRepository.save(admin);

            empleadosCentro.forEach(empleado -> empleado.setRol(rolEmpleado));
            // Asignar empleados a centros de recolección
            empleadosCentro.get(0).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            empleadosCentro.get(1).setCentroRecoleccion(defaultCentrosRecoleccion.get(1));
            empleadosCentro.get(2).setCentroRecoleccion(defaultCentrosRecoleccion.get(2));

            usuarioRepository.saveAll(empleadosCentro);

        }
    }
}
