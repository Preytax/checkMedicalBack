package com.checkmedical.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_recordatorio;

public interface itf_rct_recordatorio extends CrudRepository <mdl_recordatorio, Integer>{
    
    //Recordatorios
    public List<mdl_recordatorio> findAll();

    // Personas por correo
    public List<mdl_recordatorio> findAllByEstado(int estado);
    
    //Recordatorio por id
    public mdl_recordatorio findById(int id);

    //Recordatorios por persona
    public List<mdl_recordatorio> findAllByIdPersona(int idPersona);

    // Eliminar persona
    @Modifying
    @Query("UPDATE mdl_recordatorio t SET t.estado = 2 WHERE t.id = :id")
    public void eliminarRecordatorio(int id);
}
