package com.checkmedical.back.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.checkmedical.back.Models.mdl_persona;
import com.checkmedical.back.repository.itf_rct_persona;

@Service
public class svc_persona {
    
    @Autowired
    itf_rct_persona repository;

    public List<mdl_persona> singIn(String correo, String password) {
        return repository.findByCorreoAndPasswordAndEstado(correo, password, 1);
    }

    public List<mdl_persona> getPersonasByIdPerfilAndEstadoIn(int id_perfil, List<Integer> estado) {
        return repository.findAllByIdPerfilAndEstadoIn(id_perfil, estado);
    }

    public List<mdl_persona> getPersonasByIdPerfilInAndEstadoInAndUsuarioRegistra(List<Integer> id_perfil, List<Integer> estado, int usuario_registra) {
        return repository.findAllByIdPerfilInAndEstadoInAndUsuarioRegistra(id_perfil, estado, usuario_registra);
    }

    public mdl_persona getPersonaById(int id) {
        Optional<mdl_persona> opt = repository.findById(id);

        if(opt.isPresent()) {
            return opt.get();
        }else{
            return new mdl_persona();
        }
    }

    public Boolean savePersona(mdl_persona request) {
        try {
            repository.save(request);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Boolean updatePersona(mdl_persona request) {
        try {
            repository.save(request);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean confirmarNroDocumento(String correo) {
        return repository.findByNroDocumento(correo);
    }

    public boolean confirmarCorreo(String  dni) {
        return repository.findByCorreo(dni);
    }
}
