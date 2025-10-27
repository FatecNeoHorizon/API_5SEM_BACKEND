package com.neohorizon.api.controller.fato;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.controller.comum.BaseController;
import com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO;
import com.neohorizon.api.dto.response.fato.FatoAtividadeDTO;
import com.neohorizon.api.service.fato.FatoAtividadeService;

@RestController
@RequestMapping("/fato-atividade")
public class FatoAtividadeController extends BaseController {

    private final FatoAtividadeService fatoAtividadeService;

    public FatoAtividadeController(FatoAtividadeService fatoAtividadeService) {
        this.fatoAtividadeService = fatoAtividadeService;
    }

    @GetMapping
    public ResponseEntity<List<FatoAtividadeDTO>> getAllEntities() {
        List<FatoAtividadeDTO> fatoAtividadeDTOs = fatoAtividadeService.getAllEntities();
        return ok(fatoAtividadeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoAtividadeDTO> getFatoAtividadeById(@PathVariable Long id) {
        FatoAtividadeDTO fatoAtividadeDTO = fatoAtividadeService.findById(id);
        if (fatoAtividadeDTO == null) {
            return notFound();
        }
        return ok(fatoAtividadeDTO);
    }

    @PostMapping
    public ResponseEntity<FatoAtividadeDTO> addFatoAtividade(@RequestBody FatoAtividadeDTO fatoAtividadeDTO) {
        FatoAtividadeDTO createdEntity = fatoAtividadeService.create(fatoAtividadeDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoAtividadeDTO> updateFatoAtividade(@PathVariable Long id, @RequestBody FatoAtividadeDTO fatoAtividadeDTO) {
        FatoAtividadeDTO updatedEntity = fatoAtividadeService.update(id, fatoAtividadeDTO);
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoAtividade(@PathVariable Long id) {
        fatoAtividadeService.deleteById(id);
        return noContent();
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalAtividades() {
        return ok(fatoAtividadeService.getTotalAtividades());
    }

    @GetMapping("/por-projeto")
    public ResponseEntity<List<ProjectAtividadeCountDTO>> getAllProjectAtividades() {
        return ok(fatoAtividadeService.getAllProjectAtividades());
    }

    @GetMapping("/por-projeto/{projectId}")
    public ResponseEntity<List<ProjectAtividadeCountDTO>> getAtividadesByProject(@PathVariable Long projectId) {
        return ok(fatoAtividadeService.getAtividadesByProject(projectId));
    }
    
    @GetMapping("/agregado")
    public ResponseEntity<List<ProjectAtividadeCountDTO>> getAtividadesByAggregation(
            @RequestParam(value = "dataInicio", required = false) String dataInicio,
            @RequestParam(value = "dataFim", required = false) String dataFim,
            @RequestParam String periodo,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to) {

        String start = dataInicio != null ? dataInicio : from;
        String end = dataFim != null ? dataFim : to;

        if (start == null || end == null) {
            return badRequest();
        }

        List<ProjectAtividadeCountDTO> atividadesResultDTOs = 
                fatoAtividadeService.getAtividadesByAggregation(start, end, periodo);
        return ok(atividadesResultDTOs);
    }
}