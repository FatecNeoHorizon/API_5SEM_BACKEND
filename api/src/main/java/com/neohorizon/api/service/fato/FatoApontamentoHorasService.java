package com.neohorizon.api.service.fato;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;
import static com.neohorizon.api.utils.ValidationUtils.require;
import static com.neohorizon.api.utils.ValidationUtils.requireNonNull;
import static com.neohorizon.api.utils.ValidationUtils.requireValidId;
import static com.neohorizon.api.utils.ValidationUtils.validateFatoApontamentoHorasDTO;

@Service
public class FatoApontamentoHorasService {

    private static final String ENTITY_NAME = "FatoApontamentoHoras";
    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;
    private final FatoMapper fatoMapper;
    private final DimPeriodoRepository dimPeriodoRepository;
    private final DimProjetoRepository dimProjetoRepository;
    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimDevRepository dimDevRepository;
    private final DimensionMapper dimensionMapper;

    public FatoApontamentoHorasService(
            FatoApontamentoHorasRepository fatoApontamentoHorasRepository,
            FatoMapper fatoMapper,
            DimPeriodoRepository dimPeriodoRepository,
            DimDevRepository dimDevRepository,
            DimAtividadeRepository dimAtividadeRepository,
            DimProjetoRepository dimProjetoRepository,
            DimensionMapper dimensionMapper) {
            this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
        this.fatoMapper = fatoMapper;
        this.dimPeriodoRepository = dimPeriodoRepository;
        this.dimProjetoRepository = dimProjetoRepository;
        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimDevRepository = dimDevRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<FatoApontamentoHorasDTO> getAllEntities() {

        return fatoApontamentoHorasRepository.findAll()
                .stream()
                .map(fatoMapper::fatoApontamentoToDTO)
                .collect(Collectors.toList());
    }

    public FatoApontamentoHorasDTO findById(Long id) {
        requireValidId(id, ENTITY_NAME);
        
        return fatoApontamentoHorasRepository.findById(id)
                .map(fatoMapper::fatoApontamentoToDTO)
                .orElse(null);  
    }

    public List<FatoApontamentoHorasDTO> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        requireNonNull(dataInicio, "Data de início");
        requireNonNull(dataFim, "Data de fim");
        require(dataFim.isAfter(dataInicio) || dataFim.isEqual(dataInicio), 
            "Data fim deve ser posterior ou igual à data início");
        
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(23, 59, 59);
        List<FatoApontamentoHoras> entities = fatoApontamentoHorasRepository.findByPeriodo(inicio, fim);
        return entities.stream()
            .map(fatoMapper::fatoApontamentoToDTO)
            .collect(Collectors.toList());
    }

    public FatoApontamentoHorasDTO create(FatoApontamentoHorasDTO fatoApontamentoHorasDTO) {
        validateFatoApontamentoHorasDTO(fatoApontamentoHorasDTO);
        requireNonNull(fatoApontamentoHorasDTO, ENTITY_NAME + " é obrigatório para criação");
       
        Long periodoId = fatoApontamentoHorasDTO.getDimPeriodo().getId();
        Long devId = fatoApontamentoHorasDTO.getDimDev().getId();
        Long atividadeId = fatoApontamentoHorasDTO.getDimAtividade().getId();
        Long projetoId = fatoApontamentoHorasDTO.getDimProjeto().getId();
        
        DimPeriodo periodo = dimPeriodoRepository.findById(periodoId)
            .orElseGet(() -> dimensionMapper.dtoToPeriodo(fatoApontamentoHorasDTO.getDimPeriodo()));
        DimDev dev = dimDevRepository.findById(devId)
            .orElseGet(() -> dimensionMapper.dtoToDev(fatoApontamentoHorasDTO.getDimDev()));
        DimAtividade atividade = dimAtividadeRepository.findById(atividadeId)
            .orElseGet(() -> dimensionMapper.dtoToAtividade(fatoApontamentoHorasDTO.getDimAtividade()));
        DimProjeto projeto = dimProjetoRepository.findById(projetoId)
            .orElseGet(() -> dimensionMapper.dtoToProjeto(fatoApontamentoHorasDTO.getDimProjeto()));
        
        FatoApontamentoHoras fatoApontamentoHoras = fatoMapper.dtoToFatoApontamento(fatoApontamentoHorasDTO);
        fatoApontamentoHoras.setDimPeriodo(periodo);
        fatoApontamentoHoras.setDimDev(dev);
        fatoApontamentoHoras.setDimAtividade(atividade);
        fatoApontamentoHoras.setDimProjeto(projeto);

        FatoApontamentoHoras savedEntity = fatoApontamentoHorasRepository.save(fatoApontamentoHoras);
        return fatoMapper.fatoApontamentoToDTO(savedEntity);
    }

    public FatoApontamentoHorasDTO update(Long id, FatoApontamentoHorasDTO dto) {
        requireValidId(id, ENTITY_NAME);
        requireNonNull(dto, ENTITY_NAME + " é obrigatório para atualização");
        validateFatoApontamentoHorasDTO(dto);

        FatoApontamentoHoras existingEntity = fatoApontamentoHorasRepository.findById(id)
            .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
             
        FatoApontamentoHoras updatedEntity = fatoApontamentoHorasRepository.save(existingEntity);
        return fatoMapper.fatoApontamentoToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        requireValidId(id, ENTITY_NAME);
        
        if (!fatoApontamentoHorasRepository.existsById(id)) {
            throw EntityNotFoundException.forId(ENTITY_NAME, id);
        }
        
        fatoApontamentoHorasRepository.deleteById(id);
    }

}