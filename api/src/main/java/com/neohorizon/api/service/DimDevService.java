package com.neohorizon.api.service;

import com.neohorizon.api.dto.DimDevDTO;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.repository.DimDevRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DimDevService {

    private final DimDevRepository dimDevRepository;

    @Autowired
    public DimDevService(DimDevRepository dimDevRepository) {
        this.dimDevRepository = dimDevRepository;
    }

    public List<DimDevDTO> getAllEntities() {
        return dimDevRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DimDevDTO findById(Long id) {
        return dimDevRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public DimDevDTO save(DimDevDTO dimDevDTO) {
        DimDev dimDev = convertToEntity(dimDevDTO);
        DimDev savedEntity = dimDevRepository.save(dimDev);
        return convertToDTO(savedEntity);
    }

    public DimDevDTO update(Long id, DimDevDTO dimDevDTO) {
        DimDev existingEntity = dimDevRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimDev with ID " + id + " not found."));

        existingEntity.setNome(dimDevDTO.getNome());
        existingEntity.setEmail(dimDevDTO.getEmail());
        existingEntity.setSenha(dimDevDTO.getSenha());
        existingEntity.setRole(dimDevDTO.getRole());
        existingEntity.setCusto_hora(dimDevDTO.getCusto_hora());

        DimDev updatedEntity = dimDevRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimDevRepository.deleteById(id);
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
