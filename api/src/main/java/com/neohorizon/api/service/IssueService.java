package com.neohorizon.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.repository.IssueRepository;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public Long getTotalIssues() {
        return issueRepository.countAllIssues();
    }

    public List<ProjectIssueCountDTO> getIssuesByProject(Long projectId) {
        return issueRepository.findIssueByProject(projectId);
    }

    public List<ProjectIssueCountDTO> getAllProjectIssues() {
        return issueRepository.findAllProjectIssues();
    }

}