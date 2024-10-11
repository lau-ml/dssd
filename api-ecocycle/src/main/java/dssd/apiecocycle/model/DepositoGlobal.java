package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
@DiscriminatorValue("DEPOSITO_GLOBAL")
public class DepositoGlobal extends Centro {

    @OneToMany(mappedBy = "depositoGlobal")
    private List<Pedido> pedidos = new ArrayList<>();

    public DepositoGlobal() {
        super();
    }

    public DepositoGlobal(String email, String password, String telefono, String direccion) {
        super(email, password, telefono, direccion);
    }

    public DepositoGlobal(String email, String password, String telefono, String direccion, Permiso permiso) {
        super(email, password, telefono, direccion, permiso);
    }

    public DepositoGlobal(String email, String password, String telefono, String direccion, Set<Permiso> permisos) {
        super(email, password, telefono, direccion, permisos);
    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
