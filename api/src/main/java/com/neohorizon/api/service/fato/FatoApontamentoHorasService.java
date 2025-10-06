package com.neohorizon.api.service.fato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;

@Service
public class FatoApontamentoHorasService {

    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;
    private final DimDevRepository dimDevRepository;
    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimProjetoRepository dimProjetoRepository;
    private final FatoMapper fatoMapper;

    @Autowired
    public FatoApontamentoHorasService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository,
                                     DimDevRepository dimDevRepository,
                                     DimAtividadeRepository dimAtividadeRepository,
                                     DimProjetoRepository dimProjetoRepository,
                                     FatoMapper fatoMapper) {
        this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
        this.dimDevRepository = dimDevRepository;
        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimProjetoRepository = dimProjetoRepository;
        this.fatoMapper = fatoMapper;
    }

    public List<FatoApontamentoHorasDTO> getAllEntities() {
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findAll();
        // OTIMIZAÇÃO: parallelStream() + MapStruct mapper
        return entities.parallelStream()
                .map(fatoMapper::apontamentoToDTO)
                .toList();
    }

    public FatoApontamentoHorasDTO findById(Long id) {
        FatoApontamentoHoras entity = fatoApontamentoHorasRepository.findById(id).orElse(null);
        return entity != null ? fatoMapper.apontamentoToDTO(entity) : null;
    }

    public List<FatoApontamentoHorasDTO> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findByPeriodo(dataInicio, dataFim);
        // OTIMIZAÇÃO: parallelStream() + MapStruct mapper
        return entities.parallelStream()
                .map(fatoMapper::apontamentoToDTO)
                .toList();
    }

        @Transactional
    public FatoApontamentoHorasDTO create(FatoApontamentoHorasDTO dto) {
        validateRequiredFields(dto);
        FatoApontamentoHoras entity = fatoMapper.dtoToApontamento(dto);
        entity = fatoApontamentoHorasRepository.save(entity);
        return fatoMapper.apontamentoToDTO(entity);
    }

    @Transactional
    public FatoApontamentoHorasDTO update(Long id, FatoApontamentoHorasDTO dto) {
        FatoApontamentoHoras existingEntity = fatoApontamentoHorasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    MessageConstants.appointmentNotFound(id)));
        
        updateEntityFromDTO(existingEntity, dto);
        FatoApontamentoHoras updatedEntity = fatoApontamentoHorasRepository.save(existingEntity);
        return fatoMapper.apontamentoToDTO(updatedEntity);
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
