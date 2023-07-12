package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_chequeo_medico;
import com.checkmedical.back.repository.itf_rct_chequeo_medico;

@Service
public class svc_chequeo_medico {

    @Autowired
    itf_rct_chequeo_medico repository;

    public List<mdl_chequeo_medico> getChequeoMedicos() {
        return repository.findAll();
    }

    public mdl_chequeo_medico getChequeoMedicoById(int idChequeoMedico) {
        return repository.findById(idChequeoMedico);
    }

    public Boolean saveChequeoMedico(mdl_chequeo_medico chequeo_medico) {
        try {
            repository.save(chequeo_medico);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
