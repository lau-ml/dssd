package dssd.server.DTO;

import java.util.Date;

import dssd.server.model.Pago;
import dssd.server.model.Pago.EstadoPago;
import lombok.Getter;

@Getter
public class PagoDTO {

    private Long id;
    private Double monto;
    private Long registroRecoleccionId;
    private Date fechaPago;
    private EstadoPago estado;

    public PagoDTO() {
    }

    public PagoDTO(Pago pago) {
        this.id = pago.getId();
        this.monto = pago.getMonto();
        this.registroRecoleccionId = pago.getRegistroRecoleccion().getId();
        this.fechaPago = pago.getFechaPago();
        this.estado = pago.getEstado();
    }

}
