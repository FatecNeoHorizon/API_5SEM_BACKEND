package com.neohorizon.api.service;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.DimAtividadeDTO;
import com.neohorizon.api.entity.DimAtividade;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.repository.DimAtividadeRepository;
import com.neohorizon.api.repository.DimProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimAtividadeService {

    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimProjetoRepository dimProjetoRepository;

    @Autowired
    public DimAtividadeService(DimAtividadeRepository dimAtividadeRepository, 
                              DimProjetoRepository dimProjetoRepository) {
        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimProjetoRepository = dimProjetoRepository;
    }

    public List<DimAtividadeDTO> getAllEntities() {
        List<DimAtividade> entities = dimAtividadeRepository.findByAtivoTrue();
        return entities.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimAtividadeDTO findById(Long id) {
        DimAtividade entity = dimAtividadeRepository.findById(id).orElse(null);
        return entity != null ? convertToDTO(entity) : null;
    }

    public List<DimAtividadeDTO> findByProjetoId(Long projetoId) {
        List<DimAtividade> entities = dimAtividadeRepository.findByProjetoIdAndAtivoTrue(projetoId);
        return entities.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DimAtividadeDTO save(DimAtividadeDTO dto) {
        DimAtividade entity = convertToEntity(dto);
        DimAtividade savedEntity = dimAtividadeRepository.save(entity);
        return convertToDTO(savedEntity);
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
            return convertToDTO(updatedEntity);
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

    private DimAtividadeDTO convertToDTO(DimAtividade entity) {
        return DimAtividadeDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .projetoId(entity.getDimProjeto() != null ? entity.getDimProjeto().getId() : null)
                .projetoNome(entity.getDimProjeto() != null ? entity.getDimProjeto().getNome() : null)
                .ativo(entity.getAtivo())
                .build();
    }

    private DimAtividade convertToEntity(DimAtividadeDTO dto) {
        DimProjeto projeto = null;
        if (dto.getProjetoId() != null) {
            projeto = dimProjetoRepository.findById(dto.getProjetoId()).orElse(null);

            if (projeto == null) {
                throw new IllegalArgumentException(
                    MessageConstants.projectNotFound(dto.getProjetoId())
                );
            }
        }

        return DimAtividade.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .dimProjeto(projeto)
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : Boolean.TRUE)
                .build();
    }
}
