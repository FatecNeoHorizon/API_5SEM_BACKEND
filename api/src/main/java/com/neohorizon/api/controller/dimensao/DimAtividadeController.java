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
import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.service.dimensao.DimAtividadeService;

@RestController
@RequestMapping("/dim-atividade")
public class DimAtividadeController extends BaseController {

    private final DimAtividadeService dimAtividadeService;

    @Autowired
    public DimAtividadeController(DimAtividadeService dimAtividadeService) {
        this.dimAtividadeService = dimAtividadeService;
    }

   @GetMapping
   public ResponseEntity<List<DimAtividadeDTO>> getAllEntities(@RequestBody(required = false) String filter) {
       List<DimAtividadeDTO> dimAtividadeDTOs = dimAtividadeService.getAllEntities();
       return ok(dimAtividadeDTOs);
   }

    @GetMapping("/{id}")
    public ResponseEntity<DimAtividadeDTO> getDimAtividadeById(@PathVariable Long id) {
        DimAtividadeDTO dimAtividadeDTO = dimAtividadeService.findById(id);
        if (dimAtividadeDTO == null) {
            return notFound();
        }
        return ok(dimAtividadeDTO);
    }

    @PostMapping
    public ResponseEntity<DimAtividadeDTO> addDimAtividade(@RequestBody DimAtividadeDTO dimAtividadeDTO) {
        DimAtividadeDTO createdEntity = dimAtividadeService.save(dimAtividadeDTO);
        return ok(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimAtividadeDTO> updateDimAtividade(@PathVariable Long id, @RequestBody DimAtividadeDTO dimAtividadeDTO) {
        DimAtividadeDTO updatedEntity = dimAtividadeService.update(id, dimAtividadeDTO);
        if (updatedEntity == null) {
            return notFound();
        }
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimAtividade(@PathVariable Long id) {
        dimAtividadeService.deleteById(id);
        return noContent();
    }
}
