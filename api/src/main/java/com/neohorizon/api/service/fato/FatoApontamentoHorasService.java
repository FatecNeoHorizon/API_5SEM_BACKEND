package com.neohorizon.api.service.fato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;

@Service
public class FatoApontamentoHorasService {

    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;
    private final FatoMapper fatoMapper;

    @Autowired
    public FatoApontamentoHorasService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository,
                                     DimDevRepository dimDevRepository,
                                     DimAtividadeRepository dimAtividadeRepository,
                                     DimProjetoRepository dimProjetoRepository,
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
        FatoApontamentoHoras entity = fatoApontamentoHorasRepository.findById(id).orElse(null);
        return entity != null ? fatoMapper.fatoApontamentoToDTO(entity) : null;
    }

    public List<FatoApontamentoHorasDTO> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        java.time.LocalDateTime inicio = (dataInicio != null) ? dataInicio.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        java.time.LocalDateTime fim = (dataFim != null) ? dataFim.atTime(23, 59, 59) : LocalDate.now().atTime(23, 59, 59);
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findByPeriodo(inicio, fim);
    return entities.stream()
        .map(fatoMapper::fatoApontamentoToDTO)
        .toList();
    }

    @Transactional
    public FatoApontamentoHorasDTO create(FatoApontamentoHorasDTO dto) {
        FatoApontamentoHoras entity = fatoMapper.dtoToFatoApontamento(dto);
        entity = fatoApontamentoHorasRepository.save(entity);
        return fatoMapper.fatoApontamentoToDTO(entity);
    }

    @Transactional
    public FatoApontamentoHorasDTO update(Long id, FatoApontamentoHorasDTO dto) {
    FatoApontamentoHoras existingEntity = fatoApontamentoHorasRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(
            MessageConstants.appointmentNotFound(id)));
    fatoMapper.fatoApontamentoToDTO(existingEntity);
    FatoApontamentoHoras updatedEntity = fatoApontamentoHorasRepository.save(existingEntity);
    return fatoMapper.fatoApontamentoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        fatoApontamentoHorasRepository.deleteById(id);
    }
}
