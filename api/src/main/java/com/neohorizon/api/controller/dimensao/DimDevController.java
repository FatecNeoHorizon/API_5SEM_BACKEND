package com.neohorizon.api.controller.dimensao;

import java.util.List;

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
import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.service.dimensao.DimDevService;

@RestController
@RequestMapping("/dim-dev")
public class DimDevController extends BaseController {

    private final DimDevService dimDevService;

    public DimDevController(DimDevService dimDevService) {
        this.dimDevService = dimDevService;
    }

    @GetMapping
    public ResponseEntity<List<DimDevDTO>> getAllEntities() {
        List<DimDevDTO> devs = dimDevService.getAllEntities();
        return ok(devs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimDevDTO> getEntityById(@PathVariable Long id) {
        DimDevDTO dev = dimDevService.getById(id);
        if (dev == null) {
            return notFound();
        }
        return ok(dev);
    }

    @PostMapping
    public ResponseEntity<DimDevDTO> createEntity(@RequestBody DimDevDTO dimDevDTO) {
        DimDevDTO createdDev = dimDevService.create(dimDevDTO);
        return created(createdDev);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimDevDTO> updateEntity(@PathVariable Long id, @RequestBody DimDevDTO dimDevDTO) {
        DimDevDTO updatedDev = dimDevService.update(id, dimDevDTO);
        if (updatedDev == null) {
            return notFound();
        }
        return ok(updatedDev);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        boolean deleted = dimDevService.delete(id);
        if (!deleted) {
            return notFound();
        }
        return noContent();
    }

}
