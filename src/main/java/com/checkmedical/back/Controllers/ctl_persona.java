package com.checkmedical.back.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_persona;
import com.checkmedical.back.services.svc_persona;

@Controller
@RestController
public class ctl_persona {
    
    @Autowired
    svc_persona service;

    @GetMapping("/getPersonas/{id_perfil}/{estado}")
    @ResponseStatus(HttpStatus.CREATED)
    List<mdl_persona> getPersonas(@PathVariable int id_perfil, @PathVariable List<Integer> estado) {

        List<mdl_persona> listPersonas = null;
        listPersonas = service.getPersonasByIdPerfilAndEstadoIn(id_perfil, estado);

        return listPersonas;
    }

    @GetMapping("/singIn/{correo}/{password}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_persona>  singIn(@PathVariable String correo, @PathVariable String password) {
        return service.singIn(correo, password);
    }

    @GetMapping("/getPersonaById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_persona getPersona(@PathVariable int id) {
        return service.getPersonaById(id);
    }

    @PostMapping("/savePersona")
    @ResponseStatus(HttpStatus.CREATED)
    String savePersona(@RequestBody mdl_persona persona) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            !persona.getNombres().equals("")            && !persona.getNombres().isEmpty()          &&
            !persona.getApellidoPaterno().equals("")    && !persona.getApellidoPaterno().isEmpty()  &&
            !persona.getApellidoMaterno().equals("")    && !persona.getApellidoMaterno().isEmpty()  &&
            !persona.getTipoDocumento().equals("")      && !persona.getTipoDocumento().isEmpty()    &&
            !persona.getNroDocumento().equals("")       && !persona.getNroDocumento().isEmpty()     &&
            !persona.getFechaNacimiento().equals("")    && !persona.getFechaNacimiento().isEmpty()  &&
            !persona.getCorreo().equals("")             && !persona.getCorreo().isEmpty()           &&
            !persona.getPassword().equals("")           && !persona.getPassword().isEmpty()
            ) 
        {
            mensaje = "ER|No se pudo registrar a la persona.";

            /*if(service.confirmarCorreo(persona.getCorreo()) == false){
                return "ER|El correo ya esta registrado.";
            }
            else if(service.confirmarNroDocumento(persona.getNroDocumento()) == false){
                return "ER|El Nro de docuemento ya esta registrado.";
            }*/

            persona.setUsuarioRegistra(persona.getId());
            persona.setEstado(1);
            persona.setIpRegistra(persona.capturarIp());

            if (service.savePersona(persona)) {
                mensaje = "OK|Se registro a la persona con exito.";
            }
        }

        return mensaje;
    }
}
