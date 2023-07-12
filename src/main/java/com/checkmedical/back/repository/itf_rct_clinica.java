package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_clinica;

public interface itf_rct_clinica extends CrudRepository <mdl_clinica, Integer> {
    //Documentos
    public List<mdl_clinica> findAll();
    
    //Documento por id
    public mdl_clinica findById(int id);
}
