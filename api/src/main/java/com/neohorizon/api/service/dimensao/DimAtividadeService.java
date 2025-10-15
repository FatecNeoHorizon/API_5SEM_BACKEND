package com.neohorizon.api.service.dimensao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;

@Service
public class DimAtividadeService {

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
        DimAtividade entity = dimAtividadeRepository.findById(id).orElse(null);
        return entity != null ? dimensionMapper.atividadeToDTO(entity) : null;
    }

    public DimAtividadeDTO save(DimAtividadeDTO dto) {
        DimAtividade entity = dimensionMapper.dtoToAtividade(dto);
        DimAtividade savedEntity = dimAtividadeRepository.save(entity);
        return dimensionMapper.atividadeToDTO(savedEntity);
    }

    public DimAtividadeDTO update(Long id, DimAtividadeDTO dto) {
        DimAtividade existingEntity = dimAtividadeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("DimAtividade com ID " + id + " não encontrado."));
        existingEntity.setNome(dto.getNome());
        existingEntity.setDescricao(dto.getDescricao());
        existingEntity.setAtivo(dto.getAtivo());
        DimAtividade updatedEntity = dimAtividadeRepository.save(existingEntity);
        return dimensionMapper.atividadeToDTO(updatedEntity);
    }

    // Soft delete - marca como inativo
    public void deleteById(Long id) {
        DimAtividade entity = dimAtividadeRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setAtivo(false);
            dimAtividadeRepository.save(entity);
        }
    }
}
