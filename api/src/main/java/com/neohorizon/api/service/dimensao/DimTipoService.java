package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;
import com.neohorizon.api.entity.dimensao.DimTipo;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimTipoRepository;

@Service
public class DimTipoService {

    private final DimTipoRepository dimTipoRepository;
    private final DimensionMapper dimensionMapper;

    @Autowired
    public DimTipoService(DimTipoRepository dimTipoRepository, DimensionMapper dimensionMapper) {
        this.dimTipoRepository = dimTipoRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<DimTipoDTO> getAllEntities() {
        return dimTipoRepository.findAll()
                .parallelStream()
                .map(dimensionMapper::tipoToDTO)
                .collect(Collectors.toList());
    }

    public DimTipoDTO findById(Long id) {
        return dimTipoRepository.findById(id)
                .map(dimensionMapper::tipoToDTO)
                .orElse(null);
    }

    public DimTipoDTO save(DimTipoDTO dimTipoDTO) {
        DimTipo dimTipo = dimensionMapper.dtoToTipo(dimTipoDTO);
        DimTipo savedEntity = dimTipoRepository.save(dimTipo);
        return dimensionMapper.tipoToDTO(savedEntity);
    }

    public DimTipoDTO update(Long id, DimTipoDTO dimTipoDTO) {
        DimTipo existingEntity = dimTipoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimTipo with ID " + id + " not found."));

        existingEntity.setNome(dimTipoDTO.getNome());
        existingEntity.setDescricao(dimTipoDTO.getDescricao());

        DimTipo updatedEntity = dimTipoRepository.save(existingEntity);
        return dimensionMapper.tipoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimTipoRepository.deleteById(id);
    }
}
