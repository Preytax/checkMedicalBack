package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_clinica;
import com.checkmedical.back.repository.itf_rct_clinica;

import jakarta.transaction.Transactional;

@Service
public class svc_clinica {
    @Autowired
    itf_rct_clinica repository;

    public List<mdl_clinica> getClinicas() {
        return repository.findAll();
    }

    public List<mdl_clinica> getClinicasByEstadoIn(List<Integer> estado) {
        return repository.findAllByEstadoIn(estado);
    }

    public mdl_clinica getClinicaById(int idClinica) {
        return repository.findById(idClinica);
    }

    @Transactional
    public Boolean eliminarClinica(int id) {
        repository.eliminarClinica(id);
        return true;
    }

    @Transactional
    public Boolean habilitarClinica(int id) {
        repository.habilitarClinica(id);
        return true;
    }

    @Transactional
    public Boolean deshabilitarClinica(int id) {
        repository.deshabilitarClinica(id);
        return true;
    }

    public Boolean saveClinica(mdl_clinica request) {
        try {
            repository.save(request);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
