package com.checkmedical.back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_sede;
import com.checkmedical.back.services.svc_sede;

@Controller
@RestController
public class ctl_sede {
    @Autowired
    svc_sede service;

    @GetMapping("/getSedes")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_sede> getSedes() {
        return service.getSedes();
    }

    @GetMapping("/getSedeById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_sede getSedeById(@PathVariable int id) {
        return service.getSedeById(id);
    }
}
