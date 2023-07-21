package com.checkmedical.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_cita;

public interface itf_rct_cita extends CrudRepository <mdl_cita, Integer>{
    
    //Recordatorios
    public List<mdl_cita> findAll();

    // Personas por correo
    public List<mdl_cita> findAllByEstadoIn(List<Integer> estado);
    
    //Recordatorio por id
    public mdl_cita findById(int id);

    //Recordatorios por persona
    public List<mdl_cita> findAllByIdPersonaAndEstado(int idPersona, int estado);

    // Eliminar recordatorio
    @Modifying
    @Query("UPDATE mdl_recordatorio t SET t.estado = 2 WHERE t.id = :id")
    public void eliminarRecordatorio(int id);

    // Eliminar recordatorio
    @Modifying
    @Query("UPDATE mdl_recordatorio t SET t.estado = 4 WHERE t.id = :id")
    public void confirmarRecordatorio(int id);
}
