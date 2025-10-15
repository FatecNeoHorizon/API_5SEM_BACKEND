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
import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.service.dimensao.DimStatusService;

@RestController
@RequestMapping("/dim-status")
public class DimStatusController extends BaseController {

    private final DimStatusService dimStatusService;

    @Autowired
    public DimStatusController(DimStatusService dimStatusService) {
        this.dimStatusService = dimStatusService;
    }

    @GetMapping
    public ResponseEntity<List<DimStatusDTO>> getAllEntities() {
        List<DimStatusDTO> dimStatusDTOs = dimStatusService.getAllEntities();
        return ok(dimStatusDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimStatusDTO> getDimStatusById(@PathVariable Long id) {
        DimStatusDTO dimStatusDTO = dimStatusService.findById(id);
        if (dimStatusDTO == null) {
            return notFound();
        }
        return ok(dimStatusDTO);
    }

    @PostMapping
    public ResponseEntity<DimStatusDTO> addDimStatus(@RequestBody DimStatusDTO dimStatusDTO) {
        DimStatusDTO createdEntity = dimStatusService.save(dimStatusDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimStatusDTO> updateDimStatus(@PathVariable Long id, @RequestBody DimStatusDTO dimStatusDTO) {
        DimStatusDTO updatedEntity = dimStatusService.update(id, dimStatusDTO);
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimStatus(@PathVariable Long id) {
        dimStatusService.deleteById(id);
        return noContent();
    }

}
