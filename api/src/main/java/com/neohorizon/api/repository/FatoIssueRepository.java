package com.neohorizon.api.repository;

import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.entity.FatoIssue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FatoIssueRepository extends JpaRepository<FatoIssue, Long>  {

    @Query("SELECT COUNT(fi.id) FROM FatoIssue fi")
    Long countAllIssues();

    @Query("SELECT new com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO(dp.nome, COUNT(fi.id)) " +
           "FROM FatoIssue fi JOIN fi.dimProjeto dp " +
           "WHERE dp.id = :projectId " +
           "GROUP BY dp.nome")
    List<ProjectIssueCountDTO> findIssueByProject(@Param("projectId") Long projectId);

    @Query("SELECT new com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO(dp.nome, COUNT(fi.id)) " +
           "FROM FatoIssue fi JOIN fi.dimProjeto dp " +
           "GROUP BY dp.nome")
    List<ProjectIssueCountDTO> findAllProjectIssues();


}
