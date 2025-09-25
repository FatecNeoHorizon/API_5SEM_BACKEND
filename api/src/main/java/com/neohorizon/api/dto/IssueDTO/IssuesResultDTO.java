package com.neohorizon.api.dto.IssueDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssuesResultDTO {

    Long totalIssues;
    String periodo;
}
