package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_chequeo_medico;

public interface itf_rct_chequeo_medico extends CrudRepository <mdl_chequeo_medico, Integer>{
    
    //Chequeo medicos
    public List<mdl_chequeo_medico> findAll();
    
    //Chequeo medico por id
    public mdl_chequeo_medico findById(int id);

    //Chequeo medico por id de persona
    public List<mdl_chequeo_medico> findAllByIdPersona(int idPersona);
}
