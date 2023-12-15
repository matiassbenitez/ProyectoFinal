package com.equipoQ.alquilerQuinchos.Controlador;
import com.equipoQ.alquilerQuinchos.Entidades.Publicacion;
import com.equipoQ.alquilerQuinchos.Entidades.Usuario;
import com.equipoQ.alquilerQuinchos.Enumeraciones.Rol;
import com.equipoQ.alquilerQuinchos.Repositorios.UsuarioRepositorio;
import com.equipoQ.alquilerQuinchos.Servicios.PublicacionServicio;
import com.equipoQ.alquilerQuinchos.Servicios.UsuarioServicio;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import java.util.logging.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private PublicacionServicio publicacionServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

   @GetMapping("/dashboard")
   public String panelAdministrativo(){
       return "panel.html";
   }
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
       modelo.addAttribute("usuarios", usuarios);

        return "usuario_list";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        usuarioServicio.cambiarRol(id);

       return "redirect:/admin/usuarios";
    }

   @GetMapping("/eliminarUsuario/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) {
    Usuario user = usuarioRepositorio.findById(id).get();
    Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
    if (respuesta.isPresent()) {
        usuario = respuesta.get();
        usuario.setAltas(false);
        usuarioRepositorio.save(usuario);
    }
    // Si el usuario es un administrador, no redirige a ninguna otra página
    return "redirect:../usuarios";
}

   @GetMapping("/publicaciones")
    public String listarPublicaciones(ModelMap modelo) {
    List<Publicacion> publicaciones = publicacionServicio.listarPublicaciones();
    modelo.addAttribute("publicaciones", publicaciones);
    return "publicacion_list";
}


}