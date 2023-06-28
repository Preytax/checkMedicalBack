package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_recordatorio;

public interface itf_rct_recordatorio extends CrudRepository <mdl_recordatorio, Integer>{
    
    //Recordatorios
    public List<mdl_recordatorio> findAll();
    
    //Recordatorio por id
    public mdl_recordatorio findById(int id);

    //Recordatorios por persona
    public List<mdl_recordatorio> findAllByIdPersona(int idPersona);
}
