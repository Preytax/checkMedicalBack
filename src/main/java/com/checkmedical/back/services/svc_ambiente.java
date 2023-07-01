package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_ambiente;
import com.checkmedical.back.repository.itf_rct_ambiente;

@Service
public class svc_ambiente {
    
    @Autowired
    itf_rct_ambiente repository;

    public List<mdl_ambiente> getAmbientes() {
        return repository.findAll();
    }

    public mdl_ambiente getAmbineteById(int idActividad) {
        return repository.findById(idActividad);
    }

    public Boolean saveAmbiente(mdl_ambiente ambiente) {
        try {
            repository.save(ambiente);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
