package com.neohorizon.api.service.dimensao;

import com.neohorizon.api.dto.DimStatusDTO;
import com.neohorizon.api.entity.DimStatus;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.DimStatusRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimStatusService {

    private static final String ENTITY_NAME = "DimStatus";
    private final DimStatusRepository dimStatusRepository;

    @Autowired
    public DimStatusService(DimStatusRepository dimStatusRepository) {
        this.dimStatusRepository = dimStatusRepository;
    }

    public List<DimStatusDTO> getAllEntities() {
        return dimStatusRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimStatusDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimStatusRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimStatusDTO save(DimStatusDTO dimStatusDTO) {
        ValidationUtils.requireNonNull(dimStatusDTO, ENTITY_NAME + " é obrigatório");
        validateDimStatusDTO(dimStatusDTO);
        
        DimStatus dimStatus = convertToEntity(dimStatusDTO);
        DimStatus savedEntity = dimStatusRepository.save(dimStatus);
        return convertToDTO(savedEntity);
    }

    public DimStatusDTO update(Long id, DimStatusDTO dimStatusDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimStatusDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimStatusDTO(dimStatusDTO);
        
        DimStatus existingEntity = dimStatusRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimStatusDTO.getNome());
        existingEntity.setDescricao(dimStatusDTO.getDescricao());

        DimStatus updatedEntity = dimStatusRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimStatusRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimStatusRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimStatusDTO(DimStatusDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
    }

    private DimStatusDTO convertToDTO(DimStatus entity) {
        if (entity == null) {
            return null;
        }
        DimStatusDTO dto = new DimStatusDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());

        return dto;
    }

    private DimStatus convertToEntity(DimStatusDTO dto) {
        if (dto == null) {
            return null;
        }
        DimStatus entity = new DimStatus();
        entity.setId(dto.getId());

        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        return entity;
    }
}
