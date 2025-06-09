package com.tuempresa.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tuempresa.app.service.ConsultaService;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/consultar")
    public Map<String, Object> consultar(@RequestParam String ruc, @RequestParam String placa) {
        return consultaService.obtenerInformacion(ruc, placa);
    }
}
