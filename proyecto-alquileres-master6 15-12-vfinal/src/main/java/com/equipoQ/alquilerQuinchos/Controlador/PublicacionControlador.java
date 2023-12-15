package com.equipoQ.alquilerQuinchos.Controlador;

import com.equipoQ.alquilerQuinchos.Entidades.*;
import com.equipoQ.alquilerQuinchos.Enumeraciones.*;
import com.equipoQ.alquilerQuinchos.Excepciones.*;
import com.equipoQ.alquilerQuinchos.Repositorios.*;
import com.equipoQ.alquilerQuinchos.Servicios.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/publicacion")
public class PublicacionControlador {

    @Autowired
    private PublicacionServicio publicacionServicio;
    @Autowired
    private PublicacionRepositorio publicacionRepositorio;
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) {
        if (usuario.getRol() == Rol.PROPIETARIO) {
            modelo.put("usuario", usuario);
            return "publicacion_form.html";
        } else {
            modelo.put("error", "Solo los agentes pueden registrar propiedades");
            return "redirect:/";
        }
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String usuarioId, @RequestParam String direccion, @RequestParam String localidad,
                           @RequestParam String provincia, @RequestParam Float precio_base, @RequestParam(required = false) Boolean wifi, 
                           @RequestParam(required = false) Boolean pileta, @RequestParam Integer capacidad, @RequestParam String titulo, 
                           @RequestParam String descripcion, @RequestParam String categoria, @RequestParam MultipartFile[] archivos, 
                           @RequestParam String diasDisponibles, ModelMap modelo) {
        try {
            Usuario usuario = usuarioServicio.getOne(usuarioId);

              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
              List<LocalDate> diasDisponiblesList = Arrays.stream(diasDisponibles.split(","))
                  .map(date -> LocalDate.parse(date.trim(), formatter))
                  .collect(Collectors.toList());

            publicacionServicio.crearPublicacion(usuario, direccion, localidad, provincia, precio_base, wifi, pileta, capacidad, titulo, descripcion, categoria, archivos, diasDisponiblesList);

            modelo.put("Exito", "La publicación fue cargada correctamente");
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "publicacion_form.html";
        }
        return "redirect:/";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        // Obtener el usuario actual de la sesión
        Usuario usuario = (Usuario) modelo.get("usuariosession");
        // Obtener la publicacion por su ID
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
        return "publicacion_modify.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String direccion, String localidad, String provincia, Float precio_base, Boolean wifi, Boolean pileta, Integer capacidad, String titulo, String descripcion, String categoria/*, MultipartFile[] archivos*/, ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) throws MiExcepcion {
        try {
            if (wifi == null){
                wifi = false;
             }
             if (pileta == null){
                pileta = false;
             }
            publicacionServicio.modificarPublicacion(usuario, id, direccion, localidad, provincia, precio_base, wifi, pileta, capacidad, titulo, descripcion, categoria/*, archivos*/);
            return "redirect:../listar";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "publicacion_modify.html";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) {
    Publicacion publicacion = publicacionRepositorio.findById(id).get();
    Optional<Publicacion> respuesta = publicacionRepositorio.findById(id);
    if (respuesta.isPresent()) {
        publicacion = respuesta.get();
        publicacion.setAltas(false);
        publicacionRepositorio.save(publicacion);
    }
    // Si el usuario es un administrador, no redirige a ninguna otra página
    if (usuario.getRol() == Rol.ADMIN) {
        return "redirect:/admin/publicaciones";
    }
    return "redirect:../listar";
}

    @GetMapping("/listar")
    public String listar(ModelMap modelo, String id, @SessionAttribute("usuariosession") Usuario usuario) {
        List<Publicacion> publicaciones = publicacionServicio.listarPublicacionesPorMail(usuario);    
        if (usuario != null) {
            List<Publicacion> publicacionesEnAlta = new ArrayList<>();
            for (Publicacion publicacion : publicaciones) {
                if (publicacion.getAltas()) {
                    publicacionesEnAlta.add(publicacion);
                }
            }
            modelo.addAttribute("publicaciones", publicacionesEnAlta);
            return "publicacion_list";
        } else {  
            return "redirect:/login";
        }
    }    
        
     @GetMapping("/mostrar")
    public String mostrar(ModelMap modelo, String id, @SessionAttribute("usuariosession") Usuario usuario) {
        List<Publicacion> publicaciones = publicacionRepositorio.findAll();    
        if (usuario != null) {
            List<Publicacion> publicacionesEnAlta = new ArrayList<>();
            for (Publicacion publicacion : publicaciones) {
                if (publicacion.getAltas()) {
                    publicacionesEnAlta.add(publicacion);
                }
            }
            modelo.addAttribute("publicaciones", publicacionesEnAlta);
            return "publicacion_list";
        } else {  
            return "redirect:/login";
        }
    }
    
    @PostMapping("/listarPublis")
    public String listar(ModelMap modelo, String provincia, String categoria) {
        if ("null".equals(provincia)) {
            provincia = null;
        }
        if ("null".equals(categoria)) {
            categoria = null;
        }
        List<Publicacion> publicaciones = publicacionServicio.listarPublicacionesFiltro(provincia, categoria);

        List<Publicacion> publicacionesEnAlta = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            if (publicacion.getAltas()) {
                publicacionesEnAlta.add(publicacion);
            }
        }

        modelo.addAttribute("publicaciones", publicacionesEnAlta);
        return "publicacion_list";

    }
    
    @GetMapping("/darDeAlta/{id}")
    public String darDeAlta(@PathVariable String id, ModelMap modelo, @SessionAttribute("usuariosession") Usuario usuario) {
        Publicacion publicacion = publicacionRepositorio.findById(id).get();
        Optional<Publicacion> respuesta = publicacionRepositorio.findById(id);
        if (respuesta.isPresent()) {
            publicacion = respuesta.get();
            publicacion.setAltas(true);
            publicacionRepositorio.save(publicacion);
        }
        // Si el usuario es un administrador, no redirige a ninguna otra página
        if (usuario.getRol() == Rol.ADMIN) {
            return "redirect:/admin/publicaciones";
        }
        return "redirect:../listar";
    }
    
    
}
