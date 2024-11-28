package dssd.server.service;

import dssd.server.model.Estadistica;
import dssd.server.model.Usuario;
import dssd.server.repository.EstadisticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadisticaService {

     @Autowired
    private EstadisticaRepository estadisticaRepository;

     @Autowired
    private UserService userService;

    public Estadistica getUsuariosQueReciclaronPorPrimeraVezEnLosUltimos15Dias(String email) {
        Usuario usuario = userService.findByEmail(email);
        return estadisticaRepository.findByRecolector(usuario);
    }



}
