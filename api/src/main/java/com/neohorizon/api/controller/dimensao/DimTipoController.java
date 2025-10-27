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
import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;
import com.neohorizon.api.service.dimensao.DimTipoService;

@RestController
@RequestMapping("/dim-tipo")
public class DimTipoController extends BaseController {

    private final DimTipoService dimTipoService;

    @Autowired
    public DimTipoController(DimTipoService dimTipoService) {
        this.dimTipoService = dimTipoService;
    }

    @GetMapping
    public ResponseEntity<List<DimTipoDTO>> getAllEntities() {
        List<DimTipoDTO> dimTipoDTOs = dimTipoService.getAllEntities();
        return ok(dimTipoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimTipoDTO> getDimTipoById(@PathVariable Long id) {
        DimTipoDTO dimTipoDTO = dimTipoService.findById(id);
        if (dimTipoDTO == null) {
            return notFound();
        }
        return ok(dimTipoDTO);
    }
    
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<DimTipoDTO>> getDimTipoByNome(@PathVariable String nome) {
        List<DimTipoDTO> dimTipoDTOs = dimTipoService.findByNome(nome);
        return ok(dimTipoDTOs);
    }

    @PostMapping
    public ResponseEntity<DimTipoDTO> addDimTipo(@RequestBody DimTipoDTO dimTipoDTO) {
        DimTipoDTO createdEntity = dimTipoService.save(dimTipoDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimTipoDTO> updateDimTipo(@PathVariable Long id, @RequestBody DimTipoDTO dimTipoDTO) {
        DimTipoDTO updatedEntity = dimTipoService.update(id, dimTipoDTO);
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimTipo(@PathVariable Long id) {
        dimTipoService.deleteById(id);
        return noContent();
    }

}
