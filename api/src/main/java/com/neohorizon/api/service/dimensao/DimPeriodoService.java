package com.neohorizon.api.service.dimensao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;

@Service
public class DimPeriodoService {

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
        return dimPeriodoRepository.findById(id)
                .map(dimensionMapper::periodoToDTO)
                .orElse(null);
    }

    public DimPeriodoDTO save(DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodo dimPeriodo = dimensionMapper.dtoToPeriodo(dimPeriodoDTO);
        DimPeriodo savedEntity = dimPeriodoRepository.save(dimPeriodo);
        return dimensionMapper.periodoToDTO(savedEntity);
    }

    public DimPeriodoDTO update(Long id, DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodo existingEntity = dimPeriodoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("DimPeriodo com ID " + id + " não encontrado."));
    
        existingEntity.setDia(dimPeriodoDTO.getDia());
        existingEntity.setSemana(dimPeriodoDTO.getSemana());
        existingEntity.setMes(dimPeriodoDTO.getMes());
        existingEntity.setAno(dimPeriodoDTO.getAno());
    
        DimPeriodo updatedEntity = dimPeriodoRepository.save(existingEntity);
        return dimensionMapper.periodoToDTO(updatedEntity);
    }
    
    public void deleteById(Long id) {
        dimPeriodoRepository.deleteById(id);
    }
}
