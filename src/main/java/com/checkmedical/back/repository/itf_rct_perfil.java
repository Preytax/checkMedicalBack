package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_perfil;

public interface itf_rct_perfil extends CrudRepository <mdl_perfil, Integer>{
    
    //Perfiles
    public List<mdl_perfil> findAll();
    
    //Perfil por id
    public mdl_perfil findById(int id);
}
