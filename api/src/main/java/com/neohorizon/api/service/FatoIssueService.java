package com.neohorizon.api.service;

import com.neohorizon.api.dto.FatoIssueDTO;
import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.entity.FatoIssue;
import com.neohorizon.api.enums.AggregationType;
import com.neohorizon.api.repository.FatoIssueRepository;


import jakarta.persistence.Query;
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

public List<Object []> getIssuesByAggregation(LocalDate dataInicio, LocalDate dataFim, String agregacao) {
        String selectGroupBy;
        
        AggregationType tipoAgregacao;
        try {
            tipoAgregacao = AggregationType.valueOf(agregacao.toUpperCase());
        } catch (IllegalArgumentException e) {
            tipoAgregacao = AggregationType.DIA;
        }

        switch (tipoAgregacao) {
            case ANO:
                selectGroupBy = "periodo_ano::text";
                break;
            case MES:
                selectGroupBy = "CONCAT(periodo_ano, '-', LPAD(periodo_mes::text, 2, '0'))";
                break;
            case SEMANA:
                selectGroupBy = "CONCAT(periodo_ano, '-W', LPAD(periodo_semana::text, 2, '0'))";
                break;
            case DIA:
            default:
                selectGroupBy = "TO_CHAR(TO_DATE(CONCAT(periodo_ano, '-', periodo_mes, '-', periodo_dia), 'YYYY-MM-DD'), 'YYYY-MM-DD')";
                break;
        }

        String sql = """
            WITH periodo_filtrado AS (
                SELECT periodo_id,
                       periodo_ano,
                       periodo_mes,
                       periodo_semana,
                       periodo_dia,
                       TO_DATE(CONCAT(periodo_ano, '-', periodo_mes, '-', periodo_dia), 'YYYY-MM-DD') AS data
                FROM dim_periodo
                WHERE TO_DATE(CONCAT(periodo_ano, '-', periodo_mes, '-', periodo_dia), 'YYYY-MM-DD')
                      BETWEEN :dataInicio AND :dataFim
            )
            SELECT 
                %s AS periodo,
                SUM(fi.issue_quantidade) AS total_issues
            FROM fato_issue fi
            JOIN periodo_filtrado pf ON fi.periodo_id = pf.periodo_id
            GROUP BY periodo
            ORDER BY periodo
        """.formatted(selectGroupBy);

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);

        return query.getResultList();
    }
}
