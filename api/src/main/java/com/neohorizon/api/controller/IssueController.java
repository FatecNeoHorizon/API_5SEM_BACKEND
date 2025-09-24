package com.neohorizon.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.service.IssueService;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalIssues() {
        return ResponseEntity.ok(issueService.getTotalIssues());
    }

    @GetMapping("/todos-projetos")
    public ResponseEntity<List<ProjectIssueCountDTO>> getAllProjectIssues() {
        return ResponseEntity.ok(issueService.getAllProjectIssues());
    }

    @GetMapping("/por-projeto/{projectId}")
    public ResponseEntity<List<ProjectIssueCountDTO>> getIssuesByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(issueService.getIssuesByProject(projectId));
    }

}
