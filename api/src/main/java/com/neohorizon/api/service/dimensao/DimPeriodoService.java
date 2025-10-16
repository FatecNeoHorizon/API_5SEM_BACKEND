package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DimPeriodoService {

    private static final String ENTITY_NAME = "DimPeriodo";
    private final DimPeriodoRepository dimPeriodoRepository;
    private final com.neohorizon.api.mapper.DimensionMapper dimensionMapper;

    public DimPeriodoService(DimPeriodoRepository dimPeriodoRepository, 
                            com.neohorizon.api.mapper.DimensionMapper dimensionMapper) {
        this.dimPeriodoRepository = dimPeriodoRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<DimPeriodoDTO> getAllEntities() {
        List<DimPeriodo> entities = dimPeriodoRepository.findAll();
        return dimensionMapper.periodoListToDTO(entities);
    }

    public DimPeriodoDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimPeriodoRepository.findById(id)
                .map(dimensionMapper::periodoToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimPeriodoDTO save(DimPeriodoDTO dimPeriodoDTO) {
        ValidationUtils.requireNonNull(dimPeriodoDTO, ENTITY_NAME + " é obrigatório");
        validateDimPeriodoDTO(dimPeriodoDTO);
        
        DimPeriodo dimPeriodo = dimensionMapper.dtoToPeriodo(dimPeriodoDTO);
        DimPeriodo savedEntity = dimPeriodoRepository.save(dimPeriodo);
        return dimensionMapper.periodoToDTO(savedEntity);
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
        return dimensionMapper.periodoToDTO(updatedEntity);
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

    public List<DimPeriodoDTO> getAllEntitiesByFilter(Integer dia, Integer semana, Integer mes, Integer ano) {
        return dimPeriodoRepository.findByDiaAndSemanaAndMesAndAno(dia, semana, mes, ano)
                .parallelStream()
                .map(dimensionMapper::periodoToDTO)
                .collect(Collectors.toList());
    }

    private void validateDimPeriodoDTO(DimPeriodoDTO dto) {
        ValidationUtils.require(dto.getAno() != null && dto.getAno() > 0, "Ano é obrigatório e deve ser positivo");
        ValidationUtils.require(dto.getMes() != null && dto.getMes() >= 1 && dto.getMes() <= 12, 
            "Mês deve estar entre 1 e 12");
        ValidationUtils.require(dto.getDia() != null && dto.getDia() >= 1 && dto.getDia() <= 31, 
            "Dia deve estar entre 1 e 31");
    }
}
