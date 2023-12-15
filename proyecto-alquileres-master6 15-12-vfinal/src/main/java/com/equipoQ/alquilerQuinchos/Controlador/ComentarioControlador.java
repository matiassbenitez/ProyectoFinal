/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoQ.alquilerQuinchos.Controlador;

import com.equipoQ.alquilerQuinchos.Entidades.Publicacion;
import com.equipoQ.alquilerQuinchos.Entidades.Usuario;
import com.equipoQ.alquilerQuinchos.Enumeraciones.Rol;
import com.equipoQ.alquilerQuinchos.Excepciones.MiExcepcion;
import com.equipoQ.alquilerQuinchos.Servicios.ComentarioServicio;
import com.equipoQ.alquilerQuinchos.Servicios.PublicacionServicio;
import com.equipoQ.alquilerQuinchos.Servicios.UsuarioServicio;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 *
 * @author Leo
 */
@Controller
@RequestMapping("/comentario")
public class ComentarioControlador {

    @Autowired
    private ComentarioServicio comentarioServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private PublicacionServicio publicacionServicio;

    @PostMapping("/registrar")
    public String registrar( @RequestParam String usuarioId,@RequestParam String idPublicacion, @RequestParam String texto, ModelMap modelo,
                           HttpServletRequest request) throws MiExcepcion{

        try {
            Usuario usuario = usuarioServicio.getOne(usuarioId);
            Optional<Publicacion> publicacionOptional = publicacionServicio.getOne(idPublicacion);
        // Comprobar si la publicacion es null
        if (!publicacionOptional.isPresent()) {
            // Manejar el caso cuando la publicación es null
            modelo.put("error", "La publicación no existe");
            return "redirect:/";
        }
        if (usuario == null){
            modelo.put("error", "El usuario no existe");
            return "redirect:/";
        }
        Publicacion publicacion = publicacionOptional.get();
            comentarioServicio.crearComentario(usuario, publicacion, texto);
            System.out.println(publicacion.getComentarios());
        } catch (MiExcepcion e) {
            // Log the exception or handle it more gracefully
            modelo.put("error", "no se puede agregar comentario: " + e.getMessage());
        }
        List<Publicacion> publicaciones = publicacionServicio.listarPublicaciones();
        modelo.put("publicaciones", publicaciones);
        return "redirect:/publicacion/mostrar";
    }
       @GetMapping("/registro/{id}")
       public String registrar(@PathVariable String id, ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) {

        Optional<Publicacion> publicacionOptional = publicacionServicio.getOne(id);
        // Comprobar si la publicacion es null
        if (!publicacionOptional.isPresent()) {
            // Manejar el caso cuando la publicación es null
            modelo.put("error", "La publicación no existe");
            return "redirect:/";
        }
        Publicacion publicacion = publicacionOptional.get();
        // Mostrar el formulario de modificación
        modelo.put("publicacion", publicacion);
        
        if (usuario.getRol() == Rol.CLIENTE) {
            modelo.put("usuario", usuario);
            return "comentario.html";
        } else {
            // Si no, mostrar un mensaje de error y redirigir al inicio
            modelo.put("error", "No se puede mostrar el mensaje");
            return "redirect:/";
        }
    }
}

