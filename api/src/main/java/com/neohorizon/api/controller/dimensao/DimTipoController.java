package com.neohorizon.api.controller.dimensao;

import com.neohorizon.api.dto.DimTipoDTO;
import com.neohorizon.api.service.dimensao.DimTipoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dim-tipo")
public class DimTipoController {

    private final DimTipoService dimTipoService;

    @Autowired
    public DimTipoController(DimTipoService dimTipoService) {
        this.dimTipoService = dimTipoService;
    }

    @GetMapping
    public ResponseEntity<List<DimTipoDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<DimTipoDTO> dimTipoDTOs = dimTipoService.getAllEntities();
        return ResponseEntity.ok(dimTipoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimTipoDTO> getDimTipoById(@PathVariable Long id) {
        DimTipoDTO dimTipoDTO = dimTipoService.findById(id);
        if (dimTipoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimTipoDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<DimTipoDTO> addDimTipo(@RequestBody DimTipoDTO dimTipoDTO) {
        DimTipoDTO createdEntity = dimTipoService.save(dimTipoDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimTipoDTO> updateDimTipo(@PathVariable Long id, @RequestBody DimTipoDTO dimTipoDTO) {
        DimTipoDTO updatedEntity = dimTipoService.update(id, dimTipoDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimTipo(@PathVariable Long id) {
        dimTipoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
