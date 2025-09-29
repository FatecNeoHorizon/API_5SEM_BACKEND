// src/main/java/com/neohorizon/api/controller/FatoCustoHoraController.java
package com.neohorizon.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.dto.CustoHorasPorDevDTO;
import com.neohorizon.api.dto.CustoPorProjetoDTO;
import com.neohorizon.api.dto.CustoTotalDTO;
import com.neohorizon.api.dto.EvolucaoCustoDTO;
import com.neohorizon.api.dto.FatoCustoHoraDTO;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.service.FatoCustoHoraService;

@RestController
@RequestMapping("/fato-custo-hora")
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
    public ResponseEntity<List<EvolucaoCustoDTO>> getEvolucao(
            @RequestParam(defaultValue = "mes") String granularidade) {
        return ResponseEntity.ok(service.obterEvolucao(granularidade));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FatoCustoHoraDTO>> getAllEntitiesByFilter(
            @RequestParam("projeto_id") Long projetoId,
            @RequestParam("periodo_id") Long periodoId,
            @RequestParam("dev_id") Long devId) {

        DimProjeto dimProjeto = new DimProjeto();
        DimPeriodo dimPeriodo = new DimPeriodo();
        DimDev dimDev = new DimDev();

        dimProjeto.setId(projetoId);
        dimPeriodo.setId(periodoId);
        dimDev.setId(devId);

        if (periodoId == 0) {
            dimPeriodo = null;
        }

        if (projetoId == 0) {
            dimProjeto = null;
        }

        if (devId == 0) {
            dimDev = null;
        }

        List<FatoCustoHoraDTO> fatoCustoHoraDTOs = service.getAllEntitiesByFilter(dimProjeto, dimPeriodo, dimDev);
        return ResponseEntity.ok(fatoCustoHoraDTOs);
    }

    @GetMapping
    public ResponseEntity<List<FatoCustoHoraDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<FatoCustoHoraDTO> fatoCustoHoraDTOs = service.getAllEntities();
        return ResponseEntity.ok(fatoCustoHoraDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoCustoHoraDTO> getFatoCustoHoraById(@PathVariable Long id) {
        FatoCustoHoraDTO fatoCustoHoraDTO = service.findById(id);
        if (fatoCustoHoraDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fatoCustoHoraDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<FatoCustoHoraDTO> addFatoCustoHora(@RequestBody FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHoraDTO createdEntity = service.save(fatoCustoHoraDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoCustoHoraDTO> updateFatoCustoHora(@PathVariable Long id,
            @RequestBody FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHoraDTO updatedEntity = service.update(id, fatoCustoHoraDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoCustoHora(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
