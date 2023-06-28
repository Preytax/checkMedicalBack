package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_cargo;

public interface itf_rct_cargo extends CrudRepository <mdl_cargo, Integer>{
    
    //Cargos
    public List<mdl_cargo> findAll();
    
    //Cargo por id
    public mdl_cargo findById(int id);
}
