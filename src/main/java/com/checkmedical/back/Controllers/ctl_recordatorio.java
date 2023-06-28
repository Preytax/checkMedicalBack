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
import com.checkmedical.back.Models.mdl_recordatorio;
import com.checkmedical.back.services.svc_recordatorio;

@Controller
@RestController
public class ctl_recordatorio {
    
    @Autowired
    svc_recordatorio service;

    @GetMapping("/getRecordatorios")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_recordatorio> getRecordatorios() {
        return service.getRecordatorios();
    }

    @GetMapping("/getRecordatorioById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_recordatorio getRecordatorio(@PathVariable int id) {
        return service.getRecordatorioById(id);
    }

    @GetMapping("/getRecordatorioByIdPersona/{idPersona}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_recordatorio> getRecordatorioByIdPersona(@PathVariable int idPersona) {
        return service.getRecordatoriosByIdPersona(idPersona);
    }

    @PostMapping("/saveRecordatorio")
    @ResponseStatus(HttpStatus.CREATED)
    String saveRecordatorio(@RequestBody mdl_recordatorio recordatorio) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            recordatorio.getIdActividad() != 0 &&
            recordatorio.getFechaInicio().equals("") &&
            recordatorio.getFechaFin().equals("") &&
            recordatorio.getIdPersona() != 0
            ) 
        {
            mensaje = "ER|No se pudo registrar el recordatorio.";

            recordatorio.setUsuarioRegistra(recordatorio.getId());
            recordatorio.setEstado(1);
            recordatorio.setIpRegistra(recordatorio.capturarIp());

            if (service.saveRecordatorio(recordatorio)) {
                mensaje = "OK|Se registro el recordatorio con exito.";
            }
        }

        return mensaje;
    }
}
