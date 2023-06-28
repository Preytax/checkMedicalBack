package com.checkmedical.back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_documento;
import com.checkmedical.back.repository.itf_rct_documento;

@Service
public class svc_documento {
   
    @Autowired
    itf_rct_documento repository;

    public List<mdl_documento> getDocumentos() {
        return repository.findAll();
    }

    public mdl_documento getDocumentoById(int idDocumento) {
        return repository.findById(idDocumento);
    }
}
