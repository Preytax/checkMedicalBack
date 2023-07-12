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

import com.checkmedical.back.Models.mdl_chequeo_medico;
import com.checkmedical.back.services.svc_chequeo_medico;

@Controller
@RestController
public class ctl_chequeo_medico {
    @Autowired
    svc_chequeo_medico service;
    
    @GetMapping("/getChequeoMedicos")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_chequeo_medico> getChequeoMedicos() {
        return service.getChequeoMedicos();
    }

    @GetMapping("/getChequeoMedicoById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_chequeo_medico getChequeoMedicoById(@PathVariable int id) {
        return service.getChequeoMedicoById(id);
    }

    @PostMapping("/saveChequeoMedico")
    @ResponseStatus(HttpStatus.CREATED)
    String ChequeoMedico(@RequestBody mdl_chequeo_medico chequeoMedico) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            chequeoMedico.getIdPersona() != 0 &&
            !chequeoMedico.getFechaEmision().equals("") &&
            !chequeoMedico.getFechaVencimiento().equals("")
            ) 
        {
            mensaje = "ER|No se pudo registrar el chequeo medico.";

            chequeoMedico.setResultado(1);
            chequeoMedico.setIpRegistra(chequeoMedico.capturarIp());

            if (service.saveChequeoMedico(chequeoMedico)) {
                mensaje = "OK|Se registro el chequeo medico con exito.";
            }
        }

        return mensaje;
    }
}
