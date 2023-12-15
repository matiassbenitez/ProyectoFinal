package com.equipoQ.alquilerQuinchos.Entidades;

import java.time.*;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.*;
import javax.persistence.Entity;

import lombok.*;
@Entity
@Data
public class Reserva {

@Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String id;

    private Boolean altas;
    private LocalDate fechaDeReserva;
    private LocalDate fechaReservada;
    private Boolean wifi;
    private Boolean pileta;
    private Boolean leido;
    private Boolean aceptado;
    @ManyToOne
    private Usuario cliente;
    @ManyToOne
    private Publicacion publicacion;
    private Float precioFinal;

}


     
