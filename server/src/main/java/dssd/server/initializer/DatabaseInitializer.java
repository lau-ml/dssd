package dssd.server.initializer;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import dssd.server.model.Recolector;
import dssd.server.model.Ubicacion;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.repository.RecolectorRepository;
import dssd.server.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RecolectorRepository recolectorRepository;
    private final CentroRecoleccionRepository centroRecoleccionRepository;
    private final UbicacionRepository ubicacionRepository;

    public DatabaseInitializer(MaterialRepository materialRepository,
                               RecolectorRepository recolectorRepository,
                               CentroRecoleccionRepository centroRecoleccionRepository,
                               UbicacionRepository ubicacionRepository,
                               PasswordEncoder passwordEncoder) {
        this.materialRepository = materialRepository;
        this.recolectorRepository = recolectorRepository;
        this.centroRecoleccionRepository = centroRecoleccionRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.passwordEncoder = passwordEncoder;
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
            List<Recolector> defaultRecolectores = new ArrayList<>();
            defaultRecolectores.add(new Recolector("Juan", "Pérez", "juan.perez@ecocycle.com", passwordEncoder.encode("123456"), "juanperez"));
            defaultRecolectores.add(new Recolector("María", "Gómez", "maria.gomez@ecocycle.com", passwordEncoder.encode("123456"), "mariagomez"));
            defaultRecolectores.add(new Recolector("Carlos", "López", "carlos.lopez@ecocycle.com", passwordEncoder.encode("123456"), "carloslopez"));

            recolectorRepository.saveAll(defaultRecolectores);

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
            Recolector recolector1 = recolectorRepository.findById(1L).get();
            recolector1.setCentroRecoleccion(centroRecoleccionRepository.findById(1L).get());
            recolectorRepository.save(recolector1);

            Recolector recolector2 = recolectorRepository.findById(2L).get();
            recolector2.setCentroRecoleccion(centroRecoleccionRepository.findById(1L).get());
            recolectorRepository.save(recolector2);

            Recolector recolector3 = recolectorRepository.findById(3L).get();
            recolector3.setCentroRecoleccion(centroRecoleccionRepository.findById(2L).get());
            recolectorRepository.save(recolector3);
        }
    }
}
