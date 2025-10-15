package com.neohorizon.api.service;

import com.neohorizon.api.dto.DimProjetoDTO;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.DimProjetoRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimProjetoService {

    private static final String ENTITY_NAME = "DimProjeto";
    private final DimProjetoRepository dimProjetoRepository;

    @Autowired
    public DimProjetoService(DimProjetoRepository dimProjetoRepository) {
        this.dimProjetoRepository = dimProjetoRepository;
    }

    public List<DimProjetoDTO> getAllEntities() {
        return dimProjetoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimProjetoDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimProjetoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimProjetoDTO save(DimProjetoDTO dimProjetoDTO) {
        ValidationUtils.requireNonNull(dimProjetoDTO, ENTITY_NAME + " é obrigatório");
        validateDimProjetoDTO(dimProjetoDTO);
        
        DimProjeto dimProjeto = convertToEntity(dimProjetoDTO);
        DimProjeto savedEntity = dimProjetoRepository.save(dimProjeto);
        return convertToDTO(savedEntity);
    }

    public DimProjetoDTO update(Long id, DimProjetoDTO dimProjetoDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimProjetoDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimProjetoDTO(dimProjetoDTO);
        
        DimProjeto existingEntity = dimProjetoRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimProjetoDTO.getNome());
        existingEntity.setKey(dimProjetoDTO.getKey());
        existingEntity.setJira_id(dimProjetoDTO.getJira_id());

        DimProjeto updatedEntity = dimProjetoRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimProjetoRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimProjetoRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimProjetoDTO(DimProjetoDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
        ValidationUtils.requireNonEmpty(dto.getKey(), "Key");
        ValidationUtils.requireNonEmpty(dto.getJira_id(), "Jira ID");
    }

    private DimProjetoDTO convertToDTO(DimProjeto entity) {
        if (entity == null) {
            return null;
        }
        DimProjetoDTO dto = new DimProjetoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setKey(entity.getKey());
        dto.setJira_id(entity.getJira_id());

        return dto;
    }

    private DimProjeto convertToEntity(DimProjetoDTO dto) {
        if (dto == null) {
            return null;
        }
        DimProjeto entity = new DimProjeto();
        entity.setId(dto.getId());

        entity.setNome(dto.getNome());
        entity.setKey(dto.getKey());
        entity.setJira_id(dto.getJira_id());

        return entity;
    }
}
