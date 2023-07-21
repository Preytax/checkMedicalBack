package com.checkmedical.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_cita;
import com.checkmedical.back.repository.itf_rct_cita;

import jakarta.transaction.Transactional;

@Service
public class svc_cita {
    
    @Autowired
    itf_rct_cita repository;

    public List<mdl_cita> getRecordatorios() {
        return repository.findAll();
    }

    public List<mdl_cita> getRecordatorios(List<Integer> estado) {
        return repository.findAllByEstadoIn(estado);
    }
    
    public mdl_cita getRecordatorioById(int id) {
        return repository.findById(id);
    }

    public List<mdl_cita> getRecordatoriosByIdPersonaAndEstado(int idPersona, int estado) {
        return repository.findAllByIdPersonaAndEstado(idPersona, estado);
    }

    @Transactional
    public Boolean eliminarRecordatorio(int id) {
        repository.eliminarRecordatorio(id);
        return true;
    }

    @Transactional
    public Boolean confirmarRecordatorio(int id) {
        repository.confirmarRecordatorio(id);
        return true;
    }

    public Boolean saveRecordatorio(mdl_cita request) {
        try {
            repository.save(request);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
