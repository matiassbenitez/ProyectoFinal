package com.equipoQ.alquilerQuinchos.Controlador;
import com.equipoQ.alquilerQuinchos.Servicios.*;
import com.equipoQ.alquilerQuinchos.Entidades.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
@Autowired
UsuarioServicio usuarioServicio;
@Autowired
ImagenServicio imagenServicio;
   @GetMapping("/perfil/{id}")
      public ResponseEntity<byte[]>imagenUsuario(@PathVariable String id){
         Usuario usuario = usuarioServicio.getOne(id);
         byte[] imagen =usuario.getImagen().getContenido();
      //   con este http le decimos que lo que estamos devolviendo es una imagen entonces al header hay que setearle la img
         HttpHeaders headers =new HttpHeaders();
      //   con mediatype se elige el tipo de contenido
         headers.setContentType(MediaType.IMAGE_JPEG);
         return new ResponseEntity<>(imagen,headers, HttpStatus.OK);
      }

   @GetMapping("/{id}")
      public ResponseEntity<byte[]>mostrarImagen(@PathVariable String id){
         Imagen imagen = imagenServicio.getOne(id).get();
         byte[] image =imagen.getContenido();
      //   con este http le decimos que lo que estamos devolviendo es una imagen entonces al header hay que setearle la img
         HttpHeaders headers =new HttpHeaders();
      //   con mediatype se elige el tipo de contenido
         headers.setContentType(MediaType.IMAGE_JPEG);
         return new ResponseEntity<>(image,headers, HttpStatus.OK);
      }
}