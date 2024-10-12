package dssd.server.initializer;

import dssd.server.model.*;
import dssd.server.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final CentroRecoleccionRepository centroRecoleccionRepository;
    private final UbicacionRepository ubicacionRepository;

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;

    public DatabaseInitializer(MaterialRepository materialRepository,
                               UsuarioRepository usuarioRepository,
                               CentroRecoleccionRepository centroRecoleccionRepository,
                               UbicacionRepository ubicacionRepository,
                               PasswordEncoder passwordEncoder,
                               PermisoRepository permisoRepository,
                               RolRepository rolRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.centroRecoleccionRepository = centroRecoleccionRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.permisoRepository = permisoRepository;
        this.rolRepository = rolRepository;
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
            defaultRecolectores.add(new Usuario("Juan", "Pérez", "juan.perez@ecocycle.com", passwordEncoder.encode("123456"), "juanperez"));
            defaultRecolectores.add(new Usuario("María", "Gómez", "maria.gomez@ecocycle.com", passwordEncoder.encode("123456"), "mariagomez"));
            defaultRecolectores.add(new Usuario("Carlos", "López", "carlos.lopez@ecocycle.com", passwordEncoder.encode("123456"), "carloslopez"));



            // Cargar centros de recolección por defecto
            List<CentroRecoleccion> defaultCentrosRecoleccion = new ArrayList<>();
            defaultCentrosRecoleccion
                    .add(new CentroRecoleccion("Centro de recolección 1", "centro_recolecion1@ecocycle.com",
                            "(221) 242412"));
            defaultCentrosRecoleccion
                    .add(new CentroRecoleccion("Centro de recolección 2", "centro_recolecion2@ecocycle.com",
                            "(221) 242211"));
            defaultCentrosRecoleccion
                    .add(new CentroRecoleccion("Centro de recolección 3", "centro_recolecion3@ecocycle.com",
                            "(221) 124242"));

            centroRecoleccionRepository.saveAll(defaultCentrosRecoleccion);

            // Cargar ubicaciones por defecto
            List<Ubicacion> defaultUbicaciones = new ArrayList<>();
            defaultUbicaciones.add(new Ubicacion("Ubicación 1"));
            defaultUbicaciones.add(new Ubicacion("Ubicación 2"));
            defaultUbicaciones.add(new Ubicacion("Ubicación 3"));
            defaultUbicaciones.add(new Ubicacion("Ubicación 4"));
            defaultUbicaciones.add(new Ubicacion("Ubicación 5"));

            ubicacionRepository.saveAll(defaultUbicaciones);

            // Asignar recolectores a centros de recolección
            defaultRecolectores.get(0).setCentroRecoleccion(defaultCentrosRecoleccion.get(0));
            defaultRecolectores.get(1).setCentroRecoleccion(defaultCentrosRecoleccion.get(1));
            defaultRecolectores.get(2).setCentroRecoleccion(defaultCentrosRecoleccion.get(2));
            usuarioRepository.saveAll(defaultRecolectores);

            rolRepository.save(new Rol("ROLE_EMPLEADO", "Empleado"));
            rolRepository.save(new Rol("ROLE_ADMIN", "Administrador"));
            rolRepository.save(new Rol("ROLE_RECOLECTOR", "Recolector"));

            permisoRepository.save(new Permiso("PERMISO_VER_USUARIOS", "Ver usuarios"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_USUARIOS", "Editar usuarios"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_USUARIOS", "Eliminar usuarios"));

            permisoRepository.save(new Permiso("PERMISO_VER_REGISTROS_RECOLECCION", "Ver registros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_REGISTROS_RECOLECCION", "Editar registros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_REGISTROS_RECOLECCION", "Eliminar registros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_CREAR_REGISTROS_RECOLECCION", "Crear registros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_CANCELAR_REGISTROS_RECOLECCION", "Ver registros de recolección verificados"));


            permisoRepository.save(new Permiso("PERMISO_VER_DETALLES_REGISTROS", "Ver detalles de registros"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_DETALLES_REGISTROS", "Editar detalles de registros"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_DETALLES_REGISTROS", "Eliminar detalles de registros"));
            permisoRepository.save(new Permiso("PERMISO_CREAR_DETALLES_REGISTROS", "Crear detalles de registros"));


            permisoRepository.save(new Permiso("PERMISO_VER_MATERIALES", "Ver materiales"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_MATERIALES", "Editar materiales"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_MATERIALES", "Eliminar materiales"));
            permisoRepository.save(new Permiso("PERMISO_CREAR_MATERIALES", "Crear materiales"));

            permisoRepository.save(new Permiso("PERMISO_VER_CENTROS_RECOLECCION", "Ver centros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_CENTROS_RECOLECCION", "Editar centros de recolección"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_CENTROS_RECOLECCION", "Eliminar centros de recolección"));

            permisoRepository.save(new Permiso("PERMISO_VER_UBICACIONES", "Ver ubicaciones"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_UBICACIONES", "Editar ubicaciones"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_UBICACIONES", "Eliminar ubicaciones"));

            permisoRepository.save(new Permiso("PERMISO_VER_ORDENES_DISTRIBUCION", "Ver órdenes de distribución"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_ORDENES_DISTRIBUCION", "Editar órdenes de distribución"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_ORDENES_DISTRIBUCION", "Eliminar órdenes de distribución"));

            permisoRepository.save(new Permiso("PERMISO_VER_CANTIDADES_MATERIALES", "Ver cantidades de materiales"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_CANTIDADES_MATERIALES", "Editar cantidades de materiales"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_CANTIDADES_MATERIALES", "Eliminar cantidades de materiales"));

            permisoRepository.save(new Permiso("PERMISO_VER_ROLES", "Ver roles"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_ROLES", "Editar roles"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_ROLES", "Eliminar roles"));

            permisoRepository.save(new Permiso("PERMISO_VER_PERMISOS", "Ver permisos"));
            permisoRepository.save(new Permiso("PERMISO_EDITAR_PERMISOS", "Editar permisos"));
            permisoRepository.save(new Permiso("PERMISO_ELIMINAR_PERMISOS", "Eliminar permisos"));


            Rol rolEmpleado = rolRepository.findByNombre("ROLE_EMPLEADO").get();
            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN").get();
            Rol rolRecolector = rolRepository.findByNombre("ROLE_ADMIN").get();

            rolAdmin.setPermisos(permisoRepository.findAll());
            rolEmpleado.setPermisos(permisoRepository.findAll());
            rolRecolector.setPermisos(permisoRepository.findAll());

            rolRepository.save(rolAdmin);
            rolRepository.save(rolEmpleado);
            rolRepository.save(rolRecolector);

            defaultRecolectores.forEach(recolector -> {
                recolector.setRol(rolRecolector);
                usuarioRepository.save(recolector);
            });

        }
    }
}
