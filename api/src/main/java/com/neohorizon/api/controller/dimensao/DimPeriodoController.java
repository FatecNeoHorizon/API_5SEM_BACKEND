package com.neohorizon.api.controller.dimensao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.controller.comum.BaseController;
import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.service.dimensao.DimPeriodoService;

@RestController
@RequestMapping("/dim-periodo")
public class DimPeriodoController extends BaseController {

    private final DimPeriodoService dimPeriodoService;

    @Autowired
    public DimPeriodoController(DimPeriodoService dimPeriodoService) {
        this.dimPeriodoService = dimPeriodoService;
    }

    @GetMapping
    public ResponseEntity<List<DimPeriodoDTO>> getAllEntities() {
        List<DimPeriodoDTO> dimPeriodoDTOs = dimPeriodoService.getAllEntities();
        return ok(dimPeriodoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimPeriodoDTO> getDimPeriodoById(@PathVariable Long id) {
        DimPeriodoDTO dimPeriodoDTO = dimPeriodoService.findById(id);
        if (dimPeriodoDTO == null) {
            return notFound();
        }
        return ok(dimPeriodoDTO);
    }

    @PostMapping
    public ResponseEntity<DimPeriodoDTO> addDimPeriodo(@RequestBody DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodoDTO createdEntity = dimPeriodoService.save(dimPeriodoDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimPeriodoDTO> updateDimPeriodo(@PathVariable Long id, @RequestBody DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodoDTO updatedEntity = dimPeriodoService.update(id, dimPeriodoDTO);
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimPeriodo(@PathVariable Long id) {
        dimPeriodoService.deleteById(id);
        return noContent();
    }

}
