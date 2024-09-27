package dssd.server.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Recolector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 250)
    private String password;

    @ManyToOne
    @JoinColumn(name = "centro_recoleccion_id")
    private CentroRecoleccion centroRecoleccion;

    @OneToMany(mappedBy = "recolector")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "recolector")
    private List<Estadistica> estadisticas;

    @OneToMany(mappedBy = "recolector")
    private List<RegistroRecoleccion> registroRecolecciones;

    public Recolector() {

    }

    public Recolector(String nombres, String apellido, String email, String password) {
        this.nombres = nombres;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public List<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(List<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public List<RegistroRecoleccion> getRegistroRecolecciones() {
        return registroRecolecciones;
    }

    public void setRegistroRecolecciones(List<RegistroRecoleccion> registroRecolecciones) {
        this.registroRecolecciones = registroRecolecciones;
    }

    public CentroRecoleccion getCentroRecoleccion() {
        return this.centroRecoleccion;
    }

    public void setCentroRecoleccion(CentroRecoleccion centroRecoleccion) {
        this.centroRecoleccion = centroRecoleccion;
    }
}
