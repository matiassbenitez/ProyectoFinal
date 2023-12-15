/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoQ.alquilerQuinchos.Servicios;

import com.equipoQ.alquilerQuinchos.Entidades.Comentario;
import com.equipoQ.alquilerQuinchos.Entidades.Publicacion;
import com.equipoQ.alquilerQuinchos.Entidades.Usuario;
import com.equipoQ.alquilerQuinchos.Excepciones.MiExcepcion;
import com.equipoQ.alquilerQuinchos.Repositorios.ComentarioRepositorio;
import com.equipoQ.alquilerQuinchos.Repositorios.PublicacionRepositorio;
import com.equipoQ.alquilerQuinchos.Repositorios.UsuarioRepositorio;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

/**
 *
 * @author Leo
 */

@Service
public class ComentarioServicio {
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    @Autowired
    private PublicacionRepositorio publicacionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private PublicacionServicio publicacionServicio;

    @Transactional
    public void crearComentario(Usuario usuario, Publicacion publicacion, String texto) throws MiExcepcion {

        Comentario comentario = new Comentario();
        comentario.setFecha(new Date());
        comentario.setTexto(texto);
        comentario.setUsuario(usuario);
        //comentario.setPublicacion(publicacion);
        comentario = comentarioRepositorio.save(comentario);
        publicacion.getComentarios().add(comentario);
        publicacionRepositorio.save(publicacion);
    }
}
