package com.neohorizon.api.service;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.FatoApontamentoHoras;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimAtividade;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.repository.FatoApontamentoHorasRepository;
import com.neohorizon.api.repository.DimDevRepository;
import com.neohorizon.api.repository.DimAtividadeRepository;
import com.neohorizon.api.repository.DimProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FatoApontamentoHorasService {

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
        FatoApontamentoHoras entity = fatoApontamentoHorasRepository.findById(id).orElse(null);
        return entity != null ? convertToDTO(entity) : null;
    }

    public List<FatoApontamentoHorasDTO> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findByPeriodo(dataInicio, dataFim);
        return entities.stream()
                .map(this::convertToDTO)
                .toList();
    }

        @Transactional
    public FatoApontamentoHorasDTO create(FatoApontamentoHorasDTO dto) {
        validateRequiredFields(dto);
        FatoApontamentoHoras entity = convertToEntity(dto);
        entity = fatoApontamentoHorasRepository.save(entity);
        return convertToDTO(entity);
    }

    @Transactional
    public FatoApontamentoHorasDTO update(Long id, FatoApontamentoHorasDTO dto) {
        FatoApontamentoHoras existingEntity = fatoApontamentoHorasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    MessageConstants.appointmentNotFound(id)));
        
        updateEntityFromDTO(existingEntity, dto);
        FatoApontamentoHoras updatedEntity = fatoApontamentoHorasRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        fatoApontamentoHorasRepository.deleteById(id);
    }

    private void validateRequiredFields(FatoApontamentoHorasDTO dto) {
        if (dto.getDevId() == null) {
            throw new IllegalArgumentException("ID do desenvolvedor é obrigatório");
        }
        if (dto.getAtividadeId() == null) {
            throw new IllegalArgumentException("ID da atividade é obrigatório");
        }
        if (dto.getProjetoId() == null) {
            throw new IllegalArgumentException("ID do projeto é obrigatório");
        }
        if (dto.getDataApontamento() == null) {
            throw new IllegalArgumentException("Data do apontamento é obrigatória");
        }
        if (dto.getHorasTrabalhadas() == null || dto.getHorasTrabalhadas() <= 0) {
            throw new IllegalArgumentException("Horas trabalhadas deve ser maior que zero");
        }
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
                .orElseThrow(() -> new IllegalArgumentException(
                    MessageConstants.developerNotFound(dto.getDevId())));
        
        // Validar e buscar atividade
        DimAtividade atividade = dimAtividadeRepository.findById(dto.getAtividadeId())
                .orElseThrow(() -> new IllegalArgumentException(
                    MessageConstants.activityNotFound(dto.getAtividadeId())));
        
        // Validar e buscar projeto
        DimProjeto projeto = dimProjetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new IllegalArgumentException(
                    MessageConstants.projectNotFound(dto.getProjetoId())));

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
                    .orElseThrow(() -> new IllegalArgumentException(
                        MessageConstants.developerNotFound(dto.getDevId())));
            entity.setDimDev(dev);
        }
        if (dto.getAtividadeId() != null) {
            DimAtividade atividade = dimAtividadeRepository.findById(dto.getAtividadeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        MessageConstants.activityNotFound(dto.getAtividadeId())));
            entity.setDimAtividade(atividade);
        }
        if (dto.getProjetoId() != null) {
            DimProjeto projeto = dimProjetoRepository.findById(dto.getProjetoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        MessageConstants.projectNotFound(dto.getProjetoId())));
            entity.setDimProjeto(projeto);
        }
        if (dto.getDataApontamento() != null) {
            entity.setDataApontamento(dto.getDataApontamento());
        }
        if (dto.getHorasTrabalhadas() != null) {
            entity.setHorasTrabalhadas(dto.getHorasTrabalhadas());
        }
        if (dto.getDescricaoTrabalho() != null) {
            entity.setDescricaoTrabalho(dto.getDescricaoTrabalho());
        }
    }
}
