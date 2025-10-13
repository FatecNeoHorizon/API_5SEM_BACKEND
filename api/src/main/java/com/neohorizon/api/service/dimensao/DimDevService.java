package com.neohorizon.api.service.dimensao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimDevRepository;

@Service
public class DimDevService {

    private final DimDevRepository dimDevRepository;
    private final DimensionMapper dimensionMapper;

    public DimDevService(DimDevRepository dimDevRepository, DimensionMapper dimensionMapper) {
        this.dimDevRepository = dimDevRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public DimDevDTO getById(Long id) {
        return dimDevRepository.findById(id)
                .map(dimensionMapper::devToDTO)
                .orElse(null);
    }

    public List<DimDevDTO> getAllEntities() {
        return dimDevRepository.findAll().stream()
                .map(dimensionMapper::devToDTO)
                .toList();
    }

    public DimDevDTO create(DimDevDTO dimDevDTO) {
        DimDev entity = dimensionMapper.dtoToDev(dimDevDTO);
        DimDev savedEntity = dimDevRepository.save(entity);
        return dimensionMapper.devToDTO(savedEntity);
    }

    public DimDevDTO update(Long id, DimDevDTO dimDevDTO) {
        return dimDevRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setNome(dimDevDTO.getNome());
                    existingEntity.setCusto_hora(dimDevDTO.getCusto_hora());
                    DimDev updatedEntity = dimDevRepository.save(existingEntity);
                    return dimensionMapper.devToDTO(updatedEntity);
                })
                .orElseThrow(() -> new IllegalArgumentException("DimDev com ID " + id + " não encontrado."));
    }

    public boolean delete(Long id) {
        if (dimDevRepository.existsById(id)) {
            dimDevRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
