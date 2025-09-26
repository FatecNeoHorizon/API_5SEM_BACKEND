package com.neohorizon.api.service;

import com.neohorizon.api.dto.FatoIssueDTO;
import com.neohorizon.api.dto.IssueDTO.IssuesResultDTO;
import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.entity.FatoIssue;
import com.neohorizon.api.enums.AggregationType;
import com.neohorizon.api.repository.FatoIssueRepository;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FatoIssueService {

    private final FatoIssueRepository fatoIssueRepository;
    private final EntityManager entityManager;

    @Autowired
    public FatoIssueService(FatoIssueRepository fatoIssueRepository, EntityManager entityManager) {
        this.fatoIssueRepository = fatoIssueRepository;
        this.entityManager = entityManager;
    }

    public List<FatoIssueDTO> getAllEntities() {
        return fatoIssueRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public FatoIssueDTO findById(Long id) {
        return fatoIssueRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public FatoIssueDTO save(FatoIssueDTO fatoIssueDTO) {
        FatoIssue fatoIssue = convertToEntity(fatoIssueDTO);
        FatoIssue savedEntity = fatoIssueRepository.save(fatoIssue);
        return convertToDTO(savedEntity);
    }

    public FatoIssueDTO update(Long id, FatoIssueDTO fatoIssueDTO) {
        FatoIssue existingEntity = fatoIssueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FatoIssue with ID " + id + " not found."));

        existingEntity.setDimProjeto(fatoIssueDTO.getDimProjeto());
        existingEntity.setDimPeriodo(fatoIssueDTO.getDimPeriodo());
        existingEntity.setDimStatus(fatoIssueDTO.getDimStatus());
        existingEntity.setDimTipo(fatoIssueDTO.getDimTipo());
        existingEntity.setQuantidade(fatoIssueDTO.getQuantidade());

        FatoIssue updatedEntity = fatoIssueRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        fatoIssueRepository.deleteById(id);
    }

    private FatoIssueDTO convertToDTO(FatoIssue entity) {
        if (entity == null) {
            return null;
        }
        FatoIssueDTO dto = new FatoIssueDTO();
        dto.setId(entity.getId());
        dto.setDimProjeto(entity.getDimProjeto());
        dto.setDimPeriodo(entity.getDimPeriodo());
        dto.setDimStatus(entity.getDimStatus());
        dto.setDimTipo(entity.getDimTipo());
        dto.setQuantidade(entity.getQuantidade());

        return dto;
    }

    private FatoIssue convertToEntity(FatoIssueDTO dto) {
        if (dto == null) {
            return null;
        }
        FatoIssue entity = new FatoIssue();
        entity.setId(dto.getId());

        entity.setDimProjeto(dto.getDimProjeto());
        entity.setDimPeriodo(dto.getDimPeriodo());
        entity.setDimStatus(dto.getDimStatus());
        entity.setDimTipo(dto.getDimTipo());
        entity.setQuantidade(dto.getQuantidade());
        return entity;
    }

    public Long getTotalIssues() {
        return fatoIssueRepository.countAllIssues();
    }

    public List<ProjectIssueCountDTO> getIssuesByProject(Long projectId) {
        return fatoIssueRepository.findIssueByProject(projectId);
    }

    public List<ProjectIssueCountDTO> getAllProjectIssues() {
        return fatoIssueRepository.findAllProjectIssues();
    }

    public List<IssuesResultDTO> getIssuesByAggregation(LocalDate dataInicio, LocalDate dataFim, AggregationType agregacao) {
        validarParametros(dataInicio, dataFim, agregacao);

        String selectClause = switch (agregacao) {
            case ANO -> "CAST(dp.ano AS string)";
            case MES -> "CONCAT(CAST(dp.ano AS string), '-', " +
                       "CASE WHEN dp.mes < 10 THEN CONCAT('0', CAST(dp.mes AS string)) " +
                       "ELSE CAST(dp.mes AS string) END)";
            case SEMANA -> "CONCAT(CAST(dp.ano AS string), '-W', " +
                          "CASE WHEN dp.semana < 10 THEN CONCAT('0', CAST(dp.semana AS string)) " +
                          "ELSE CAST(dp.semana AS string) END)";
            case DIA -> "CONCAT(CAST(dp.ano AS string), '-', " +
                       "CASE WHEN dp.mes < 10 THEN CONCAT('0', CAST(dp.mes AS string)) " +
                       "ELSE CAST(dp.mes AS string) END, '-', " +
                       "CASE WHEN dp.dia < 10 THEN CONCAT('0', CAST(dp.dia AS string)) " +
                       "ELSE CAST(dp.dia AS string) END)";
        };

        String jpql = String.format(
            "SELECT new com.neohorizon.api.dto.IssueDTO.IssuesResultDTO(SUM(fi.quantidade), %s) " +
            "FROM FatoIssue fi JOIN fi.dimPeriodo dp " +
            "WHERE FUNCTION('DATE', CONCAT(dp.ano, '-', dp.mes, '-', dp.dia)) " +
            "BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY %s " +
            "ORDER BY %s", 
            selectClause, selectClause, selectClause
        );

        return entityManager.createQuery(jpql, IssuesResultDTO.class)
                .setParameter("dataInicio", dataInicio)
                .setParameter("dataFim", dataFim)
                .getResultList();
    }

    private void validarParametros(LocalDate dataInicio, LocalDate dataFim, AggregationType agregacao) {
        if (dataInicio == null || dataFim == null || agregacao == null) {
            throw new IllegalArgumentException("Parâmetros obrigatórios não informados");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início deve ser anterior à data fim");
        }
    }
}
