package com.equipoQ.alquilerQuinchos.Entidades;
import com.sun.istack.*;
import java.io.*;
import java.time.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.*;
@Entity
@Data
@NoArgsConstructor
public class Publicacion {
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String id;
   private Boolean altas;
   private String direccion;
   private String localidad;

   @Column(name = "provincia")
   private String provincia;

   @Temporal(TemporalType.DATE)
   private Date alta;

   @ManyToOne
   @JoinColumn(name = "usuario_id")
   private Usuario usuario;

   @Column(name = "precio_base")
   @NotNull
   private Float precio_base;
   private Boolean wifi;
   private Boolean pileta;
   private Integer capacidad;
   @OneToMany
   private List<Imagen> imagenes;

   @Column(name = "titulo", length = 100)
   private String titulo;
   @Column(name = "descripcion", length = 500)
   @Lob
   private String descripcion;
   private String categoria;
   @OneToMany
   private List<Comentario> comentarios;
   @ElementCollection
   private List<LocalDate> diasDisponibles;
}
