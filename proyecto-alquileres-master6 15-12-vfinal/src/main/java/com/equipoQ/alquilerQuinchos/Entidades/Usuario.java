package com.equipoQ.alquilerQuinchos.Entidades;
import com.equipoQ.alquilerQuinchos.Enumeraciones.*;
import javax.persistence.CascadeType;
import java.util.*;
import static java.util.Collections.list;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.*;
//import lombok.Data; crea el geter and seter automaticamente

@Entity
@Data
public class Usuario {

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String id;
   private Boolean altas;
   private String nombre;
   @Column(name = "email", length = 50, unique = true)
   private String email;
   @Column(name = "password")
   private String password;
   @Enumerated(EnumType.STRING)
   private Rol rol;
   // @JoinColumn indica nombre de la columna que hace referencia a la entidad relacionada
   @OneToOne
   @JoinColumn(name = "imagen_id")
   private Imagen imagen;
   @OneToMany
   private List<Publicacion> publicaciones;
   @OneToMany
   private List<Comentario> comentarios;
   //   (mappedBy = "usuario", cascade = CascadeType.ALL)  
//   (mappedBy = "usuario", cascade = CascadeType.ALL) tendria que ver para aplicarlo si borro un usuario se borra todo lo que tiene 

}
