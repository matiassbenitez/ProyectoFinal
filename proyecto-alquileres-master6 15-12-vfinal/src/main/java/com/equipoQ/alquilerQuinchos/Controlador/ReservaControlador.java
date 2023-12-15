

package com.equipoQ.alquilerQuinchos.Controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.equipoQ.alquilerQuinchos.Entidades.Imagen;
import com.equipoQ.alquilerQuinchos.Entidades.Publicacion;
import com.equipoQ.alquilerQuinchos.Entidades.Reserva;
import com.equipoQ.alquilerQuinchos.Entidades.Usuario;
import com.equipoQ.alquilerQuinchos.Enumeraciones.Rol;
import com.equipoQ.alquilerQuinchos.Excepciones.MiExcepcion;
import com.equipoQ.alquilerQuinchos.Repositorios.ReservaRepositorio;
import com.equipoQ.alquilerQuinchos.Repositorios.UsuarioRepositorio;
import com.equipoQ.alquilerQuinchos.Servicios.PublicacionServicio;
import com.equipoQ.alquilerQuinchos.Servicios.ReservaServicio;
import com.equipoQ.alquilerQuinchos.Servicios.UsuarioServicio;

@Controller
@RequestMapping("/reserva")
public class ReservaControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    private PublicacionServicio publicacionServicio;

    @GetMapping("/{id}")
    public String reservar(@PathVariable String id, ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) {
        // Obtener el usuario actual de la sesión
        // Comprobar si el usuario tiene el rol de AGENTE
        if (usuario.getRol() == Rol.CLIENTE) {
            modelo.put("usuario", usuario);
            // return "reserva_form.html";
        } else {
            // Si no, mostrar un mensaje de error y redirigir al inicio
            modelo.put("error", "Solo los clientes pueden realizar reservas");
            return "redirect:/";
        }
        Optional<Publicacion> publicacionOptional = publicacionServicio.getOne(id);
        if (!publicacionOptional.isPresent()) {
            modelo.put("error", "La publicación no existe");
            return "redirect:/";
        }
        Publicacion publicacion = publicacionOptional.get();
        modelo.put("publicacion", publicacion);
        // Agregar los diasDisponibles al modelo
        modelo.put("diasDisponibles", publicacion.getDiasDisponibles());
        return "reserva_form.html";
    }

    @PostMapping("/reservar/{id}")
    public String reserva(@PathVariable String id, @RequestParam String usuarioId, @RequestParam String fecha, Boolean wifi, Boolean pileta, @RequestParam Float precio_final, ModelMap modelo)
    {
        try {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
            LocalDate fechaReservada = LocalDate.parse(fecha.trim(), formatter);

            // Busca al propietario por su ID de usuario
            Usuario usuario = usuarioServicio.getOne(usuarioId);
            Optional<Publicacion> publicacionOptional = publicacionServicio.getOne(id);
            if (!publicacionOptional.isPresent()) {
                modelo.put("error", "La publicación no existe");
                return "redirect:/";
            }
            Publicacion publicacion = publicacionOptional.get();
            // Crea la reserva
            reservaServicio.crearReserva(fechaReservada, usuario, publicacion, wifi, pileta,precio_final);

            modelo.put("Exito", "La propiedad fue reservada correctamente");
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "reserva_form.html";
        }
        return "redirect:/";
    }

   /* @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        // Obtener el usuario actual de la sesión
        //Usuario usuario = (Usuario) modelo.get("usuariosession");
        // Obtener la publicacion por su ID
        Optional<Reserva> reservaOptional = reservaServicio.getOne(id);
        // Comprobar si la publicacion es null
        if (!reservaOptional.isPresent()) {
            // Manejar el caso cuando la publicacion es null
            modelo.put("error", "La reserva no existe");
            return "redirect:/";
        }
        Reserva reserva = reservaOptional.get();
        // Mostrar el formulario de modificación
        modelo.put("reserva", reserva);
        return "reserva_modify.html";
    }*/

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) {

        Optional<Reserva> respuesta = reservaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Reserva reserva = respuesta.get();
            reserva.setAltas(false);
            reservaRepositorio.save(reserva);
        }
        return "redirect:../listar";
    }

    @GetMapping("/listar/{id}")
    public String listar(@PathVariable String id, ModelMap modelo) {
        List<Reserva> reservas = reservaServicio.listarReservasPorId(id);
        Optional<Usuario> clienteOptional = usuarioRepositorio.findById(id);
        if (clienteOptional.isPresent()) {
            List<Reserva> reservasEnAlta = new ArrayList<>();
            for (Reserva reserva : reservas) {
                if (reserva.getAltas()) {
                    reservasEnAlta.add(reserva);
                }
            }
            modelo.addAttribute("reservas", reservasEnAlta);
            return "reserva_list";
        } else {
            modelo.addAttribute("error", "El cliente no existe.");
            return "redirect:/login";
        }
    }

    @GetMapping("/mostrar/{id}")
    public String mostrar(@PathVariable String id, ModelMap modelo) {
        List<Reserva> reservas = reservaRepositorio.findAll();
        Optional<Usuario> clienteOptional = usuarioRepositorio.findById(id);
        if (clienteOptional.isPresent()) {
            List<Reserva> reservasEnAlta = new ArrayList<>();
            for (Reserva reserva : reservas) {
                if ((reserva.getAltas()) && (reserva.getPublicacion().getUsuario().getId().equals(id))) {
                    reservasEnAlta.add(reserva);
                }
            }
            modelo.addAttribute("reservas", reservasEnAlta);
            return "reserva_list";
        } else {
            modelo.addAttribute("error", "El cliente no existe.");
            return "redirect:/login";
        }
    }


    @GetMapping("/aceptar/{id}/{usuarioId}")
    public String aceptar(@PathVariable String id,@PathVariable String usuarioId, ModelMap modelo){
        Optional<Reserva> respuesta = reservaRepositorio.findById(id);
        if (!respuesta.isPresent()){
            modelo.addAttribute("error", "La reserva no existe.");
            return "redirect:/login";
        }
        //Reserva reserva = respuesta.get();
        reservaServicio.aceptar(id);
        List<Reserva> reservas = reservaRepositorio.findAll();
        //String path = "redirect:/mostrar/" + usuarioId;
        //return "redirect:/mostrar/${session.usuariosession.id}";
        modelo.addAttribute("reservas", reservas);
        //return "redirect:/mostrar/${usuarioId}";
        return "reserva_list";
    }

    @GetMapping("/rechazar/{id}")
    public String rechazar(@PathVariable String id){
        reservaServicio.rechazar(id);
        return "redirect:/listar/{id}";
    }

    
}