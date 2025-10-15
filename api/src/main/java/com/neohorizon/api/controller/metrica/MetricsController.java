package com.neohorizon.api.controller.metrica;

import com.neohorizon.api.dto.DevHoursMetricsDTO;
import com.neohorizon.api.service.metrica.DevHoursMetricsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final DevHoursMetricsService devHoursMetricsService;

    @Autowired
    public MetricsController(DevHoursMetricsService devHoursMetricsService) {
        this.devHoursMetricsService = devHoursMetricsService;
    }

    /**
     * Rota para consolidar as horas registradas por desenvolvedores, detalhadas por atividades e dia.
     * 
     * @param dev ID do desenvolvedor (opcional)
     * @param activity ID da atividade (opcional)
     * @param from Data de início do período (opcional, padrão: últimos 30 dias)
     * @param to Data de fim do período (opcional, padrão: hoje)
     * @return Lista de métricas consolidadas por desenvolvedor
     */
    @GetMapping("/dev-hours")
    public ResponseEntity<List<DevHoursMetricsDTO>> getDevHoursMetrics(
            @RequestParam(name = "dev", required = false) Long devId,
            @RequestParam(name = "activity", required = false) Long activityId,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<DevHoursMetricsDTO> metrics = devHoursMetricsService.getDevHoursMetrics(devId, activityId, fromDate, toDate);
        return ResponseEntity.ok(metrics);
    }
}
