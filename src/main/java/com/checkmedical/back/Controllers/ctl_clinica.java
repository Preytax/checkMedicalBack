package com.checkmedical.back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.checkmedical.back.Models.mdl_clinica;
import com.checkmedical.back.services.svc_clinica;

@Controller
@RestController
public class ctl_clinica {
    
    @Autowired
    svc_clinica service;

    @GetMapping("/getClinicas")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_clinica> getClinicas() {
        return service.getClinicas();
    }

    @GetMapping("/getClinicaById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_clinica getClinicaById(@PathVariable int id) {
        return service.getClinicaById(id);
    }
}
