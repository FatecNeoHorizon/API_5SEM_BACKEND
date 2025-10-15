package com.neohorizon.api.service.dimensao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;

@Service
public class DimProjetoService {

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
        return dimProjetoRepository.findById(id)
                .map(dimensionMapper::projetoToDTO)
                .orElse(null);
    }

    public DimProjetoDTO save(DimProjetoDTO dimProjetoDTO) {
        DimProjeto dimProjeto = dimensionMapper.dtoToProjeto(dimProjetoDTO);
        DimProjeto savedEntity = dimProjetoRepository.save(dimProjeto);
        return dimensionMapper.projetoToDTO(savedEntity);
    }

    public DimProjetoDTO update(Long id, DimProjetoDTO dimProjetoDTO) {
        DimProjeto existingEntity = dimProjetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DimProjeto com ID " + id + " não encontrado."));

        existingEntity.setNome(dimProjetoDTO.getNome());
        existingEntity.setKey(dimProjetoDTO.getKey());
        existingEntity.setProjeto_jira_id(dimProjetoDTO.getProjeto_jira_id());

        DimProjeto updatedEntity = dimProjetoRepository.save(existingEntity);
        return dimensionMapper.projetoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        dimProjetoRepository.deleteById(id);
    }
}
