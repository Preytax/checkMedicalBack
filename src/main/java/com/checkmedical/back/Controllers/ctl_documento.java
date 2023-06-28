package com.checkmedical.back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_documento;
import com.checkmedical.back.services.svc_documento;

@Controller
@RestController
public class ctl_documento {
    
    @Autowired
    svc_documento service;

    @GetMapping("/getDocumentos")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_documento> getDocumentos() {
        return service.getDocumentos();
    }

    @GetMapping("/getDocumentoById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_documento getActividad(@PathVariable int id) {
        return service.getDocumentoById(id);
    }
}
