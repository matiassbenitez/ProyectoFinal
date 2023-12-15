package com.equipoQ.alquilerQuinchos.Repositorios;
import com.equipoQ.alquilerQuinchos.Entidades.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, String>{
    @Query("SELECT p FROM Publicacion p WHERE p.direccion = :direccion")
    public Publicacion buscarPorDireccion(@Param("direccion") String direccion);

    @Query("SELECT p FROM Publicacion p WHERE p.usuario.email = :email")
    public List<Publicacion> buscarPorEmailDeUsuario(@Param("email") String email);

    @Query("SELECT p FROM Publicacion p WHERE (:provincia IS NULL OR p.provincia = :provincia) AND (:categoria IS NULL OR p.categoria = :categoria)")
    public List<Publicacion> buscarPublicaciones(@Param("provincia") String provincia,@Param("categoria") String categoria);

}
