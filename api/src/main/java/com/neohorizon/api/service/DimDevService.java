package com.neohorizon.api.service;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.DimDevDTO;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.DimDevRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimDevService {

    private static final String ENTITY_NAME = "DimDev";
    private final DimDevRepository dimDevRepository;

    @Autowired
    public DimDevService(DimDevRepository dimDevRepository) {
        this.dimDevRepository = dimDevRepository;
    }

    public List<DimDevDTO> getAllEntities() {
        return dimDevRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimDevDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimDevRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimDevDTO save(DimDevDTO dimDevDTO) {
        ValidationUtils.requireNonNull(dimDevDTO, MessageConstants.DIMDEV_PREFIX + " é obrigatório");
        validateDimDevDTO(dimDevDTO);
        
        DimDev dimDev = convertToEntity(dimDevDTO);
        DimDev savedEntity = dimDevRepository.save(dimDev);
        return convertToDTO(savedEntity);
    }

    public DimDevDTO update(Long id, DimDevDTO dimDevDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimDevDTO, "DimDevDTO é obrigatório para atualização");
        
        validateDimDevDTO(dimDevDTO);
        
        DimDev existingEntity = dimDevRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimDevDTO.getNome());
        existingEntity.setEmail(dimDevDTO.getEmail());
        existingEntity.setSenha(dimDevDTO.getSenha());
        existingEntity.setRole(dimDevDTO.getRole());
        existingEntity.setCusto_hora(dimDevDTO.getCusto_hora());

        DimDev updatedEntity = dimDevRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimDevRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimDevRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar DimDev: " + e.getMessage(), e);
        }
    }

    private void validateDimDevDTO(DimDevDTO dimDevDTO) {
        ValidationUtils.requireNonEmpty(dimDevDTO.getNome(), "Nome");
        ValidationUtils.requireValidEmail(dimDevDTO.getEmail());
        ValidationUtils.requireNonEmpty(dimDevDTO.getSenha(), "Senha");
        ValidationUtils.requireNonEmpty(dimDevDTO.getRole(), "Role");
        
        ValidationUtils.require(dimDevDTO.getCusto_hora() != null && dimDevDTO.getCusto_hora() >= 0, 
            "Custo por hora deve ser um valor positivo");
    }

    private DimDevDTO convertToDTO(DimDev entity) {
        if (entity == null) {
            return null;
        }
        DimDevDTO dto = new DimDevDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setSenha(entity.getSenha());
        dto.setRole(entity.getRole());
        dto.setCusto_hora(entity.getCusto_hora());

        return dto;
    }

    private DimDev convertToEntity(DimDevDTO dto) {
        if (dto == null) {
            return null;
        }
        DimDev entity = new DimDev();
        entity.setId(dto.getId());

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setRole(dto.getRole());
        entity.setCusto_hora(dto.getCusto_hora());
        return entity;
    }
}
