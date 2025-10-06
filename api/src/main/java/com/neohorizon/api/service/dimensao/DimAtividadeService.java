package com.neohorizon.api.service.dimensao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;

@Service
public class DimAtividadeService {

    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimProjetoRepository dimProjetoRepository;
    private final DimensionMapper dimensionMapper;

    @Autowired
    public DimAtividadeService(DimAtividadeRepository dimAtividadeRepository, 
                              DimProjetoRepository dimProjetoRepository,
                              DimensionMapper dimensionMapper) {
        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimProjetoRepository = dimProjetoRepository;
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

    public List<DimAtividadeDTO> findByProjetoId(Long projetoId) {
        List<DimAtividade> entities = dimAtividadeRepository.findByProjetoIdAndAtivoTrue(projetoId);
        return dimensionMapper.atividadeListToDTO(entities);
    }

    public DimAtividadeDTO save(DimAtividadeDTO dto) {
        DimAtividade entity = dimensionMapper.dtoToAtividade(dto);
        DimAtividade savedEntity = dimAtividadeRepository.save(entity);
        return dimensionMapper.atividadeToDTO(savedEntity);
    }

    public DimAtividadeDTO update(Long id, DimAtividadeDTO dto) {
        DimAtividade existingEntity = dimAtividadeRepository.findById(id).orElse(null);
        if (existingEntity != null) {
            existingEntity.setNome(dto.getNome());
            existingEntity.setDescricao(dto.getDescricao());
            existingEntity.setAtivo(dto.getAtivo());
            
            if (dto.getProjetoId() != null) {
                DimProjeto projeto = dimProjetoRepository.findById(dto.getProjetoId()).orElse(null);
                existingEntity.setDimProjeto(projeto);
            }
            
            DimAtividade updatedEntity = dimAtividadeRepository.save(existingEntity);
            return dimensionMapper.atividadeToDTO(updatedEntity);
        }
        return null;
    }

    public void deleteById(Long id) {
        // Soft delete - marca como inativo
        DimAtividade entity = dimAtividadeRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setAtivo(false);
            dimAtividadeRepository.save(entity);
        }
    }
}
