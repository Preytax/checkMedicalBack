package com.checkmedical.back.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_actividad;
import com.checkmedical.back.services.svc_actividad;

@Controller
@RestController
public class ctl_actividad {
    
    @Autowired
    svc_actividad service;
    
    @GetMapping("/getActividades")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_actividad> getActividades() {
        return service.getActividades();
    }

    @GetMapping("/getActividadById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_actividad getActividad(@PathVariable int id) {
        return service.getActividadById(id);
    }
}
