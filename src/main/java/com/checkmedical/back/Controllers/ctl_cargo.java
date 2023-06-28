package com.checkmedical.back.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_cargo;
import com.checkmedical.back.services.svc_cargo;

@Controller
@RestController
public class ctl_cargo {
    
    @Autowired
    svc_cargo service;
    
    @GetMapping("/getCargos")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_cargo> getCargos() {
        return service.getCargos();
    }

    @GetMapping("/getCargoById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_cargo getCargoById(@PathVariable int id) {
        return service.getCargoById(id);
    }
}
