package com.checkmedical.back.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.checkmedical.back.Models.mdl_persona;

public interface itf_rct_persona extends CrudRepository <mdl_persona, Integer> {
   
    // Deshabilitar persona
    @Modifying
    @Query("UPDATE mdl_persona t SET t.estado = 0 WHERE t.id = :id")
    public void deshabilitarPersona(int id);

    // Habilitar persona
    @Modifying
    @Query("UPDATE mdl_persona t SET t.estado = 1 WHERE t.id = :id")
    public void habilitarPersona(int id);

    // Eliminar persona
    @Modifying
    @Query("UPDATE mdl_persona t SET t.estado = 2 WHERE t.id = :id")
    public void eliminarPersona(int id);

    // Personas por correo
    public Boolean findByCorreo(String correo);

    // Personas por nro de documentos
    public Boolean findByNroDocumento(String nroDocumento);

    // Personas por tipo de perfil y estado
    public List<mdl_persona> findAllByIdPerfilAndEstadoIn(int id_perfil, List<Integer> estado);

    // Confirmacion de login
    public List<mdl_persona> findByCorreoAndPasswordAndEstado(String correo, String password, int estado);

    // Personas por tipo de perfil, estado y usuario que registro
    public List<mdl_persona> findAllByIdPerfilInAndEstadoInAndUsuarioRegistra(List<Integer> id_perfil,
            List<Integer> estado, int usuarioRegistra);
}