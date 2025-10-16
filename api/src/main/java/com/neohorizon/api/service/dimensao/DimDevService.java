package com.neohorizon.api.service.dimensao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DimDevService {

    private static final String ENTITY_NAME = "DimDev";
    private final DimDevRepository dimDevRepository;
    private final DimensionMapper dimensionMapper;

    public DimDevService(DimDevRepository dimDevRepository, DimensionMapper dimensionMapper) {
        this.dimDevRepository = dimDevRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public DimDevDTO getById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimDevRepository.findById(id)
                .map(dimensionMapper::devToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public List<DimDevDTO> getAllEntities() {
        return dimDevRepository.findAll().stream()
                .map(dimensionMapper::devToDTO)
                .toList();
    }

    public DimDevDTO create(DimDevDTO dimDevDTO) {
        ValidationUtils.requireNonNull(dimDevDTO, ENTITY_NAME + " é obrigatório");
        validateDimDevDTO(dimDevDTO);
        
        DimDev entity = dimensionMapper.dtoToDev(dimDevDTO);
        DimDev savedEntity = dimDevRepository.save(entity);
        return dimensionMapper.devToDTO(savedEntity);
    }

    public DimDevDTO update(Long id, DimDevDTO dimDevDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimDevDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimDevDTO(dimDevDTO);
        
        return dimDevRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setNome(dimDevDTO.getNome());
                    existingEntity.setCustoHora(dimDevDTO.getCustoHora());
                    DimDev updatedEntity = dimDevRepository.save(existingEntity);
                    return dimensionMapper.devToDTO(updatedEntity);
                })
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public boolean delete(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimDevRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimDevRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimDevDTO(DimDevDTO dimDevDTO) {
        ValidationUtils.requireNonEmpty(dimDevDTO.getNome(), "Nome");
        
        ValidationUtils.require(
            dimDevDTO.getCustoHora() != null && dimDevDTO.getCustoHora().compareTo(java.math.BigDecimal.ZERO) >= 0,
            "Custo por hora deve ser um valor positivo"
        );
    }
}
