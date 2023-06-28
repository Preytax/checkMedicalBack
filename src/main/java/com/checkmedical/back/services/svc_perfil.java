package com.checkmedical.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.checkmedical.back.Models.mdl_perfil;
import com.checkmedical.back.repository.itf_rct_perfil;

@Service
public class svc_perfil {
   
    @Autowired
    itf_rct_perfil repository;

    public List<mdl_perfil> getPerfiles() {
        return repository.findAll();
    }

    public mdl_perfil getPerfilById(int idPerfil) {
        return repository.findById(idPerfil);
    }
}
