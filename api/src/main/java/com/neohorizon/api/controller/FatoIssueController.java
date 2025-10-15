package com.neohorizon.api.controller;

import com.neohorizon.api.dto.FatoIssueDTO;
import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.service.fato.FatoIssueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fato-issue")
public class FatoIssueController {

    private final FatoIssueService fatoIssueService;

    @Autowired
    public FatoIssueController(FatoIssueService fatoIssueService) {
        this.fatoIssueService = fatoIssueService;
    }

    @GetMapping
    public ResponseEntity<List<FatoIssueDTO>> getAllEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<FatoIssueDTO> fatoIssueDTOs = fatoIssueService.getAllEntities();
        return ResponseEntity.ok(fatoIssueDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoIssueDTO> getFatoIssueById(@PathVariable Long id) {
        FatoIssueDTO fatoIssueDTO = fatoIssueService.findById(id);
        if (fatoIssueDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fatoIssueDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<FatoIssueDTO> addFatoIssue(@RequestBody FatoIssueDTO fatoIssueDTO) {
        FatoIssueDTO createdEntity = fatoIssueService.save(fatoIssueDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoIssueDTO> updateFatoIssue(@PathVariable Long id, @RequestBody FatoIssueDTO fatoIssueDTO) {
        FatoIssueDTO updatedEntity = fatoIssueService.update(id, fatoIssueDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoIssue(@PathVariable Long id) {
        fatoIssueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalIssues() {
        return ResponseEntity.ok(fatoIssueService.getTotalIssues());
    }

    @GetMapping("/todos-projetos")
    public ResponseEntity<List<ProjectIssueCountDTO>> getAllProjectIssues() {
        return ResponseEntity.ok(fatoIssueService.getAllProjectIssues());
    }

    @GetMapping("/por-projeto/{projectId}")
    public ResponseEntity<List<ProjectIssueCountDTO>> getIssuesByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(fatoIssueService.getIssuesByProject(projectId));
    }
    
    @GetMapping("/agregado")
    public ResponseEntity<List<ProjectIssueCountDTO>> getIssuesByAggregation(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @RequestParam String periodo) {

        List<ProjectIssueCountDTO> issuesResultDTOs = fatoIssueService.getIssuesByAggregation(dataInicio, dataFim, periodo);

        return ResponseEntity.ok(issuesResultDTOs);
    }
}
