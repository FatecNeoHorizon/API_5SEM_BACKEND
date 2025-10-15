package com.neohorizon.api.controller;

import com.neohorizon.api.dto.DimDevDTO;
import com.neohorizon.api.service.dimensao.DimDevService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dim-dev")
public class DimDevController {

    private final DimDevService dimDevService;

    @Autowired
    public DimDevController(DimDevService dimDevService) {
        this.dimDevService = dimDevService;
    }

    @GetMapping
    public ResponseEntity<List<DimDevDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<DimDevDTO> dimDevDTOs = dimDevService.getAllEntities();
        return ResponseEntity.ok(dimDevDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimDevDTO> getDimDevById(@PathVariable Long id) {
        DimDevDTO dimDevDTO = dimDevService.findById(id);
        if (dimDevDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimDevDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<DimDevDTO> addDimDev(@RequestBody DimDevDTO dimDevDTO) {
        DimDevDTO createdEntity = dimDevService.save(dimDevDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimDevDTO> updateDimDev(@PathVariable Long id, @RequestBody DimDevDTO dimDevDTO) {
        DimDevDTO updatedEntity = dimDevService.update(id, dimDevDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimDev(@PathVariable Long id) {
        dimDevService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
