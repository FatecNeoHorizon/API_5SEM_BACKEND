package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DimProjetoService {

    private static final String ENTITY_NAME = "DimProjeto";
    private final DimProjetoRepository dimProjetoRepository;
    private final DimensionMapper dimensionMapper;

    @Autowired
    public DimProjetoService(DimProjetoRepository dimProjetoRepository, DimensionMapper dimensionMapper) {
        this.dimProjetoRepository = dimProjetoRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<DimProjetoDTO> getAllEntities() {
        return dimProjetoRepository.findAll()
                .parallelStream()
                .map(dimensionMapper::projetoToDTO)
                .collect(Collectors.toList());
    }

    public DimProjetoDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return dimProjetoRepository.findById(id)
                .map(dimensionMapper::projetoToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public DimProjetoDTO save(DimProjetoDTO dimProjetoDTO) {
        ValidationUtils.requireNonNull(dimProjetoDTO, ENTITY_NAME + " é obrigatório");
        validateDimProjetoDTO(dimProjetoDTO);
        
        DimProjeto dimProjeto = dimensionMapper.dtoToProjeto(dimProjetoDTO);
        DimProjeto savedEntity = dimProjetoRepository.save(dimProjeto);
        return dimensionMapper.projetoToDTO(savedEntity);
    }

    public DimProjetoDTO update(Long id, DimProjetoDTO dimProjetoDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dimProjetoDTO, ENTITY_NAME + " é obrigatório para atualização");
        validateDimProjetoDTO(dimProjetoDTO);
        
        DimProjeto existingEntity = dimProjetoRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingEntity.setNome(dimProjetoDTO.getNome());
        existingEntity.setKey(dimProjetoDTO.getKey());
        existingEntity.setProjeto_jira_id(dimProjetoDTO.getProjeto_jira_id());

        DimProjeto updatedEntity = dimProjetoRepository.save(existingEntity);
        return dimensionMapper.projetoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!dimProjetoRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            dimProjetoRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateDimProjetoDTO(DimProjetoDTO dto) {
        ValidationUtils.requireNonEmpty(dto.getNome(), "Nome");
        ValidationUtils.requireNonEmpty(dto.getKey(), "Key");
        ValidationUtils.requireNonEmpty(dto.getProjeto_jira_id(), "Projeto Jira ID");
    }
}
