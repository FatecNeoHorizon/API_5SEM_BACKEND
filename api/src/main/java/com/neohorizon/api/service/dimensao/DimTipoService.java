package com.neohorizon.api.service.dimensao;

import com.neohorizon.api.dto.DimTipoDTO;
import com.neohorizon.api.entity.DimTipo;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.DimTipoRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimTipoService {

    private static final String ENTITY_NAME = "DimTipo";
    private final DimTipoRepository dimTipoRepository;

    @Autowired
    public DimTipoService(DimTipoRepository dimTipoRepository) {
        this.dimTipoRepository = dimTipoRepository;
    }

    public List<DimTipoDTO> getAllEntities() {
        return dimTipoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimTipoDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimTipoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimTipoDTO save(DimTipoDTO dimTipoDTO) {
        ValidationUtils.requireNonNull(dimTipoDTO, ENTITY_NAME + " é obrigatório");
        validateDimTipoDTO(dimTipoDTO);
        
        DimTipo dimTipo = convertToEntity(dimTipoDTO);
        DimTipo savedEntity = dimTipoRepository.save(dimTipo);
        return convertToDTO(savedEntity);
    }

    public DimTipoDTO update(Long id, DimTipoDTO dimTipoDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimTipoDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimTipoDTO(dimTipoDTO);
        
        DimTipo existingEntity = dimTipoRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimTipoDTO.getNome());
        existingEntity.setDescricao(dimTipoDTO.getDescricao());

        DimTipo updatedEntity = dimTipoRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimTipoRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimTipoRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimTipoDTO(DimTipoDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
    }

    private DimTipoDTO convertToDTO(DimTipo entity) {
        if (entity == null) {
            return null;
        }
        DimTipoDTO dto = new DimTipoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
    
        return dto;
    }

    private DimTipo convertToEntity(DimTipoDTO dto) {
        if (dto == null) {
            return null;
        }
        DimTipo entity = new DimTipo();
        entity.setId(dto.getId());

        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        return entity;
    }
}
