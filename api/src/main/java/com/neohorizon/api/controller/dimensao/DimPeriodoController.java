package com.neohorizon.api.controller.dimensao;

import com.neohorizon.api.dto.DimPeriodoDTO;
import com.neohorizon.api.service.dimensao.DimPeriodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dim-periodo")
public class DimPeriodoController {

    private final DimPeriodoService dimPeriodoService;

    @Autowired
    public DimPeriodoController(DimPeriodoService dimPeriodoService) {
        this.dimPeriodoService = dimPeriodoService;
    }

    @GetMapping
    public ResponseEntity<List<DimPeriodoDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<DimPeriodoDTO> dimPeriodoDTOs = dimPeriodoService.getAllEntities();
        return ResponseEntity.ok(dimPeriodoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimPeriodoDTO> getDimPeriodoById(@PathVariable Long id) {
        DimPeriodoDTO dimPeriodoDTO = dimPeriodoService.findById(id);
        if (dimPeriodoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimPeriodoDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<DimPeriodoDTO> addDimPeriodo(@RequestBody DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodoDTO createdEntity = dimPeriodoService.save(dimPeriodoDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimPeriodoDTO> updateDimPeriodo(@PathVariable Long id, @RequestBody DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodoDTO updatedEntity = dimPeriodoService.update(id, dimPeriodoDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimPeriodo(@PathVariable Long id) {
        dimPeriodoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
