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
import com.checkmedical.back.Models.mdl_ambiente;
import com.checkmedical.back.services.svc_ambiente;

@Controller
@RestController
public class ctl_ambiente {
    
    @Autowired
    svc_ambiente service;
    
    @GetMapping("/getAmbientes")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_ambiente> getAmbientes() {
        return service.getAmbientes();
    }

    @GetMapping("/getAmbineteById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_ambiente getAmbineteById(@PathVariable int id) {
        return service.getAmbineteById(id);
    }

    @PostMapping("/saveAmbiente")
    @ResponseStatus(HttpStatus.CREATED)
    String saveRecordatorio(@RequestBody mdl_ambiente ambiente) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            !ambiente.getAmbiente().equals("")
            ) 
        {
            mensaje = "ER|No se pudo registrar el ambiente.";

            ambiente.setEstado(1);
            ambiente.setIpRegistra(ambiente.capturarIp());

            if (service.saveAmbiente(ambiente)) {
                mensaje = "OK|Se registro el ambiente con exito.";
            }
        }

        return mensaje;
    }
}
