package com.neohorizon.api.controller.dimensao;

import com.neohorizon.api.dto.DimStatusDTO;
import com.neohorizon.api.service.dimensao.DimStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dim-status")
public class DimStatusController {

    private final DimStatusService dimStatusService;

    @Autowired
    public DimStatusController(DimStatusService dimStatusService) {
        this.dimStatusService = dimStatusService;
    }

    @GetMapping
    public ResponseEntity<List<DimStatusDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<DimStatusDTO> dimStatusDTOs = dimStatusService.getAllEntities();
        return ResponseEntity.ok(dimStatusDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimStatusDTO> getDimStatusById(@PathVariable Long id) {
        DimStatusDTO dimStatusDTO = dimStatusService.findById(id);
        if (dimStatusDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimStatusDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<DimStatusDTO> addDimStatus(@RequestBody DimStatusDTO dimStatusDTO) {
        DimStatusDTO createdEntity = dimStatusService.save(dimStatusDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimStatusDTO> updateDimStatus(@PathVariable Long id, @RequestBody DimStatusDTO dimStatusDTO) {
        DimStatusDTO updatedEntity = dimStatusService.update(id, dimStatusDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimStatus(@PathVariable Long id) {
        dimStatusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
