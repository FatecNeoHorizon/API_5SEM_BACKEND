package com.neohorizon.api.dto.IssueDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectIssueCountDTO {
    private String projectName;
    private long totalIssues;
}