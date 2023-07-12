package com.checkmedical.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_recordatorio;
import com.checkmedical.back.repository.itf_rct_recordatorio;

import jakarta.transaction.Transactional;

@Service
public class svc_recordatorio {
    
    @Autowired
    itf_rct_recordatorio repository;

    public List<mdl_recordatorio> getRecordatorios() {
        return repository.findAll();
    }

    public List<mdl_recordatorio> getRecordatorios(int estado) {
        return repository.findAllByEstado(estado);
    }
    
    public mdl_recordatorio getRecordatorioById(int id) {
        return repository.findById(id);
    }

    public List<mdl_recordatorio> getRecordatoriosByIdPersona(int idPersona) {
        return repository.findAllByIdPersona(idPersona);
    }

    @Transactional
    public Boolean eliminarRecordatorio(int id) {
        repository.eliminarRecordatorio(id);
        return true;
    }

    public Boolean saveRecordatorio(mdl_recordatorio request) {
        try {
            repository.save(request);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
