package com.neohorizon.api.service.metrica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.metrica.DevHoursMetricsDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;

@Service
public class DevHoursMetricsService {

    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;

    @Autowired
    public DevHoursMetricsService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository) {
        this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
    }

    public List<DevHoursMetricsDTO> getDevHoursMetrics(Long devId, Long activityId, 
                                                      LocalDate fromDate, LocalDate toDate) {
        
        List<FatoApontamentoHoras> apontamentos = getApontamentosByFilters(devId, activityId, fromDate, toDate);
        
        // OTIMIZAÇÃO: Usar parallelStream() para grandes volumes + collector customizado
        return apontamentos.parallelStream()
                .collect(Collectors.groupingBy(
                    a -> a.getDimDev().getId(),
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        this::processDevApontamentos
                    )
                ))
                .values()
                .stream()
                .filter(dto -> dto != null) // Filtrar nulls
                .sorted(Comparator.comparing(DevHoursMetricsDTO::getDevNome))
                .toList();
    }

       private DevHoursMetricsDTO processDevApontamentos(List<FatoApontamentoHoras> devApontamentos) {
        if (devApontamentos.isEmpty()) {
            return null;
        }
    
        DimDev dev = devApontamentos.get(0).getDimDev();
    
        Map<Long, List<DevHoursMetricsDTO.DiaHorasDTO>> atividadeMap = new HashMap<>();
        Map<Long, Double> horasPorAtividade = new HashMap<>();
        Map<Long, DimAtividade> atividadeCache = new HashMap<>();
        double totalHorasGeral = 0.0;
    
        for (FatoApontamentoHoras apontamento : devApontamentos) {
            Long atividadeId = apontamento.getDimAtividade().getId();
            double horas = apontamento.getHorasTrabalhadas();
    
            totalHorasGeral += horas;
            horasPorAtividade.merge(atividadeId, horas, Double::sum);
            atividadeCache.putIfAbsent(atividadeId, apontamento.getDimAtividade());
    
            DevHoursMetricsDTO.DiaHorasDTO diaDTO = DevHoursMetricsDTO.DiaHorasDTO.builder()
                .data(apontamento.getDataAtualizacao().toLocalDate())
                .horas(horas)
                .descricaoTrabalho(apontamento.getDescricaoTrabalho())
                .build();
    
            atividadeMap.computeIfAbsent(atividadeId, k -> new ArrayList<>()).add(diaDTO);
        }
    
        List<DevHoursMetricsDTO.AtividadeHorasDTO> atividades = atividadeMap.entrySet().stream()
            .map(entry -> {
                Long atividadeId = entry.getKey();
                List<DevHoursMetricsDTO.DiaHorasDTO> dias = entry.getValue();
                DimAtividade atividade = atividadeCache.get(atividadeId);
    
                String atividadeNome = atividade != null ? atividade.getNome() : "Atividade desconhecida";
                Double totalHoras = horasPorAtividade.get(atividadeId);
                if (totalHoras == null) {
                    totalHoras = 0.0;
                }
    
                return DevHoursMetricsDTO.AtividadeHorasDTO.builder()
                    .atividadeId(atividadeId)
                    .atividadeNome(atividadeNome)
                    .totalHoras(totalHoras)
                    .diasApontamentos(dias)
                    .build();
            })
            .sorted(Comparator.comparing(DevHoursMetricsDTO.AtividadeHorasDTO::getAtividadeNome))
            .toList();
    
        return DevHoursMetricsDTO.builder()
            .devId(dev.getId())
            .devNome(dev.getNome())
            .totalHoras(totalHorasGeral)
            .atividades(atividades)
            .build();
    }

    private List<FatoApontamentoHoras> getApontamentosByFilters(Long devId, Long activityId, 
                                                              LocalDate fromDate, LocalDate toDate) {
        // Converter LocalDate para LocalDateTime (início do dia e fim do dia)
        java.time.LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        java.time.LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59) : LocalDate.now().atTime(23, 59, 59);

        if (devId != null && activityId != null) {
            return fatoApontamentoHorasRepository.findByDevAtividadeAndPeriodo(devId, activityId, fromDateTime, toDateTime);
        } else if (devId != null) {
            return fatoApontamentoHorasRepository.findByDevAndPeriodo(devId, fromDateTime, toDateTime);
        } else if (activityId != null) {
            return fatoApontamentoHorasRepository.findByAtividadeAndPeriodo(activityId, fromDateTime, toDateTime);
        } else {
            return fatoApontamentoHorasRepository.findByPeriodo(fromDateTime, toDateTime);
        }
    }
}
