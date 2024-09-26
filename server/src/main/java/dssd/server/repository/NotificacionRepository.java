package dssd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

}