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

    /**
     * OTIMIZAÇÃO: Método otimizado para processar apontamentos de um desenvolvedor
     * Evita loops aninhados e múltiplas iterações
     */
    private DevHoursMetricsDTO processDevApontamentos(List<FatoApontamentoHoras> devApontamentos) {
        if (devApontamentos.isEmpty()) {
            return null;
        }
        
        DimDev dev = devApontamentos.get(0).getDimDev();
        
        // OTIMIZAÇÃO: Calcular totais e agrupar em uma única passada
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
                    .data(apontamento.getDataApontamento())
                    .horas(horas)
                    .descricaoTrabalho(apontamento.getDescricaoTrabalho())
                    .build();
                    
            atividadeMap.computeIfAbsent(atividadeId, k -> new ArrayList<>()).add(diaDTO);
        }
        
        // OTIMIZAÇÃO: Construir DTOs de atividade de forma eficiente
        List<DevHoursMetricsDTO.AtividadeHorasDTO> atividades = atividadeMap.entrySet()
                .parallelStream()
                .map(entry -> {
                    Long atividadeId = entry.getKey();
                    List<DevHoursMetricsDTO.DiaHorasDTO> dias = entry.getValue();
                    DimAtividade atividade = atividadeCache.get(atividadeId);
                    
                    // Ordenar dias por data (mais recente primeiro)
                    dias.sort(Comparator.comparing(DevHoursMetricsDTO.DiaHorasDTO::getData).reversed());
                    
                    return DevHoursMetricsDTO.AtividadeHorasDTO.builder()
                            .atividadeId(atividadeId)
                            .atividadeNome(atividade.getNome())
                            .projetoNome(atividade.getDimProjeto().getNome())
                            .totalHoras(horasPorAtividade.get(atividadeId))
                            .diasApontamentos(dias)
                            .build();
                })
                .sorted(Comparator.comparing(DevHoursMetricsDTO.AtividadeHorasDTO::getAtividadeNome))
                .toList();

        return DevHoursMetricsDTO.builder()
                .devId(dev.getId())
                .devNome(dev.getNome())
                .devEmail(dev.getEmail())
                .totalHoras(totalHorasGeral)
                .atividades(atividades)
                .build();
    }

    private List<FatoApontamentoHoras> getApontamentosByFilters(Long devId, Long activityId, 
                                                              LocalDate fromDate, LocalDate toDate) {
        
        // Se não especificou datas, usar últimos 30 dias
        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(30);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        // Aplicar filtros baseado nos parâmetros
        if (devId != null && activityId != null) {
            return fatoApontamentoHorasRepository.findByDevAtividadeAndPeriodo(devId, activityId, fromDate, toDate);
        } else if (devId != null) {
            return fatoApontamentoHorasRepository.findByDevAndPeriodo(devId, fromDate, toDate);
        } else if (activityId != null) {
            return fatoApontamentoHorasRepository.findByAtividadeAndPeriodo(activityId, fromDate, toDate);
        } else {
            return fatoApontamentoHorasRepository.findByPeriodo(fromDate, toDate);
        }
    }
}
