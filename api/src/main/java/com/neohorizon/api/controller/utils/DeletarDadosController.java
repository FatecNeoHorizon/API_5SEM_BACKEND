package com.neohorizon.api.controller.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.controller.comum.BaseController;
import com.neohorizon.api.service.utils.DeletarDadosService;

@RestController
@RequestMapping("/deletar-dados")
public class DeletarDadosController extends BaseController {

    private final DeletarDadosService deletarDadosService;

    @Autowired
    public DeletarDadosController(DeletarDadosService deletarDadosService) {
        this.deletarDadosService = deletarDadosService;
    }

   @PostMapping()
   public ResponseEntity<Boolean> getAllEntities() {
       Boolean isSucesso = deletarDadosService.deleteDados();
       return ok(isSucesso);
   }

}
