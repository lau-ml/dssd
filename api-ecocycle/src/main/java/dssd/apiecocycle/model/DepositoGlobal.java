package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@DiscriminatorValue("DEPOSITO_GLOBAL")
public class DepositoGlobal extends Centro {

    @OneToMany(mappedBy = "depositoGlobal")
    private List<Pedido> pedidos = new ArrayList<>();

    public DepositoGlobal() {
        super();
    }

    public DepositoGlobal(String nombre, String email, String password, String telefono, String direccion) {
        super(nombre, email, password, telefono, direccion);
    }

    @Override
    public List<Orden> getOrdenes() {
        return pedidos.stream().map(Pedido::getOrdenes).reduce(new ArrayList<>(), (a, b) -> {
            a.addAll(b);
            return a;
        });
    }

    public Orden getOrdenById(Long id) {
        return pedidos.stream().map(Pedido::getOrdenes).flatMap(List::stream).filter(orden -> orden.getId().equals(id)).findFirst().orElse(null);
    }

    public Pedido getPedidoById(Long id) {
        return pedidos.stream().filter(pedido -> pedido.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean onlyMinePedidos() {
        return true;
    }
}
