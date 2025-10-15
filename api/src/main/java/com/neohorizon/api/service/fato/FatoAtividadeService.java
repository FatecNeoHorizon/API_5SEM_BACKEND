package com.neohorizon.api.service.fato;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.metrica.AtividadeAggregationDTO;
import com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO;
import com.neohorizon.api.dto.response.fato.FatoAtividadeDTO;
import com.neohorizon.api.entity.fato.FatoAtividade;
import com.neohorizon.api.enums.AggregationType;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.fato.FatoAtividadeRepository;
import com.neohorizon.api.utils.ConvertStringToInteger;

@Service
public class FatoAtividadeService {

    private final FatoAtividadeRepository fatoAtividadeRepository;
    private final FatoMapper fatoMapper;

    public FatoAtividadeService(FatoAtividadeRepository fatoAtividadeRepository, FatoMapper fatoMapper) {
        this.fatoAtividadeRepository = fatoAtividadeRepository;
        this.fatoMapper = fatoMapper;
    }

    public List<FatoAtividadeDTO> getAllEntities() {

        return fatoAtividadeRepository.findAll()
                .stream()
                .map(fatoMapper::fatoAtividadeToDTO)
                .collect(Collectors.toList());
    }

    public FatoAtividadeDTO findById(Long id) {
        return fatoAtividadeRepository.findById(id)
                .map(fatoMapper::fatoAtividadeToDTO)
                .orElse(null);
    }

    public FatoAtividadeDTO save(FatoAtividadeDTO fatoAtividadeDTO) {
        FatoAtividade fatoAtividade = fatoMapper.dtoToFatoAtividade(fatoAtividadeDTO);
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
        periodoBuscado.setAnoInicio(dataInicioInteger[0]); // parts[0] é o ano
        periodoBuscado.setMesInicio(dataInicioInteger[1]); // parts[1] é o mês
        periodoBuscado.setDiaInicio(dataInicioInteger[2]); // parts[2] é o dia

        periodoBuscado.setAnoFim(dataFimInteger[0]);    // parts[0] é o ano
        periodoBuscado.setMesFim(dataFimInteger[1]);    // parts[1] é o mês
        periodoBuscado.setDiaFim(dataFimInteger[2]);    // parts[2] é o dia
    
        periodoBuscado.setPeriodo(periodoEnum);

        return fatoAtividadeRepository.findAtividadesByPeriod(periodoBuscado);
    }
}