package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_sede;

public interface itf_rct_sede extends CrudRepository <mdl_sede, Integer>{
    
    //Perfiles
    public List<mdl_sede> findAll();
    
    //Cargo por id
    public mdl_sede findById(int id);
}
