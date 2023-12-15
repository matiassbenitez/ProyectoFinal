package com.equipoQ.alquilerQuinchos.Servicios;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equipoQ.alquilerQuinchos.Entidades.Publicacion;
import com.equipoQ.alquilerQuinchos.Entidades.Reserva;
import com.equipoQ.alquilerQuinchos.Entidades.Usuario;
import com.equipoQ.alquilerQuinchos.Excepciones.MiExcepcion;
import com.equipoQ.alquilerQuinchos.Repositorios.PublicacionRepositorio;
import com.equipoQ.alquilerQuinchos.Repositorios.ReservaRepositorio;

@Service
public class ReservaServicio {
    
    @Autowired
    private PublicacionRepositorio publicacionRepositorio;
    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Transactional
    public void crearReserva( LocalDate fecha, Usuario cliente, Publicacion publicacion, Boolean wifi, Boolean pileta, Float precio_final) throws MiExcepcion{
        Optional<Publicacion> respuesta = publicacionRepositorio.findById(publicacion.getId());
        if (respuesta.isPresent()) {
            Reserva reserva = new Reserva();

            LocalDate fechaReserva = LocalDate.now();
            reserva.setFechaDeReserva(fechaReserva);

            reserva.setFechaReservada(fecha);
            reserva.setCliente(cliente);
            reserva.setPublicacion(publicacion);
            reserva.setAltas(true);
            if (wifi == null){
                wifi = false;
            }
            if (pileta == null){
                pileta = false;
            }
            reserva.setLeido(false);
            reserva.setAceptado(false);
            reserva.setWifi(wifi);
            reserva.setPileta(pileta);
            reserva.setPrecioFinal(precio_final);
            reservaRepositorio.save(reserva);
        }
    }

    public List<Reserva> listarReservasPorId(String id){
      List<Reserva> reservas = new ArrayList();
      reservas = reservaRepositorio.buscarPorIdDeUsuario(id);
      return reservas;
    }

    public Optional<Reserva> getOne(String id) {
        return reservaRepositorio.findById(id);
    }

    public void aceptar(String id){
        Optional<Reserva> respuesta = reservaRepositorio.findById(id);
        if (respuesta.isPresent()){
            Reserva reserva = respuesta.get();
            reserva.setLeido(true);
            reserva.setAceptado(true);
            reservaRepositorio.save(reserva);
        }
    }

    public void rechazar(String id){
        Optional<Reserva> respuesta = reservaRepositorio.findById(id);
        if (respuesta.isPresent()){
            Reserva reserva = respuesta.get();
            reserva.setLeido(true);
        }
    }
}
