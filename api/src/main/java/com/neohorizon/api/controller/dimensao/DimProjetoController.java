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
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.service.dimensao.DimProjetoService;

@RestController
@RequestMapping("/dim-projeto")
public class DimProjetoController extends BaseController {

    private final DimProjetoService dimProjetoService;

    @Autowired
    public DimProjetoController(DimProjetoService dimProjetoService) {
        this.dimProjetoService = dimProjetoService;
    }

    @GetMapping
    public ResponseEntity<List<DimProjetoDTO>> getAllEntities() {
        List<DimProjetoDTO> dimProjetoDTOs = dimProjetoService.getAllEntities();
        return ok(dimProjetoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimProjetoDTO> getDimProjetoById(@PathVariable Long id) {
        DimProjetoDTO dimProjetoDTO = dimProjetoService.findById(id);
        if (dimProjetoDTO == null) {
            return notFound();
        }
        return ok(dimProjetoDTO);
    }

    @PostMapping
    public ResponseEntity<DimProjetoDTO> addDimProjeto(@RequestBody DimProjetoDTO dimProjetoDTO) {
        DimProjetoDTO createdEntity = dimProjetoService.save(dimProjetoDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimProjetoDTO> updateDimProjeto(@PathVariable Long id, @RequestBody DimProjetoDTO dimProjetoDTO) {
        DimProjetoDTO updatedEntity = dimProjetoService.update(id, dimProjetoDTO);
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimProjeto(@PathVariable Long id) {
        dimProjetoService.deleteById(id);
        return noContent();
    }

}
