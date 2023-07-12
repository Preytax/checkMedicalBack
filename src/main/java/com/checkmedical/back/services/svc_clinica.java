package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_clinica;
import com.checkmedical.back.repository.itf_rct_clinica;

@Service
public class svc_clinica {
    @Autowired
    itf_rct_clinica repository;

    public List<mdl_clinica> getClinicas() {
        return repository.findAll();
    }

    public mdl_clinica getClinicaById(int idClinica) {
        return repository.findById(idClinica);
    }
}
