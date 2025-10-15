package com.neohorizon.api.service.fato;

import com.neohorizon.api.dto.FatoIssueDTO;
import com.neohorizon.api.dto.IssueDTO.IssueAgregationDTO;
import com.neohorizon.api.dto.IssueDTO.ProjectIssueCountDTO;
import com.neohorizon.api.entity.FatoIssue;
import com.neohorizon.api.enums.AggregationType;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.FatoIssueRepository;
import com.neohorizon.api.utils.ConvertStringToInteger;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FatoIssueService {

    private static final String ENTITY_NAME = "FatoIssue";
    private final FatoIssueRepository fatoIssueRepository;

    @Autowired
    public FatoIssueService(FatoIssueRepository fatoIssueRepository) {
        this.fatoIssueRepository = fatoIssueRepository;
    }

    public List<FatoIssueDTO> getAllEntities() {
        return fatoIssueRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public FatoIssueDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return fatoIssueRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public FatoIssueDTO save(FatoIssueDTO fatoIssueDTO) {
        ValidationUtils.requireNonNull(fatoIssueDTO, ENTITY_NAME + " é obrigatório");
        validateFatoIssueDTO(fatoIssueDTO);
        
        FatoIssue fatoIssue = convertToEntity(fatoIssueDTO);
        FatoIssue savedEntity = fatoIssueRepository.save(fatoIssue);
        return convertToDTO(savedEntity);
    }

    public FatoIssueDTO update(Long id, FatoIssueDTO fatoIssueDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(fatoIssueDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateFatoIssueDTO(fatoIssueDTO);
        
        FatoIssue existingEntity = fatoIssueRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setDimProjeto(fatoIssueDTO.getDimProjeto());
        existingEntity.setDimPeriodo(fatoIssueDTO.getDimPeriodo());
        existingEntity.setDimStatus(fatoIssueDTO.getDimStatus());
        existingEntity.setDimTipo(fatoIssueDTO.getDimTipo());
        existingEntity.setQuantidade(fatoIssueDTO.getQuantidade());

        FatoIssue updatedEntity = fatoIssueRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!fatoIssueRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            fatoIssueRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateFatoIssueDTO(FatoIssueDTO dto) {
        ValidationUtils.requireNonNull(dto.getDimProjeto(), "Projeto");
        ValidationUtils.requireNonNull(dto.getDimPeriodo(), "Período");
        ValidationUtils.requireNonNull(dto.getDimStatus(), "Status");
        ValidationUtils.requireNonNull(dto.getDimTipo(), "Tipo");
        ValidationUtils.require(dto.getQuantidade() != null && dto.getQuantidade() >= 0, 
            "Quantidade deve ser um valor positivo");
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

    public List<ProjectIssueCountDTO> getIssuesByAggregation(String dataInicio, String dataFim, String periodo) {

        Integer[] dataInicioInteger = ConvertStringToInteger.convertStringDateToInteger(dataInicio);
        Integer[] dataFimInteger = ConvertStringToInteger.convertStringDateToInteger(dataFim);
        AggregationType periodoEnum = AggregationType.fromString(periodo);

        IssueAgregationDTO periodoBuscado = new IssueAgregationDTO();
        periodoBuscado.setAnoInicio(dataInicioInteger[0]); // parts[0] é o ano
        periodoBuscado.setMesInicio(dataInicioInteger[1]); // parts[1] é o mês
        periodoBuscado.setDiaInicio(dataInicioInteger[2]); // parts[2] é o dia

        periodoBuscado.setAnoFim(dataFimInteger[0]);    // parts[0] é o ano
        periodoBuscado.setMesFim(dataFimInteger[1]);    // parts[1] é o mês
        periodoBuscado.setDiaFim(dataFimInteger[2]);    // parts[2] é o dia
    
        periodoBuscado.setPeriodo(periodoEnum);

        return fatoIssueRepository.findIssuesByPeriod(periodoBuscado);
    }
        
}
