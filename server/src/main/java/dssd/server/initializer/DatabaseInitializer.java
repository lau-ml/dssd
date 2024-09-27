package dssd.server.initializer;

import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import dssd.server.model.Recolector;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.repository.RecolectorRepository;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final MaterialRepository materialRepository;
    private final RecolectorRepository recolectorRepository;
    private final CentroRecoleccionRepository centroRecoleccionRepository;

    public DatabaseInitializer(MaterialRepository materialRepository,
            RecolectorRepository recolectorRepository,
            CentroRecoleccionRepository centroRecoleccionRepository) {
        this.materialRepository = materialRepository;
        this.recolectorRepository = recolectorRepository;
        this.centroRecoleccionRepository = centroRecoleccionRepository;
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
            defaultRecolectores.add(new Recolector("Juan", "Pérez", "juan.perez@ecocycle.com", "123456"));
            defaultRecolectores.add(new Recolector("María", "Gómez", "maria.gomez@ecocycle.com", "123456"));
            defaultRecolectores.add(new Recolector("Carlos", "López", "carlos.lopez@ecocycle.com", "123456"));

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
