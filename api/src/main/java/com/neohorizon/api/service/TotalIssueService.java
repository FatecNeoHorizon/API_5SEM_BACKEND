package com.neohorizon.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.repository.TotalIssueRepository;

@Service
public class TotalIssueService {

    @Autowired
    private TotalIssueRepository totalIssueRepository;

    public Long getTotalIssues() {
        return totalIssueRepository.countAllIssues();
    }

    public List<ProjectIssueCountDTO> getIssuesByProject(Long projectId) {
        return totalIssueRepository.findIssueByProject(projectId);
    }

    public List<ProjectIssueCountDTO> getAllProjectIssues() {
        return totalIssueRepository.findAllProjectIssues();
    }

}