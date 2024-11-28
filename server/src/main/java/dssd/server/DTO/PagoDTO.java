package dssd.server.DTO;

import java.time.LocalDate;
import java.util.Date;

import dssd.server.model.Pago;
import dssd.server.model.Pago.EstadoPago;
import lombok.Getter;

@Getter
public class PagoDTO {

    private Long id;
    private Double monto;
    private Long registroRecoleccionId;
    private LocalDate fechaEmision;
    private LocalDate fechaPago;
    private EstadoPago estado;
    private UsuarioDTO recolector;

    public PagoDTO() {
    }

    public PagoDTO(Pago pago) {
        this.id = pago.getId();
        this.monto = pago.getMonto();
        this.registroRecoleccionId = pago.getRegistroRecoleccion().getId();
        this.fechaEmision = pago.getFechaEmision();
        this.fechaPago = pago.getFechaPago();
        this.estado = pago.getEstado();
        this.recolector = new UsuarioDTO(pago.getRegistroRecoleccion().getRecolector());
    }

}
