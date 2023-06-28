package com.checkmedical.back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_perfil;
import com.checkmedical.back.services.svc_perfil;

@Controller
@RestController
public class ctl_perfil {
    
    @Autowired
    svc_perfil service;

    @GetMapping("/getPerfiles")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_perfil> getPerfiles() {
        return service.getPerfiles();
    }

    @GetMapping("/getPerfilById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_perfil getPerfil(@PathVariable int id) {
        return service.getPerfilById(id);
    }
}
