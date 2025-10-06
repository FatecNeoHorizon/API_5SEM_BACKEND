package com.neohorizon.api.service.dimensao;

import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DimPeriodoService {

    private final DimPeriodoRepository dimPeriodoRepository;

    @Autowired
    public DimPeriodoService(DimPeriodoRepository dimPeriodoRepository) {
        this.dimPeriodoRepository = dimPeriodoRepository;
    }

    public List<DimPeriodoDTO> getAllEntities() {
        return dimPeriodoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DimPeriodoDTO findById(Long id) {
        return dimPeriodoRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public DimPeriodoDTO save(DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodo dimPeriodo = convertToEntity(dimPeriodoDTO);
        DimPeriodo savedEntity = dimPeriodoRepository.save(dimPeriodo);
        return convertToDTO(savedEntity);
    }

    public DimPeriodoDTO update(Long id, DimPeriodoDTO dimPeriodoDTO) {
        DimPeriodo existingEntity = dimPeriodoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimPeriodo with ID " + id + " not found."));

        existingEntity.setDia(dimPeriodoDTO.getDia());
        existingEntity.setSemana(dimPeriodoDTO.getSemana());
        existingEntity.setMes(dimPeriodoDTO.getMes());
        existingEntity.setAno(dimPeriodoDTO.getAno());

        DimPeriodo updatedEntity = dimPeriodoRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimPeriodoRepository.deleteById(id);
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
