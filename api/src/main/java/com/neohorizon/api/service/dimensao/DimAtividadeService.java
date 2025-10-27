package com.neohorizon.api.service.dimensao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DimAtividadeService {

    private static final String ENTITY_NAME = "DimAtividade";
    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimensionMapper dimensionMapper;

    public DimAtividadeService(DimAtividadeRepository dimAtividadeRepository,
                              DimensionMapper dimensionMapper) {
        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<DimAtividadeDTO> getAllEntities() {
        List<DimAtividade> entities = dimAtividadeRepository.findByAtivoTrue();
        return dimensionMapper.atividadeListToDTO(entities);
    }

    public DimAtividadeDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        DimAtividade entity = dimAtividadeRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        return dimensionMapper.atividadeToDTO(entity);
    }

    public DimAtividadeDTO save(DimAtividadeDTO dto) {
        ValidationUtils.requireNonNull(dto, ENTITY_NAME + " é obrigatório");
        validateDimAtividadeDTO(dto);
        
        DimAtividade entity = dimensionMapper.dtoToAtividade(dto);
        DimAtividade savedEntity = dimAtividadeRepository.save(entity);
        return dimensionMapper.atividadeToDTO(savedEntity);
    }

    public DimAtividadeDTO update(Long id, DimAtividadeDTO dto) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dto, ENTITY_NAME + " é obrigatório para atualização");
        validateDimAtividadeDTO(dto);
        
        DimAtividade existingEntity = dimAtividadeRepository.findById(id)
            .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        existingEntity.setNome(dto.getNome());
        existingEntity.setDescricao(dto.getDescricao());
        existingEntity.setAtivo(dto.getAtivo());
        DimAtividade updatedEntity = dimAtividadeRepository.save(existingEntity);
        return dimensionMapper.atividadeToDTO(updatedEntity);
    }

    // Soft delete - marca como inativo
    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        DimAtividade entity = dimAtividadeRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        
        try {
            entity.setAtivo(false);
            dimAtividadeRepository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao desativar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimAtividadeDTO(DimAtividadeDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
    }
}
