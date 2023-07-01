package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_ambiente;

public interface itf_rct_ambiente extends CrudRepository <mdl_ambiente, Integer>{
    
    //Ambientes
    public List<mdl_ambiente> findAll();
    
    //Ambiente por id
    public mdl_ambiente findById(int id);
}
