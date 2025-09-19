package com.neohorizon.api.service;

import com.neohorizon.api.dto.FatoCustoHoraDTO;
import com.neohorizon.api.entity.FatoCustoHora;
import com.neohorizon.api.repository.FatoCustoHoraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FatoCustoHoraService {

    private final FatoCustoHoraRepository fatoCustoHoraRepository;

    @Autowired
    public FatoCustoHoraService(FatoCustoHoraRepository fatoCustoHoraRepository) {
        this.fatoCustoHoraRepository = fatoCustoHoraRepository;
    }

    public List<FatoCustoHoraDTO> getAllEntities() {
        return fatoCustoHoraRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FatoCustoHoraDTO findById(Long id) {
        return fatoCustoHoraRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public FatoCustoHoraDTO save(FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHora fatoCustoHora = convertToEntity(fatoCustoHoraDTO);
        FatoCustoHora savedEntity = fatoCustoHoraRepository.save(fatoCustoHora);
        return convertToDTO(savedEntity);
    }

    public FatoCustoHoraDTO update(Long id, FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHora existingEntity = fatoCustoHoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FatoCustoHora with ID " + id + " not found."));

        existingEntity.setDimProjeto(fatoCustoHoraDTO.getDimProjeto());
        existingEntity.setDimPeriodo(fatoCustoHoraDTO.getDimPeriodo());
        existingEntity.setDimDev(fatoCustoHoraDTO.getDimDev());
        existingEntity.setCusto(fatoCustoHoraDTO.getCusto());
        existingEntity.setHoras_quantidade(fatoCustoHoraDTO.getHoras_quantidade());

        FatoCustoHora updatedEntity = fatoCustoHoraRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        fatoCustoHoraRepository.deleteById(id);
    }

    private FatoCustoHoraDTO convertToDTO(FatoCustoHora entity) {
        if (entity == null) {
            return null;
        }
        FatoCustoHoraDTO dto = new FatoCustoHoraDTO();
        dto.setId(entity.getId());
        dto.setDimProjeto(entity.getDimProjeto());
        dto.setDimPeriodo(entity.getDimPeriodo());
        dto.setDimDev(entity.getDimDev());
        dto.setCusto(entity.getCusto());
        dto.setHoras_quantidade(entity.getHoras_quantidade());

        return dto;
    }

    private FatoCustoHora convertToEntity(FatoCustoHoraDTO dto) {
        if (dto == null) {
            return null;
        }
        FatoCustoHora entity = new FatoCustoHora();
        entity.setId(dto.getId());

        entity.setDimProjeto(dto.getDimProjeto());
        entity.setDimPeriodo(dto.getDimPeriodo());
        entity.setDimDev(dto.getDimDev());
        entity.setCusto(dto.getCusto());
        entity.setHoras_quantidade(dto.getHoras_quantidade());
        return entity;
    }
}
