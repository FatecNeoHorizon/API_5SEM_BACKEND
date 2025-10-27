package com.neohorizon.api.service.fato;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.metrica.AtividadeAggregationDTO;
import com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO;
import com.neohorizon.api.dto.response.fato.FatoAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.entity.dimensao.DimTipo;
import com.neohorizon.api.entity.fato.FatoAtividade;
import com.neohorizon.api.enums.AggregationType;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.dimensao.DimStatusRepository;
import com.neohorizon.api.repository.dimensao.DimTipoRepository;
import com.neohorizon.api.repository.fato.FatoAtividadeRepository;
import com.neohorizon.api.utils.ConvertStringToInteger;
import static com.neohorizon.api.utils.ValidationUtils.requireValidId;
import static com.neohorizon.api.utils.ValidationUtils.validateFatoAtividade;

@Service
public class FatoAtividadeService {

    private static final String ENTITY_NAME = "FatoAtividade";
    private final FatoAtividadeRepository fatoAtividadeRepository;
    private final FatoMapper fatoMapper;
    private final DimProjetoRepository dimProjetoRepository;
    private final DimPeriodoRepository dimPeriodoRepository;
    private final DimStatusRepository dimStatusRepository;
    private final DimTipoRepository dimTipoRepository;
    private final DimensionMapper dimensionMapper;

    public FatoAtividadeService(FatoAtividadeRepository fatoAtividadeRepository,
            FatoMapper fatoMapper,
            DimProjetoRepository dimProjetoRepository,
            DimPeriodoRepository dimPeriodoRepository,
            DimStatusRepository dimStatusRepository,
            DimTipoRepository dimTipoRepository,
            DimensionMapper dimensionMapper) {
        this.fatoAtividadeRepository = fatoAtividadeRepository;
        this.fatoMapper = fatoMapper;
        this.dimProjetoRepository = dimProjetoRepository;
        this.dimPeriodoRepository = dimPeriodoRepository;
        this.dimStatusRepository = dimStatusRepository;
        this.dimTipoRepository = dimTipoRepository;
        this.dimensionMapper = dimensionMapper;
    }

    public List<FatoAtividadeDTO> getAllEntities() {

        return fatoAtividadeRepository.findAll()
                .stream()
                .map(fatoMapper::fatoAtividadeToDTO)
                .collect(Collectors.toList());
    }

    public FatoAtividadeDTO findById(Long id) {
        requireValidId(id, ENTITY_NAME);
        
        return fatoAtividadeRepository.findById(id)
                .map(fatoMapper::fatoAtividadeToDTO)
                .orElse(null);
    }

    public FatoAtividadeDTO create(FatoAtividadeDTO fatoAtividadeDTO) {
        validateFatoAtividade(fatoAtividadeDTO);

        Long projetoId = fatoAtividadeDTO.getDimProjeto().getId();
        Long periodoId = fatoAtividadeDTO.getDimPeriodo().getId();
        Long statusId = fatoAtividadeDTO.getDimStatus().getId();
        Long tipoId = fatoAtividadeDTO.getDimTipo().getId();


        DimProjeto projeto = dimProjetoRepository.findById(projetoId)
            .orElseGet(() -> dimensionMapper.dtoToProjeto(fatoAtividadeDTO.getDimProjeto()));
        DimPeriodo periodo = dimPeriodoRepository.findById(periodoId)
            .orElseGet(() -> dimensionMapper.dtoToPeriodo(fatoAtividadeDTO.getDimPeriodo()));
        DimStatus status = dimStatusRepository.findById(statusId)
            .orElseGet(() -> dimensionMapper.dtoToStatus(fatoAtividadeDTO.getDimStatus()));
        DimTipo tipo = dimTipoRepository.findById(tipoId)
            .orElseGet(() -> dimensionMapper.dtoToTipo(fatoAtividadeDTO.getDimTipo()));

        FatoAtividade fatoAtividade = fatoMapper.dtoToFatoAtividade(fatoAtividadeDTO);
        fatoAtividade.setDimProjeto(projeto);
        fatoAtividade.setDimPeriodo(periodo);
        fatoAtividade.setDimStatus(status);
        fatoAtividade.setDimTipo(tipo);

        FatoAtividade savedEntity = fatoAtividadeRepository.save(fatoAtividade);
        return fatoMapper.fatoAtividadeToDTO(savedEntity);
    }

    public FatoAtividadeDTO update(Long id, FatoAtividadeDTO fatoAtividadeDTO) {
        FatoAtividade existingEntity = fatoAtividadeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FatoAtividade with ID " + id + " not found."));
        existingEntity.setQuantidade(fatoAtividadeDTO.getQuantidade());

        FatoAtividade updatedEntity = fatoAtividadeRepository.save(existingEntity);
        return fatoMapper.fatoAtividadeToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        fatoAtividadeRepository.deleteById(id);
    }

    public Long getTotalAtividades() {
        return fatoAtividadeRepository.countAllAtividades();
    }

    public List<ProjectAtividadeCountDTO> getAtividadesByProject(Long projectId) {
            if (projectId != null) {
                return fatoAtividadeRepository.findAtividadeByProject(projectId);
            } 
            return fatoAtividadeRepository.findAllProjectAtividades();
        }

    public List<ProjectAtividadeCountDTO> getAllProjectAtividades() {
        return fatoAtividadeRepository.findAllProjectAtividades();
    }

    public List<ProjectAtividadeCountDTO> getAtividadesByAggregation(String dataInicio, String dataFim, String periodo) {

        Integer[] dataInicioInteger = ConvertStringToInteger.convertStringDateToInteger(dataInicio);
        Integer[] dataFimInteger = ConvertStringToInteger.convertStringDateToInteger(dataFim);
        AggregationType periodoEnum = AggregationType.fromString(periodo);

        AtividadeAggregationDTO periodoBuscado = new AtividadeAggregationDTO();
        periodoBuscado.setAnoInicio(dataInicioInteger[0]);
        periodoBuscado.setMesInicio(dataInicioInteger[1]);
        periodoBuscado.setDiaInicio(dataInicioInteger[2]);

        periodoBuscado.setAnoFim(dataFimInteger[0]);
        periodoBuscado.setMesFim(dataFimInteger[1]);
        periodoBuscado.setDiaFim(dataFimInteger[2]);

        periodoBuscado.setPeriodo(periodoEnum);

        return fatoAtividadeRepository.findAtividadesByPeriod(periodoBuscado);
    }
}