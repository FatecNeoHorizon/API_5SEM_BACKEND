package com.neohorizon.api.service;

import com.neohorizon.api.dto.DimProjetoDTO;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.repository.DimProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DimProjetoService {

    private final DimProjetoRepository dimProjetoRepository;

    @Autowired
    public DimProjetoService(DimProjetoRepository dimProjetoRepository) {
        this.dimProjetoRepository = dimProjetoRepository;
    }

    public List<DimProjetoDTO> getAllEntities() {
        return dimProjetoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DimProjetoDTO findById(Long id) {
        return dimProjetoRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public DimProjetoDTO save(DimProjetoDTO dimProjetoDTO) {
        DimProjeto dimProjeto = convertToEntity(dimProjetoDTO);
        DimProjeto savedEntity = dimProjetoRepository.save(dimProjeto);
        return convertToDTO(savedEntity);
    }

    public DimProjetoDTO update(Long id, DimProjetoDTO dimProjetoDTO) {
        DimProjeto existingEntity = dimProjetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimProjeto with ID " + id + " not found."));

        existingEntity.setNome(dimProjetoDTO.getNome());
        existingEntity.setKey(dimProjetoDTO.getKey());
        existingEntity.setJira_id(dimProjetoDTO.getJira_id());

        DimProjeto updatedEntity = dimProjetoRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimProjetoRepository.deleteById(id);
    }

    private DimProjetoDTO convertToDTO(DimProjeto entity) {
        if (entity == null) {
            return null;
        }
        DimProjetoDTO dto = new DimProjetoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setKey(entity.getKey());
        dto.setJira_id(entity.getJira_id());

        return dto;
    }

    private DimProjeto convertToEntity(DimProjetoDTO dto) {
        if (dto == null) {
            return null;
        }
        DimProjeto entity = new DimProjeto();
        entity.setId(dto.getId());

        entity.setNome(dto.getNome());
        entity.setKey(dto.getKey());
        entity.setJira_id(dto.getJira_id());

        return entity;
    }
}
