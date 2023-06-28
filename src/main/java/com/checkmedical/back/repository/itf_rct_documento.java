package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_documento;

public interface itf_rct_documento extends CrudRepository <mdl_documento, Integer>{
    
    //Documentos
    public List<mdl_documento> findAll();
    
    //Documento por id
    public mdl_documento findById(int id);
}
