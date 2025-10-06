package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimStatusRepository;

@Service
public class DimStatusService {

    private final DimStatusRepository dimStatusRepository;
    private final DimensionMapper dimensionMapper;

    @Autowired
    public DimStatusService(DimStatusRepository dimStatusRepository, DimensionMapper dimensionMapper) {
        this.dimStatusRepository = dimStatusRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<DimStatusDTO> getAllEntities() {
        return dimStatusRepository.findAll()
                .parallelStream()
                .map(dimensionMapper::statusToDTO)
                .collect(Collectors.toList());
    }

    public DimStatusDTO findById(Long id) {
        return dimStatusRepository.findById(id)
                .map(dimensionMapper::statusToDTO)
                .orElse(null);
    }

    public DimStatusDTO save(DimStatusDTO dimStatusDTO) {
        DimStatus dimStatus = dimensionMapper.dtoToStatus(dimStatusDTO);
        DimStatus savedEntity = dimStatusRepository.save(dimStatus);
        return dimensionMapper.statusToDTO(savedEntity);
    }

    public DimStatusDTO update(Long id, DimStatusDTO dimStatusDTO) {
        DimStatus existingEntity = dimStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimStatus with ID " + id + " not found."));

        existingEntity.setNome(dimStatusDTO.getNome());
        existingEntity.setDescricao(dimStatusDTO.getDescricao());

        DimStatus updatedEntity = dimStatusRepository.save(existingEntity);
        return dimensionMapper.statusToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimStatusRepository.deleteById(id);
    }
}
