package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimStatusRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DimStatusService {

    private static final String ENTITY_NAME = "DimStatus";
    private final DimStatusRepository dimStatusRepository;
    private final DimensionMapper dimensionMapper;

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
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimStatusRepository.findById(id)
                .map(dimensionMapper::statusToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimStatusDTO save(DimStatusDTO dimStatusDTO) {
        ValidationUtils.requireNonNull(dimStatusDTO, ENTITY_NAME + " é obrigatório");
        validateDimStatusDTO(dimStatusDTO);
        
        DimStatus dimStatus = dimensionMapper.dtoToStatus(dimStatusDTO);
        DimStatus savedEntity = dimStatusRepository.save(dimStatus);
        return dimensionMapper.statusToDTO(savedEntity);
    }

    public DimStatusDTO update(Long id, DimStatusDTO dimStatusDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimStatusDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimStatusDTO(dimStatusDTO);
        
        DimStatus existingEntity = dimStatusRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimStatusDTO.getNome());
        existingEntity.setStatusJiraId(dimStatusDTO.getStatusJiraId());

        DimStatus updatedEntity = dimStatusRepository.save(existingEntity);
        return dimensionMapper.statusToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimStatusRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimStatusRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimStatusDTO(DimStatusDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
    }
}
