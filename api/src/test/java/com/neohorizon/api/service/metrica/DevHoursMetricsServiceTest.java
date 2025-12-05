package com.neohorizon.api.service.metrica;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neohorizon.api.dto.metrica.DevHoursMetricsDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;

@ExtendWith(MockitoExtension.class)
public class DevHoursMetricsServiceTest {

    @Mock
    private FatoApontamentoHorasRepository repository;

    @InjectMocks
    private DevHoursMetricsService service;

    private DimDev dev;
    private DimAtividade atividade;
    private FatoApontamentoHoras apont1, apont2;

    @BeforeEach
    void setup() {
        dev = new DimDev();
        dev.setId(1L);
        dev.setNome("João");

        atividade = new DimAtividade();
        atividade.setId(10L);
        atividade.setNome("Desenvolvimento Backend");

        apont1 = new FatoApontamentoHoras();
        apont1.setDimDev(dev);
        apont1.setDimAtividade(atividade);
        apont1.setHorasTrabalhadas(3.0);
        apont1.setDescricaoTrabalho("API Login");
        apont1.setDataAtualizacao(LocalDateTime.of(2025, 1, 5, 10, 0));

        apont2 = new FatoApontamentoHoras();
        apont2.setDimDev(dev);
        apont2.setDimAtividade(atividade);
        apont2.setHorasTrabalhadas(5.0);
        apont2.setDescricaoTrabalho("API Pedidos");
        apont2.setDataAtualizacao(LocalDateTime.of(2025, 1, 6, 11, 0));
    }

    @Test
    void deveRetornarMetricasCorretasPorDev() {
        when(repository.findByDevAndPeriodo(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(apont1, apont2));

        List<DevHoursMetricsDTO> result = service.getDevHoursMetrics(
                1L, null, LocalDate.now().minusDays(10), LocalDate.now()
        );

        assertThat(result).hasSize(1);

        DevHoursMetricsDTO dto = result.get(0);

        assertThat(dto.getDevId()).isEqualTo(1L);
        assertThat(dto.getDevNome()).isEqualTo("João");

        assertThat(dto.getTotalHoras()).isEqualTo(8.0);

        assertThat(dto.getAtividades()).hasSize(1);
        assertThat(dto.getAtividades().get(0).getDiasApontamentos()).hasSize(2);
    }

    @Test
    void deveBuscarPorAtividadeQuandoInformada() {
        when(repository.findByAtividadeAndPeriodo(
                eq(10L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(apont1));

        List<DevHoursMetricsDTO> result = service.getDevHoursMetrics(
                null, 10L, LocalDate.now().minusDays(5), LocalDate.now()
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTotalHoras()).isEqualTo(3.0);
    }

    @Test
    void deveBuscarPorDevEAtividade() {
        when(repository.findByDevAtividadeAndPeriodo(
                eq(1L), eq(10L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(apont2));

        List<DevHoursMetricsDTO> result = service.getDevHoursMetrics(
                1L, 10L, LocalDate.now().minusDays(5), LocalDate.now()
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTotalHoras()).isEqualTo(5.0);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaApontamentos() {
        when(repository.findByPeriodo(any(), any())).thenReturn(List.of());

        List<DevHoursMetricsDTO> result = service.getDevHoursMetrics(
                null, null, LocalDate.now().minusDays(5), LocalDate.now()
        );

        assertThat(result).isEmpty();
    }

    @Test
    void deveAgruparHorasPorMultiplasAtividades() {
        DimAtividade atividade2 = new DimAtividade();
        atividade2.setId(20L);
        atividade2.setNome("Correções");

        FatoApontamentoHoras apont3 = new FatoApontamentoHoras();
        apont3.setDimDev(dev);
        apont3.setDimAtividade(atividade2);
        apont3.setHorasTrabalhadas(2.0);
        apont3.setDescricaoTrabalho("Bugfix");
        apont3.setDataAtualizacao(LocalDateTime.now());

        when(repository.findByDevAndPeriodo(eq(1L), any(), any()))
                .thenReturn(List.of(apont1, apont2, apont3));

        List<DevHoursMetricsDTO> result = service.getDevHoursMetrics(
                1L, null, LocalDate.now().minusDays(5), LocalDate.now()
        );

        assertThat(result).hasSize(1);

        DevHoursMetricsDTO dto = result.get(0);

        assertThat(dto.getAtividades()).hasSize(2);

        double backendHoras = dto.getAtividades().get(0).getTotalHoras();
        double correcaoHoras = dto.getAtividades().get(1).getTotalHoras();

        assertThat(backendHoras + correcaoHoras).isEqualTo(10.0);
    }
}
