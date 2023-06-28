package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_actividad;

public interface itf_rct_actividad extends CrudRepository <mdl_actividad, Integer>{
    
    //Actividades
    public List<mdl_actividad> findAll();
    
    //Actividad por id
    public mdl_actividad findById(int id);
}
