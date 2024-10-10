package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@Entity
@DiscriminatorValue("DEPOSITO_GLOBAL")
public class DepositoGlobal extends Centro {

    @OneToMany(mappedBy = "depositoGlobal")
    private Set<Pedido> pedidos;

    // Getters and Setters
}

