package com.neohorizon.api.service;

import com.neohorizon.api.dto.DimStatusDTO;
import com.neohorizon.api.entity.DimStatus;
import com.neohorizon.api.repository.DimStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DimStatusService {

    private final DimStatusRepository dimStatusRepository;

    @Autowired
    public DimStatusService(DimStatusRepository dimStatusRepository) {
        this.dimStatusRepository = dimStatusRepository;
    }

    public List<DimStatusDTO> getAllEntities() {
        return dimStatusRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DimStatusDTO findById(Long id) {
        return dimStatusRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public DimStatusDTO save(DimStatusDTO dimStatusDTO) {
        DimStatus dimStatus = convertToEntity(dimStatusDTO);
        DimStatus savedEntity = dimStatusRepository.save(dimStatus);
        return convertToDTO(savedEntity);
    }

    public DimStatusDTO update(Long id, DimStatusDTO dimStatusDTO) {
        DimStatus existingEntity = dimStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimStatus with ID " + id + " not found."));

        existingEntity.setNome(dimStatusDTO.getNome());
        existingEntity.setDescricao(dimStatusDTO.getDescricao());

        DimStatus updatedEntity = dimStatusRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimStatusRepository.deleteById(id);
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
