package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_sede;
import com.checkmedical.back.repository.itf_rct_sede;

@Service
public class svc_sede {
    
    @Autowired
    itf_rct_sede repository;

    public List<mdl_sede> getSedes() {
        return repository.findAll();
    }

    public mdl_sede getSedeById(int idSede) {
        return repository.findById(idSede);
    }
}
