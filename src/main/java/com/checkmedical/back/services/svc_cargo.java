package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_cargo;
import com.checkmedical.back.repository.itf_rct_cargo;

@Service
public class svc_cargo {
    
    @Autowired
    itf_rct_cargo repository;

    public List<mdl_cargo> getCargos() {
        return repository.findAll();
    }

    public mdl_cargo getCargoById(int idCargo) {
        return repository.findById(idCargo);
    }
}
