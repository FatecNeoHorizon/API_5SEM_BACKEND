package com.neohorizon.api.service;

import com.neohorizon.api.dto.DimTipoDTO;
import com.neohorizon.api.entity.DimTipo;
import com.neohorizon.api.repository.DimTipoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DimTipoService {

    private final DimTipoRepository dimTipoRepository;

    @Autowired
    public DimTipoService(DimTipoRepository dimTipoRepository) {
        this.dimTipoRepository = dimTipoRepository;
    }

    public List<DimTipoDTO> getAllEntities() {
        return dimTipoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DimTipoDTO findById(Long id) {
        return dimTipoRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public DimTipoDTO save(DimTipoDTO dimTipoDTO) {
        DimTipo dimTipo = convertToEntity(dimTipoDTO);
        DimTipo savedEntity = dimTipoRepository.save(dimTipo);
        return convertToDTO(savedEntity);
    }

    public DimTipoDTO update(Long id, DimTipoDTO dimTipoDTO) {
        DimTipo existingEntity = dimTipoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimTipo with ID " + id + " not found."));

        existingEntity.setNome(dimTipoDTO.getNome());
        existingEntity.setDescricao(dimTipoDTO.getDescricao());

        DimTipo updatedEntity = dimTipoRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimTipoRepository.deleteById(id);
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
