package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_persona;

public interface itf_rct_persona extends CrudRepository <mdl_persona, Integer> {
   
    //Clientes por correo
    public Boolean findByCorreo(String correo);

    //Clientes por nro de documentos
    public Boolean findByNroDocumento(String nroDocumento);

    //Clientes por tipo de perfil y estado
    public List<mdl_persona> findAllByIdPerfilAndEstadoIn(int id_perfil, List<Integer> estado);

    //Confirmacion de login
    public List<mdl_persona> findByCorreoAndPasswordAndEstado(String correo, String password, int estado);

    //Personas por tipo de perfil, estado y usuario que registro
    public List<mdl_persona> findAllByIdPerfilInAndEstadoInAndUsuarioRegistra(List<Integer> id_perfil, List<Integer> estado, int usuario_registra);

}
