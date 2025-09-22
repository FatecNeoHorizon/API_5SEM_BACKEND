package com.neohorizon.api.controller;

import com.neohorizon.api.dto.FatoCustoHoraDTO;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.service.FatoCustoHoraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/fato-custo-hora")
public class FatoCustoHoraController {

    private final FatoCustoHoraService fatoCustoHoraService;

    @Autowired
    public FatoCustoHoraController(FatoCustoHoraService fatoCustoHoraService) {
        this.fatoCustoHoraService = fatoCustoHoraService;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FatoCustoHoraDTO>> getAllEntitiesByFilter(
        @RequestParam Long projeto_id,
        @RequestParam Long periodo_id,
        @RequestParam Long dev_id) {

        DimProjeto dimProjeto = new DimProjeto();
        DimPeriodo dimPeriodo = new DimPeriodo();
        DimDev dimDev = new DimDev();

       
        dimProjeto.setId(projeto_id);
        dimPeriodo.setId(periodo_id);
        dimDev.setId(dev_id);
       
       
        if(periodo_id == 0)
        {
            dimPeriodo = null;
        }

        if(projeto_id == 0)
        {
            dimProjeto = null;
        }

        if(dev_id == 0)
        {
            dimDev = null;
        }

        List<FatoCustoHoraDTO> fatoCustoHoraDTOs = fatoCustoHoraService.getAllEntitiesByFilter(dimProjeto,dimPeriodo, dimDev);
        return ResponseEntity.ok(fatoCustoHoraDTOs);
    }
    

    @GetMapping
    public ResponseEntity<List<FatoCustoHoraDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<FatoCustoHoraDTO> fatoCustoHoraDTOs = fatoCustoHoraService.getAllEntities();
        return ResponseEntity.ok(fatoCustoHoraDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoCustoHoraDTO> getFatoCustoHoraById(@PathVariable Long id) {
        FatoCustoHoraDTO fatoCustoHoraDTO = fatoCustoHoraService.findById(id);
        if (fatoCustoHoraDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fatoCustoHoraDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<FatoCustoHoraDTO> addFatoCustoHora(@RequestBody FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHoraDTO createdEntity = fatoCustoHoraService.save(fatoCustoHoraDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoCustoHoraDTO> updateFatoCustoHora(@PathVariable Long id, @RequestBody FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHoraDTO updatedEntity = fatoCustoHoraService.update(id, fatoCustoHoraDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoCustoHora(@PathVariable Long id) {
        fatoCustoHoraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
