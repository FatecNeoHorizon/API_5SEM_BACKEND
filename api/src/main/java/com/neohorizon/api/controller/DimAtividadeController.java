package com.neohorizon.api.controller;

import com.neohorizon.api.dto.DimAtividadeDTO;
import com.neohorizon.api.service.DimAtividadeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dim-atividade")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DimAtividadeController {

    private final DimAtividadeService dimAtividadeService;

    @Autowired
    public DimAtividadeController(DimAtividadeService dimAtividadeService) {
        this.dimAtividadeService = dimAtividadeService;
    }

    @GetMapping
    public ResponseEntity<List<DimAtividadeDTO>> getAllEntities() {
        List<DimAtividadeDTO> dimAtividadeDTOs = dimAtividadeService.getAllEntities();
        return ResponseEntity.ok(dimAtividadeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimAtividadeDTO> getDimAtividadeById(@PathVariable Long id) {
        DimAtividadeDTO dimAtividadeDTO = dimAtividadeService.findById(id);
        if (dimAtividadeDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimAtividadeDTO);
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<DimAtividadeDTO>> getAtividadesByProjeto(@PathVariable Long projetoId) {
        List<DimAtividadeDTO> atividades = dimAtividadeService.findByProjetoId(projetoId);
        return ResponseEntity.ok(atividades);
    }

    @PostMapping("/add")
    public ResponseEntity<DimAtividadeDTO> addDimAtividade(@RequestBody DimAtividadeDTO dimAtividadeDTO) {
        DimAtividadeDTO createdEntity = dimAtividadeService.save(dimAtividadeDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimAtividadeDTO> updateDimAtividade(@PathVariable Long id, @RequestBody DimAtividadeDTO dimAtividadeDTO) {
        DimAtividadeDTO updatedEntity = dimAtividadeService.update(id, dimAtividadeDTO);
        if (updatedEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimAtividade(@PathVariable Long id) {
        dimAtividadeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
