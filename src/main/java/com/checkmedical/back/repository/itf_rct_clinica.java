package com.checkmedical.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_clinica;

public interface itf_rct_clinica extends CrudRepository <mdl_clinica, Integer> {
    //Documentos
    public List<mdl_clinica> findAll();
    
    //Documento por id
    public mdl_clinica findById(int id);

    //Documento por id
    public List<mdl_clinica> findAllByEstadoIn(List<Integer> estado);

    // Eliminar clinica
    @Modifying
    @Query("UPDATE mdl_clinica t SET t.estado = 2 WHERE t.id = :id")
    public void eliminarClinica(int id);

    // Habilitar clinica
    @Modifying
    @Query("UPDATE mdl_clinica t SET t.estado = 1 WHERE t.id = :id")
    public void habilitarClinica(int id);

    // Deshabilitar clinica
    @Modifying
    @Query("UPDATE mdl_clinica t SET t.estado = 0 WHERE t.id = :id")
    public void deshabilitarClinica(int id);
}
