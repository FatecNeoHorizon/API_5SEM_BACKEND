package com.neohorizon.api.repository.custom;

import java.util.List;

import com.neohorizon.api.dto.IssueDTO.IssueAgregationDTO;
import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;

public interface FatoIssueRepositoryCustom {

    List<ProjectIssueCountDTO> findIssuesByPeriod(IssueAgregationDTO issueAgregationDTO);

}
