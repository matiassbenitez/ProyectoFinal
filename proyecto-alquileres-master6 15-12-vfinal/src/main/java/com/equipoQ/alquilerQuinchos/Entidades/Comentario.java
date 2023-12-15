package com.equipoQ.alquilerQuinchos.Entidades;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.*;
@Entity
@Data
public class Comentario {

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String id;
   @Lob
   private String texto;

   @Temporal(TemporalType.DATE)
   private Date fecha;
   
   // @ManyToOne
   // @JoinColumn(name = "publicacion_id")
   // private Publicacion publicacion;
   @ManyToOne
   @JoinColumn(name = "usuario_id")
   private Usuario usuario;
}
