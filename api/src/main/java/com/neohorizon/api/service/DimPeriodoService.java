package com.neohorizon.api.service;

import com.neohorizon.api.dto.DimPeriodoDTO;
import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.DimPeriodoRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimPeriodoService {

    private static final String ENTITY_NAME = "DimPeriodo";
    private final DimPeriodoRepository dimPeriodoRepository;

    @Autowired
    public DimPeriodoService(DimPeriodoRepository dimPeriodoRepository) {
        this.dimPeriodoRepository = dimPeriodoRepository;
    }

    public List<DimPeriodoDTO> getAllEntities() {
        return dimPeriodoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimPeriodoDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimPeriodoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimPeriodoDTO save(DimPeriodoDTO dimPeriodoDTO) {
        ValidationUtils.requireNonNull(dimPeriodoDTO, ENTITY_NAME + " é obrigatório");
        validateDimPeriodoDTO(dimPeriodoDTO);
        
        DimPeriodo dimPeriodo = convertToEntity(dimPeriodoDTO);
        DimPeriodo savedEntity = dimPeriodoRepository.save(dimPeriodo);
        return convertToDTO(savedEntity);
    }

    public DimPeriodoDTO update(Long id, DimPeriodoDTO dimPeriodoDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimPeriodoDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimPeriodoDTO(dimPeriodoDTO);
        
        DimPeriodo existingEntity = dimPeriodoRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setDia(dimPeriodoDTO.getDia());
        existingEntity.setSemana(dimPeriodoDTO.getSemana());
        existingEntity.setMes(dimPeriodoDTO.getMes());
        existingEntity.setAno(dimPeriodoDTO.getAno());

        DimPeriodo updatedEntity = dimPeriodoRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimPeriodoRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimPeriodoRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimPeriodoDTO(DimPeriodoDTO dto) {
        ValidationUtils.require(dto.getAno() != null && dto.getAno() > 0, "Ano é obrigatório e deve ser positivo");
        ValidationUtils.require(dto.getMes() != null && dto.getMes() >= 1 && dto.getMes() <= 12, 
            "Mês deve estar entre 1 e 12");
        ValidationUtils.require(dto.getDia() != null && dto.getDia() >= 1 && dto.getDia() <= 31, 
            "Dia deve estar entre 1 e 31");
    }

    private DimPeriodoDTO convertToDTO(DimPeriodo entity) {
        if (entity == null) {
            return null;
        }
        DimPeriodoDTO dto = new DimPeriodoDTO();
        dto.setId(entity.getId());
        dto.setDia(entity.getDia());
        dto.setSemana(entity.getSemana());
        dto.setMes(entity.getMes());
        dto.setAno(entity.getAno());
        return dto;
    }

    private DimPeriodo convertToEntity(DimPeriodoDTO dto) {
        if (dto == null) {
            return null;
        }
        DimPeriodo entity = new DimPeriodo();
        entity.setId(dto.getId());

        entity.setDia(dto.getDia());
        entity.setSemana(dto.getSemana());
        entity.setMes(dto.getMes());
        entity.setAno(dto.getAno());
        return entity;
    }
}
