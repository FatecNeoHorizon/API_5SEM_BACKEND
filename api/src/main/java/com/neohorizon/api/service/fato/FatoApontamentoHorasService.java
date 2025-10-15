package com.neohorizon.api.service.fato;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.FatoApontamentoHoras;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimAtividade;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.FatoApontamentoHorasRepository;
import com.neohorizon.api.repository.DimDevRepository;
import com.neohorizon.api.repository.DimAtividadeRepository;
import com.neohorizon.api.repository.DimProjetoRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FatoApontamentoHorasService {

    private static final String ENTITY_NAME = "FatoApontamentoHoras";
    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;
    private final DimDevRepository dimDevRepository;
    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimProjetoRepository dimProjetoRepository;

    @Autowired
    public FatoApontamentoHorasService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository,
                                     DimDevRepository dimDevRepository,
                                     DimAtividadeRepository dimAtividadeRepository,
                                     DimProjetoRepository dimProjetoRepository) {
        this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
        this.dimDevRepository = dimDevRepository;
        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimProjetoRepository = dimProjetoRepository;
    }

    public List<FatoApontamentoHorasDTO> getAllEntities() {
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findAll();
        return entities.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public FatoApontamentoHorasDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        return fatoApontamentoHorasRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public List<FatoApontamentoHorasDTO> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        ValidationUtils.requireNonNull(dataInicio, "Data de início");
        ValidationUtils.requireNonNull(dataFim, "Data de fim");
        ValidationUtils.require(dataFim.isAfter(dataInicio) || dataFim.isEqual(dataInicio), 
            "Data fim deve ser posterior ou igual à data início");
        
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findByPeriodo(dataInicio, dataFim);
        return entities.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public FatoApontamentoHorasDTO create(FatoApontamentoHorasDTO dto) {
        ValidationUtils.requireNonNull(dto, ENTITY_NAME + " é obrigatório");
        validateRequiredFields(dto);
        
        FatoApontamentoHoras entity = convertToEntity(dto);
        entity = fatoApontamentoHorasRepository.save(entity);
        return convertToDTO(entity);
    }

    @Transactional
    public FatoApontamentoHorasDTO update(Long id, FatoApontamentoHorasDTO dto) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dto, ENTITY_NAME + " é obrigatório para atualização");
        
        FatoApontamentoHoras existingEntity = fatoApontamentoHorasRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        
        updateEntityFromDTO(existingEntity, dto);
        FatoApontamentoHoras updatedEntity = fatoApontamentoHorasRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!fatoApontamentoHorasRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        try {
            fatoApontamentoHorasRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar " + ENTITY_NAME + ": " + e.getMessage(), e);
        }
    }

    private void validateRequiredFields(FatoApontamentoHorasDTO dto) {
        ValidationUtils.requireNonNull(dto.getDevId(), MessageConstants.DEVELOPER_ID_REQUIRED);
        ValidationUtils.requireNonNull(dto.getAtividadeId(), MessageConstants.ACTIVITY_ID_REQUIRED);
        ValidationUtils.requireNonNull(dto.getProjetoId(), MessageConstants.PROJECT_ID_REQUIRED);
        ValidationUtils.requireNonNull(dto.getDataApontamento(), MessageConstants.DATE_REQUIRED);
        ValidationUtils.require(dto.getHorasTrabalhadas() != null && dto.getHorasTrabalhadas() > 0, 
            MessageConstants.HOURS_INVALID);
    }

    private FatoApontamentoHorasDTO convertToDTO(FatoApontamentoHoras entity) {
        return FatoApontamentoHorasDTO.builder()
                .id(entity.getId())
                .devId(entity.getDimDev().getId())
                .devNome(entity.getDimDev().getNome())
                .atividadeId(entity.getDimAtividade().getId())
                .atividadeNome(entity.getDimAtividade().getNome())
                .projetoId(entity.getDimProjeto().getId())
                .projetoNome(entity.getDimProjeto().getNome())
                .dataApontamento(entity.getDataApontamento())
                .horasTrabalhadas(entity.getHorasTrabalhadas())
                .descricaoTrabalho(entity.getDescricaoTrabalho())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }

    private FatoApontamentoHoras convertToEntity(FatoApontamentoHorasDTO dto) {
        // Validar e buscar desenvolvedor
        DimDev dev = dimDevRepository.findById(dto.getDevId())
                .orElseThrow(() -> EntityNotFoundException.forId("DimDev", dto.getDevId()));
        
        // Validar e buscar atividade
        DimAtividade atividade = dimAtividadeRepository.findById(dto.getAtividadeId())
                .orElseThrow(() -> EntityNotFoundException.forId("DimAtividade", dto.getAtividadeId()));
        
        // Validar e buscar projeto
        DimProjeto projeto = dimProjetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> EntityNotFoundException.forId("DimProjeto", dto.getProjetoId()));

        return FatoApontamentoHoras.builder()
                .id(dto.getId())
                .dimDev(dev)
                .dimAtividade(atividade)
                .dimProjeto(projeto)
                .dataApontamento(dto.getDataApontamento())
                .horasTrabalhadas(dto.getHorasTrabalhadas())
                .descricaoTrabalho(dto.getDescricaoTrabalho())
                .build();
    }

    private void updateEntityFromDTO(FatoApontamentoHoras entity, FatoApontamentoHorasDTO dto) {
        if (dto.getDevId() != null) {
            DimDev dev = dimDevRepository.findById(dto.getDevId())
                    .orElseThrow(() -> EntityNotFoundException.forId("DimDev", dto.getDevId()));
            entity.setDimDev(dev);
        }
        if (dto.getAtividadeId() != null) {
            DimAtividade atividade = dimAtividadeRepository.findById(dto.getAtividadeId())
                    .orElseThrow(() -> EntityNotFoundException.forId("DimAtividade", dto.getAtividadeId()));
            entity.setDimAtividade(atividade);
        }
        if (dto.getProjetoId() != null) {
            DimProjeto projeto = dimProjetoRepository.findById(dto.getProjetoId())
                    .orElseThrow(() -> EntityNotFoundException.forId("DimProjeto", dto.getProjetoId()));
            entity.setDimProjeto(projeto);
        }
        if (dto.getDataApontamento() != null) {
            entity.setDataApontamento(dto.getDataApontamento());
        }
        if (dto.getHorasTrabalhadas() != null) {
            ValidationUtils.require(dto.getHorasTrabalhadas() > 0, MessageConstants.HOURS_INVALID);
            entity.setHorasTrabalhadas(dto.getHorasTrabalhadas());
        }
        if (dto.getDescricaoTrabalho() != null) {
            entity.setDescricaoTrabalho(dto.getDescricaoTrabalho());
        }
    }
}
