
package com.equipoQ.alquilerQuinchos.Repositorios;
import com.equipoQ.alquilerQuinchos.Entidades.Reserva;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservaRepositorio  extends JpaRepository <Reserva, String>  {

    @Query("SELECT r FROM Reserva r WHERE r.cliente.id = :id")
    public List<Reserva> buscarPorIdDeUsuario(@Param("id") String id);

    //@Query("SELECT r FROM Reserva r JOIN r.usuario u WHERE u.id = :idUsuario")
    //public List<Reserva> buscarPorIdDeUsuario(@Param("idUsuario") String idUsuario);

}
