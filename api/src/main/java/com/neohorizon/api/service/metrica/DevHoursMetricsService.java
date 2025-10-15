package com.neohorizon.api.service.metrica;

import com.neohorizon.api.dto.DevHoursMetricsDTO;
import com.neohorizon.api.entity.FatoApontamentoHoras;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimAtividade;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.repository.FatoApontamentoHorasRepository;
import com.neohorizon.api.utils.ValidationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DevHoursMetricsService {

    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;

    @Autowired
    public DevHoursMetricsService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository) {
        this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
    }

    public List<DevHoursMetricsDTO> getDevHoursMetrics(Long devId, Long activityId, 
                                                      LocalDate fromDate, LocalDate toDate) {
        // Validar IDs se fornecidos
        if (devId != null) {
            ValidationUtils.requireValidId(devId, "Desenvolvedor");
        }
        if (activityId != null) {
            ValidationUtils.requireValidId(activityId, "Atividade");
        }
        
        // Validar datas se fornecidas
        if (fromDate != null && toDate != null) {
            ValidationUtils.require(toDate.isAfter(fromDate) || toDate.isEqual(fromDate), 
                "Data fim deve ser posterior ou igual à data início");
        }
        
        List<FatoApontamentoHoras> apontamentos = getApontamentosByFilters(devId, activityId, fromDate, toDate);
        
        // Agrupar por desenvolvedor
        Map<Long, List<FatoApontamentoHoras>> apontamentosPorDev = apontamentos.stream()
                .collect(Collectors.groupingBy(a -> a.getDimDev().getId()));

        List<DevHoursMetricsDTO> metrics = new ArrayList<>();

        for (Map.Entry<Long, List<FatoApontamentoHoras>> entry : apontamentosPorDev.entrySet()) {
            List<FatoApontamentoHoras> devApontamentos = entry.getValue();
            
            DimDev dev = devApontamentos.get(0).getDimDev();
            
            // Calcular total de horas do desenvolvedor
            Double totalHoras = devApontamentos.stream()
                    .mapToDouble(FatoApontamentoHoras::getHorasTrabalhadas)
                    .sum();

            // Agrupar por atividade
            Map<Long, List<FatoApontamentoHoras>> apontamentosPorAtividade = devApontamentos.stream()
                    .collect(Collectors.groupingBy(a -> a.getDimAtividade().getId()));

            List<DevHoursMetricsDTO.AtividadeHorasDTO> atividades = new ArrayList<>();

            for (Map.Entry<Long, List<FatoApontamentoHoras>> atividadeEntry : apontamentosPorAtividade.entrySet()) {
                List<FatoApontamentoHoras> atividadeApontamentos = atividadeEntry.getValue();
                DimAtividade atividade = atividadeApontamentos.get(0).getDimAtividade();
                
                // Calcular total de horas da atividade
                Double totalHorasAtividade = atividadeApontamentos.stream()
                        .mapToDouble(FatoApontamentoHoras::getHorasTrabalhadas)
                        .sum();

                // Agrupar por dia
                List<DevHoursMetricsDTO.DiaHorasDTO> diasApontamentos = atividadeApontamentos.stream()
                        .map(a -> DevHoursMetricsDTO.DiaHorasDTO.builder()
                                .data(a.getDataApontamento())
                                .horas(a.getHorasTrabalhadas())
                                .descricaoTrabalho(a.getDescricaoTrabalho())
                                .build())
                        .sorted(Comparator.comparing(DevHoursMetricsDTO.DiaHorasDTO::getData).reversed())
                        .toList();

                DevHoursMetricsDTO.AtividadeHorasDTO atividadeDTO = DevHoursMetricsDTO.AtividadeHorasDTO.builder()
                        .atividadeId(atividade.getId())
                        .atividadeNome(atividade.getNome())
                        .projetoNome(atividade.getDimProjeto().getNome())
                        .totalHoras(totalHorasAtividade)
                        .diasApontamentos(diasApontamentos)
                        .build();

                atividades.add(atividadeDTO);
            }

            // Ordenar atividades por total de horas decrescente
            atividades.sort(Comparator.comparing(DevHoursMetricsDTO.AtividadeHorasDTO::getTotalHoras).reversed());

            DevHoursMetricsDTO devMetrics = DevHoursMetricsDTO.builder()
                    .devId(dev.getId())
                    .devNome(dev.getNome())
                    .devEmail(dev.getEmail())
                    .totalHoras(totalHoras)
                    .atividades(atividades)
                    .build();

            metrics.add(devMetrics);
        }

        // Ordenar por total de horas decrescente
        metrics.sort(Comparator.comparing(DevHoursMetricsDTO::getTotalHoras).reversed());

        return metrics;
    }

    private List<FatoApontamentoHoras> getApontamentosByFilters(Long devId, Long activityId, 
                                                              LocalDate fromDate, LocalDate toDate) {
        // Se não especificou datas, usar últimos 30 dias
        LocalDate effectiveFromDate = fromDate != null ? fromDate : LocalDate.now().minusDays(30);
        LocalDate effectiveToDate = toDate != null ? toDate : LocalDate.now();

        // Aplicar filtros baseado nos parâmetros
        try {
            if (devId != null && activityId != null) {
                return fatoApontamentoHorasRepository.findByDevAtividadeAndPeriodo(
                    devId, activityId, effectiveFromDate, effectiveToDate);
            } else if (devId != null) {
                return fatoApontamentoHorasRepository.findByDevAndPeriodo(
                    devId, effectiveFromDate, effectiveToDate);
            } else if (activityId != null) {
                return fatoApontamentoHorasRepository.findByAtividadeAndPeriodo(
                    activityId, effectiveFromDate, effectiveToDate);
            } else {
                return fatoApontamentoHorasRepository.findByPeriodo(
                    effectiveFromDate, effectiveToDate);
            }
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar apontamentos: " + e.getMessage(), e);
        }
    }
}
