package dssd.server.service;

import dssd.server.model.Estadistica;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.EstadisticaRepository;
import dssd.server.repository.RegistroRecoleccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EstadisticaService {

    @Autowired
    private EstadisticaRepository estadisticaRepository;

    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;


    @Autowired
    private UserService userService;

    public Boolean getTuvoEstadisticaMitadMesUsuario(String email) {
        Usuario usuario = userService.findByEmail(email);

        if (usuario == null) {
            return false; // Usuario no encontrado
        }

        Optional<Estadistica> estadistica = estadisticaRepository.findFirstByRecolectorOrderByFechaCreacionDesc(usuario);

        if (estadistica.isEmpty()) {
            return false; // No hay estadísticas
        }

        // Fecha de la última estadística
        LocalDate fechaEstadistica = estadistica.get().getFechaCreacion(); // Ajustar según el tipo de dato en `fecha`

        // Evaluar si pasaron 15 días desde esa fecha
        LocalDate hoy = LocalDate.now();
        long diasDesdeUltimaEstadistica = ChronoUnit.DAYS.between(fechaEstadistica, hoy);

        return diasDesdeUltimaEstadistica > 15;


    }


    public Estadistica crearEstadistica(String email) {
        Usuario usuario = userService.findByEmail(email);

        if (usuario == null) {
            return null; // Usuario no encontrado
        }
        Optional<List<RegistroRecoleccion>> registroRecoleccions = this.registroRecoleccionRepository.findByRecolectorAndFechaRecoleccionBetweenOrderByFechaRecoleccion(usuario, LocalDate.now().minusDays(15), LocalDate.now());
        if (registroRecoleccions.isEmpty()) {
            return null; // No hay registros
        }

        // Inicializamos las variables para contar y almacenar la información
        AtomicInteger completados = new AtomicInteger();
        AtomicInteger noCompletados = new AtomicInteger();
        AtomicReference<Double> totalPagosCompletados = new AtomicReference<>(0.0); // Iniciamos como 0.0
        Set<Long> centrosVisitados = new HashSet<>();  // Usamos un Set para almacenar centros únicos
        StringBuilder resumen = new StringBuilder();

// Recorremos los registros de recolección
        registroRecoleccions.get().forEach(registroRecoleccion -> {
            // Contamos los registros completados y no completados
            if (registroRecoleccion.isCompletado()) {
                completados.getAndIncrement();
                // Si el registro está completado, sumamos el monto del pago
                if (registroRecoleccion.getPago() != null) {
                    // Actualizamos el total de pagos completados de manera segura
                    totalPagosCompletados.updateAndGet(v -> v + registroRecoleccion.getPago().getMonto());
                }
            } else {
                noCompletados.getAndIncrement();
            }

            // Agregamos el id del centro de recolección al Set para contar centros únicos
            centrosVisitados.add(registroRecoleccion.getIdCentroRecoleccion());
        });

// Construimos el resumen final
        resumen.insert(0, "Cantidad de Registros Completados: " + completados.get() + "\n");
        resumen.append("Cantidad de Registros No Completados: ").append(noCompletados.get()).append("\n");
        resumen.append("Total de Pagos Completados: ").append(totalPagosCompletados.get()).append("\n");
        resumen.append("Cantidad de Centros Visitados: ").append(centrosVisitados.size()).append("\n");
        Estadistica estadistica = new Estadistica();
        estadistica.setRecolector(usuario);
        estadistica.setDescripcion(resumen.toString());
        estadistica.setFechaCreacion(LocalDate.now());
        return estadisticaRepository.save(estadistica);
    }
}
