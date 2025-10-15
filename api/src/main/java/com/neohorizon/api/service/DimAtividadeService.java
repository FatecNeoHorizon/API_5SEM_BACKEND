package com.neohorizon.api.service;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.DimAtividadeDTO;
import com.neohorizon.api.entity.DimAtividade;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.repository.DimAtividadeRepository;
import com.neohorizon.api.repository.DimProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            List<DimAtividade> entities = dimAtividadeRepository.findByAtivoTrue();
            return entities.stream()
                    .map(this::convertToDTO)
                    .toList();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao buscar atividades: " + e.getMessage(), e);
        }
    }

    public DimAtividadeDTO findById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException(MessageConstants.ACTIVITY_ID_REQUIRED);
            }
            
            DimAtividade entity = dimAtividadeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(MessageConstants.activityNotFound(id)));
            return convertToDTO(entity);
        } catch (IllegalArgumentException e) {
            throw e; // Re-lança as exceções de validação
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao buscar atividade: " + e.getMessage(), e);
        }
    }

    public List<DimAtividadeDTO> findByProjetoId(Long projetoId) {
        try {
            if (projetoId == null) {
                throw new IllegalArgumentException(MessageConstants.PROJECT_ID_REQUIRED);
            }
            
            dimProjetoRepository.findById(projetoId)
                    .orElseThrow(() -> new IllegalArgumentException(MessageConstants.projectNotFound(projetoId)));
            
            List<DimAtividade> entities = dimAtividadeRepository.findByProjetoIdAndAtivoTrue(projetoId);
            return entities.stream()
                    .map(this::convertToDTO)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw e; // Re-lança as exceções de validação
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao buscar atividades do projeto: " + e.getMessage(), e);
        }
    }

    public DimAtividadeDTO save(DimAtividadeDTO dto) {
        try {
            DimAtividade entity = convertToEntity(dto);
            DimAtividade savedEntity = dimAtividadeRepository.save(entity);
            return convertToDTO(savedEntity);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(getDataIntegrityErrorMessage(e), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar atividade: " + e.getMessage(), e);
        }
    }

    public DimAtividadeDTO update(Long id, DimAtividadeDTO dto) {
        DimAtividade existingEntity = dimAtividadeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.activityNotFound(id)));
        
        try {
            existingEntity.setNome(dto.getNome());
            existingEntity.setDescricao(dto.getDescricao());
            existingEntity.setAtivo(dto.getAtivo());
            
            if (dto.getProjetoId() != null) {
                DimProjeto projeto = dimProjetoRepository.findById(dto.getProjetoId())
                        .orElseThrow(() -> new IllegalArgumentException(MessageConstants.projectNotFound(dto.getProjetoId())));
                existingEntity.setDimProjeto(projeto);
            }
            
            DimAtividade updatedEntity = dimAtividadeRepository.save(existingEntity);
            return convertToDTO(updatedEntity);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(getDataIntegrityErrorMessage(e), e);
        } catch (IllegalArgumentException e) {
            throw e; // Re-lança as exceções de validação
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar atividade: " + e.getMessage(), e);
        }
    }

    public void deleteById(Long id) {
        // Soft delete - marca como inativo
        DimAtividade entity = dimAtividadeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.activityNotFound(id)));
        
        try {
            entity.setAtivo(false);
            dimAtividadeRepository.save(entity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao desativar atividade: " + e.getMessage(), e);
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

    private String getDataIntegrityErrorMessage(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause().getMessage().toLowerCase();
        
        if (message.contains("unique") && message.contains("nome")) {
            return "Já existe uma atividade com esse nome no projeto";
        }
        if (message.contains("foreign key") || message.contains("projeto_id")) {
            return "Projeto especificado não existe ou foi removido";
        }
        if (message.contains("not null") && message.contains("nome")) {
            return "Nome da atividade é obrigatório";
        }
        if (message.contains("not null") && message.contains("projeto_id")) {
            return "Projeto é obrigatório para a atividade";
        }
        
        return "Erro de integridade de dados. Verifique se todos os campos obrigatórios estão preenchidos corretamente";
    }
}
