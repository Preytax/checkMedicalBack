package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_actividad;
import com.checkmedical.back.repository.itf_rct_actividad;

@Service
public class svc_actividad {
    
    @Autowired
    itf_rct_actividad repository;

    public List<mdl_actividad> getActividades() {
        return repository.findAll();
    }

    public mdl_actividad getActividadById(int idActividad) {
        return repository.findById(idActividad);
    }
}
