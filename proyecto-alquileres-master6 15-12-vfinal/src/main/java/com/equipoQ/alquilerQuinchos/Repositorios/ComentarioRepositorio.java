/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoQ.alquilerQuinchos.Repositorios;

import com.equipoQ.alquilerQuinchos.Entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leo
 */
@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String>{
}
