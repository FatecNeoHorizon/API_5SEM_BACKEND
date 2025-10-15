package com.neohorizon.api.controller;

import com.neohorizon.api.dto.DimProjetoDTO;
import com.neohorizon.api.service.dimensao.DimProjetoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dim-projeto")
public class DimProjetoController {

    private final DimProjetoService dimProjetoService;

    @Autowired
    public DimProjetoController(DimProjetoService dimProjetoService) {
        this.dimProjetoService = dimProjetoService;
    }

    @GetMapping
    public ResponseEntity<List<DimProjetoDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<DimProjetoDTO> dimProjetoDTOs = dimProjetoService.getAllEntities();
        return ResponseEntity.ok(dimProjetoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimProjetoDTO> getDimProjetoById(@PathVariable Long id) {
        DimProjetoDTO dimProjetoDTO = dimProjetoService.findById(id);
        if (dimProjetoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimProjetoDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<DimProjetoDTO> addDimProjeto(@RequestBody DimProjetoDTO dimProjetoDTO) {
        DimProjetoDTO createdEntity = dimProjetoService.save(dimProjetoDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimProjetoDTO> updateDimProjeto(@PathVariable Long id, @RequestBody DimProjetoDTO dimProjetoDTO) {
        DimProjetoDTO updatedEntity = dimProjetoService.update(id, dimProjetoDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimProjeto(@PathVariable Long id) {
        dimProjetoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
