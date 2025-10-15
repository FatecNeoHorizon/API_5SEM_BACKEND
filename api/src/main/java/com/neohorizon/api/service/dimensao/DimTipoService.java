package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;
import com.neohorizon.api.entity.dimensao.DimTipo;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimTipoRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DimTipoService {

    private static final String ENTITY_NAME = "DimTipo";
    private final DimTipoRepository dimTipoRepository;
    private final DimensionMapper dimensionMapper;

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
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimTipoRepository.findById(id)
                .map(dimensionMapper::tipoToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimTipoDTO save(DimTipoDTO dimTipoDTO) {
        ValidationUtils.requireNonNull(dimTipoDTO, ENTITY_NAME + " é obrigatório");
        validateDimTipoDTO(dimTipoDTO);
        
        DimTipo dimTipo = dimensionMapper.dtoToTipo(dimTipoDTO);
        DimTipo savedEntity = dimTipoRepository.save(dimTipo);
        return dimensionMapper.tipoToDTO(savedEntity);
    }

    public DimTipoDTO update(Long id, DimTipoDTO dimTipoDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimTipoDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimTipoDTO(dimTipoDTO);
        
        DimTipo existingEntity = dimTipoRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimTipoDTO.getNome());
        existingEntity.setDescricao(dimTipoDTO.getDescricao());
        existingEntity.setTipoJiraId(dimTipoDTO.getTipoJiraId());

        DimTipo updatedEntity = dimTipoRepository.save(existingEntity);
        return dimensionMapper.tipoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimTipoRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimTipoRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimTipoDTO(DimTipoDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
    }
}
