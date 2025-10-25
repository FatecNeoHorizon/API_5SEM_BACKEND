package com.neohorizon.api.service.fato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class FatoApontamentoHorasService {

    private static final String ENTITY_NAME = "FatoApontamentoHoras";
    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;
    private final FatoMapper fatoMapper;

    public FatoApontamentoHorasService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository,
                                      FatoMapper fatoMapper) {
        this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
        this.fatoMapper = fatoMapper;
    }

    public List<FatoApontamentoHorasDTO> getAllEntities() {
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findAll();
        return entities.parallelStream()
                .map(fatoMapper::fatoApontamentoToDTO)
                .toList();
    }

    public FatoApontamentoHorasDTO findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        FatoApontamentoHoras entity = fatoApontamentoHorasRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        return fatoMapper.fatoApontamentoToDTO(entity);
    }

    public List<FatoApontamentoHorasDTO> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        ValidationUtils.requireNonNull(dataInicio, "Data de início");
        ValidationUtils.requireNonNull(dataFim, "Data de fim");
        ValidationUtils.require(dataFim.isAfter(dataInicio) || dataFim.isEqual(dataInicio), 
            "Data fim deve ser posterior ou igual à data início");
        
        java.time.LocalDateTime inicio = dataInicio.atStartOfDay();
        java.time.LocalDateTime fim = dataFim.atTime(23, 59, 59);
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findByPeriodo(inicio, fim);
        return entities.stream()
            .map(fatoMapper::fatoApontamentoToDTO)
            .toList();
    }

    @Transactional
    public FatoApontamentoHorasDTO create(FatoApontamentoHorasDTO dto) {
        ValidationUtils.requireNonNull(dto, ENTITY_NAME + " é obrigatório");
        
        FatoApontamentoHoras entity = fatoMapper.dtoToFatoApontamento(dto);
        entity = fatoApontamentoHorasRepository.save(entity);
        return fatoMapper.fatoApontamentoToDTO(entity);
    }

    @Transactional
    public FatoApontamentoHorasDTO update(Long id, FatoApontamentoHorasDTO dto) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonNull(dto, ENTITY_NAME + " é obrigatório para atualização");
        
        FatoApontamentoHoras existingEntity = fatoApontamentoHorasRepository.findById(id)
            .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        
        if (dto.getDimDev() != null) {
            existingEntity.setDimDev(fatoMapper.dtoToFatoApontamento(dto).getDimDev());
        }
        if (dto.getDimAtividade() != null) {
            existingEntity.setDimAtividade(fatoMapper.dtoToFatoApontamento(dto).getDimAtividade());
        }
        if (dto.getDimProjeto() != null) {
            existingEntity.setDimProjeto(fatoMapper.dtoToFatoApontamento(dto).getDimProjeto());
        }
        if (dto.getDimPeriodo() != null) {
            existingEntity.setDimPeriodo(fatoMapper.dtoToFatoApontamento(dto).getDimPeriodo());
        }
        if (dto.getHorasTrabalhadas() != null) {
            existingEntity.setHorasTrabalhadas(dto.getHorasTrabalhadas());
        }
        if (dto.getDescricaoTrabalho() != null) {
            existingEntity.setDescricaoTrabalho(dto.getDescricaoTrabalho());
        }
        
        FatoApontamentoHoras updatedEntity = fatoApontamentoHorasRepository.save(existingEntity);
        return fatoMapper.fatoApontamentoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        
        if (!fatoApontamentoHorasRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        fatoApontamentoHorasRepository.deleteById(id);
    }
}
