// src/main/java/com/neohorizon/api/controller/FatoCustoHoraController.java
package com.neohorizon.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.dto.CustoHorasPorDevDTO;
import com.neohorizon.api.dto.CustoPorProjetoDTO;
import com.neohorizon.api.dto.CustoTotalDTO;
import com.neohorizon.api.dto.EvolucaoCustoDTO;
import com.neohorizon.api.service.FatoCustoHoraService;

@RestController
@RequestMapping("/fato-custo-hora")
@CrossOrigin(origins = "http://localhost:3000")
public class FatoCustoHoraController {

    private final FatoCustoHoraService service;

    @Autowired
    public FatoCustoHoraController(FatoCustoHoraService service) {
        this.service = service;
    }

    @GetMapping("/total")
    public ResponseEntity<CustoTotalDTO> getTotalGeral() {
        return ResponseEntity.ok(service.obterTotalGeral());
    }

    @GetMapping("/total-por-projeto")
    public ResponseEntity<List<CustoPorProjetoDTO>> getTotalPorProjeto() {
        return ResponseEntity.ok(service.obterTotalPorProjeto());
    }

    @GetMapping("/por-dev")
    public ResponseEntity<List<CustoHorasPorDevDTO>> getTotalPorDev() {
        return ResponseEntity.ok(service.obterTotalPorDev());
    }

    @GetMapping("/evolucao")
    public ResponseEntity<List<EvolucaoCustoDTO>> getEvolucao(@RequestParam(defaultValue = "mes") String granularidade) {
        return ResponseEntity.ok(service.obterEvolucao(granularidade));
    }
}
