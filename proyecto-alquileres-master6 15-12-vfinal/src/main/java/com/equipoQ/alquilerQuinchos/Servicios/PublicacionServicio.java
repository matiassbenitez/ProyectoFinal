package com.equipoQ.alquilerQuinchos.Servicios;
import com.equipoQ.alquilerQuinchos.Excepciones.*;
import com.equipoQ.alquilerQuinchos.Entidades.*;
import com.equipoQ.alquilerQuinchos.Repositorios.*;
import java.time.*;
import java.util.*;
import static java.util.Collections.list;
import static java.util.Collections.newSetFromMap;

import javax.transaction.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.MultipartFile;
@Service
public class PublicacionServicio {

   @Autowired
   private PublicacionRepositorio publicacionRepositorio;
   @Autowired
   private UsuarioRepositorio usuarioRepositorio;
   @Autowired
   private ImagenServicio imagenServicio;

   @Transactional
   public void crearPublicacion(Usuario usuario, String direccion, String localidad, String provincia, Float precio_base, Boolean wifi, Boolean pileta, Integer capacidad, String titulo, String descripcion, String categoria, MultipartFile[] archivos, List<LocalDate> diasDisponibles) throws MiExcepcion {
      if (wifi == null){
         wifi = false;
      }
      if (pileta == null){
         pileta = false;
      }
      try {
      validar(usuario, direccion, localidad, provincia, precio_base, wifi, pileta, capacidad, titulo, descripcion, categoria/*, archivos*/); //VER SI VA COMENTARIOS

      Publicacion publicacion = new Publicacion();

      publicacion.setAltas(true);
      publicacion.setDireccion(direccion);
      publicacion.setAlta(new Date());
      publicacion.setLocalidad(localidad);
      publicacion.setProvincia(provincia);
      publicacion.setUsuario(usuario);
      publicacion.setPrecio_base(precio_base);
      publicacion.setWifi(wifi);
      publicacion.setPileta(pileta);
      publicacion.setCapacidad(capacidad);
      publicacion.setTitulo(titulo);
      publicacion.setDescripcion(descripcion);
      publicacion.setCategoria(categoria);
      publicacion.setDiasDisponibles(diasDisponibles);

      List<Imagen> imagenes = new ArrayList();  //SE CREA EL ARRAY DE IMAGENES
      for (MultipartFile archivo : archivos) {  //SE RECORRE EL ARRAY DE ARCHIVOS RECIBIDOS DESDE EL FORM
         imagenes.add(imagenServicio.guardar(archivo));  //SE TRANSFORMAN LOS ARCHIVOS A IMAGENES
      }
      publicacion.setImagenes(imagenes);
      publicacionRepositorio.save(publicacion);
      usuario.getPublicaciones().add(publicacion);
      usuarioRepositorio.save(usuario);
      } catch (MiExcepcion ex) {
      // Lanzar la excepción para que sea capturada por el bloque catch en el método registro
      throw ex;
   }
   }

   public List<Publicacion> listarPublicaciones() {
      List<Publicacion> publicaciones = new ArrayList();
      publicaciones = publicacionRepositorio.findAll();
      return publicaciones;
   }

   public List<Publicacion> listarPublicacionesPorMail(Usuario u){
      String email= u.getEmail();
      List<Publicacion> publicaciones = new ArrayList();
      publicaciones = publicacionRepositorio.buscarPorEmailDeUsuario(email);
      return publicaciones;  
  }

  public List<Publicacion> listarPublicacionesFiltro(String provincia, String categoria) {
   List<Publicacion> publicaciones = new ArrayList();
   publicaciones = publicacionRepositorio.buscarPublicaciones(provincia, categoria);
   return publicaciones;
}

   public void modificarPublicacion(Usuario usuario, String id, String direccion, String localidad, String provincia, Float precio_base, Boolean wifi, Boolean pileta, Integer capacidad, String titulo, String descripcion, String categoria /*, MultipartFile[] archivos*/) throws MiExcepcion {
      //Publicacion publicacion = publicacionRepositorio.findById(id).get();
      
      Publicacion publicacion = publicacionRepositorio.findById(id).orElseThrow(() -> new MiExcepcion("La publicación no existe")); //VER SI HAY QUE AGREGAR UN TRY CATCH
      
      validar(usuario, direccion, localidad, provincia, precio_base, wifi, pileta, capacidad, titulo, descripcion, categoria/*, archivos*/);
      Optional<Publicacion> respuesta = publicacionRepositorio.findById(id);
      
      if (respuesta.isPresent()) {
         //publicacion = respuesta.get();
         publicacion.setDireccion(direccion);
         publicacion.setLocalidad(localidad);
         publicacion.setProvincia(provincia);
         publicacion.setPrecio_base(precio_base);
         publicacion.setWifi(wifi);
         publicacion.setPileta(pileta);
         publicacion.setCapacidad(capacidad);
         publicacion.setTitulo(titulo);
         publicacion.setDescripcion(descripcion);
         publicacion.setCategoria(categoria);
         //publicacion.setComentarios(comentarios); //VER SI VA
         //List<Imagen> imagenes = new ArrayList();
         //for (MultipartFile archivo : archivos) {
            //imagenes.add(imagenServicio.guardar(archivo));
         //}
         //publicacion.setImagenes(imagenes);
         publicacionRepositorio.save(publicacion);
         System.out.println("***********************************");
         System.out.println(id);
         System.out.println("***********************************");
         }
      }
      //se le agrego un optional 
   public Optional<Publicacion> getOne(String id) {
      return publicacionRepositorio.findById(id);
   }

   public void eliminar(String id) {
      publicacionRepositorio.deleteById(id);
   }

   private void validar(Usuario usuario, String direccion, String localidad, String provincia, Float precio_base, Boolean wifi, Boolean pileta, Integer capacidad, String titulo, String descripcion, String categoria/*, MultipartFile[] imagenes*/) throws MiExcepcion {
      if (usuario == null) {
         throw new MiExcepcion("El propietario no puede ser nulo");
      }

      if (direccion == null) {
         throw new MiExcepcion("La direccion no puede ser nulo");
      }

      if (localidad == null) {
         throw new MiExcepcion("La localidad no puede ser nula");
      }

      if (provincia == null) {
         throw new MiExcepcion("La provincia no puede ser nula");
      }
/*
      if (imagenes == null) {
         throw new MiExcepcion("Las imágenes no pueden ser nulas");
      }*/
      if (titulo == null) {
         throw new MiExcepcion("El titulo no puede ser nulo");
      }
      if (descripcion == null) {
         throw new MiExcepcion("La descripción no puede ser nula");
      }
      if (categoria == null) {
         throw new MiExcepcion("La categoría no puede ser nula");
      }

   }
}
