package dssd.server.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class RegistroRecoleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_recolector")
    private Recolector recolector;

    @Column(nullable = false)
    private Long idCentroRecoleccion;

    @CreationTimestamp
    private Date fechaRecoleccion;

    @Column(nullable = false)
    private boolean validado = false;

    @OneToOne(mappedBy = "registroRecoleccion")
    private Pago pago;

    @OneToMany(mappedBy = "registroRecoleccion")
    private List<DetalleRegistro> detalleRegistros;

    public RegistroRecoleccion() {
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recolector getRecolector() {
        return recolector;
    }

    public void setRecolector(Recolector recolector) {
        this.recolector = recolector;
    }

    public Long getIdCentroRecoleccion() {
        return idCentroRecoleccion;
    }

    public void setIdCentroRecoleccion(Long idCentroRecoleccion) {
        this.idCentroRecoleccion = idCentroRecoleccion;
    }

    public Date getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(Date fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public boolean isValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public List<DetalleRegistro> getDetalleRegistros() {
        return detalleRegistros;
    }

    public void setDetalleRegistros(List<DetalleRegistro> detalleRegistros) {
        this.detalleRegistros = detalleRegistros;
    }
}
