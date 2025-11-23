package com.neohorizon.api.service.fato;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;

@ExtendWith(MockitoExtension.class)
class FatoApontamentoHorasServiceTest {

    @Mock
    private FatoApontamentoHorasRepository fatoApontamentoHorasRepository;

    @Mock
    private DimPeriodoRepository dimPeriodoRepository;

    @Mock
    private DimProjetoRepository dimProjetoRepository;

    @Mock
    private DimAtividadeRepository dimAtividadeRepository;

    @Mock
    private DimDevRepository dimDevRepository;

    @Mock
    private FatoMapper fatoMapper;

    @InjectMocks
    private FatoApontamentoHorasService service;

    @Test
    void getAllEntities_deveRetornarListaDTO() {
        FatoApontamentoHoras entity = FatoApontamentoHoras.builder()
                .id(1L)
                .horasTrabalhadas(4.0)
                .descricaoTrabalho("Impl")
                .dataCriacao(LocalDateTime.now())
                .build();

        FatoApontamentoHorasDTO dto = new FatoApontamentoHorasDTO();
        dto.setId(1L);
        dto.setHorasTrabalhadas(4.0);
        dto.setDescricaoTrabalho("Impl");

        when(fatoApontamentoHorasRepository.findAll()).thenReturn(List.of(entity));
        when(fatoMapper.fatoApontamentoToDTO(entity)).thenReturn(dto);

        List<FatoApontamentoHorasDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findByPeriodo_deveChamarRepositorioEMapear() {
        // datas de entrada do service (LocalDate)
        LocalDate inicioData = LocalDate.of(2025, 1, 1);
        LocalDate fimData = LocalDate.of(2025, 1, 31);

        // datas que o service efetivamente envia ao repository (LocalDateTime)
        LocalDateTime inicio = inicioData.atStartOfDay();
        LocalDateTime fim = fimData.atTime(23, 59, 59);

        FatoApontamentoHoras entity = FatoApontamentoHoras.builder()
                .id(1L)
                .horasTrabalhadas(2.0)
                .descricaoTrabalho("X")
                .dataCriacao(LocalDateTime.now())
                .build();

        FatoApontamentoHorasDTO dto = new FatoApontamentoHorasDTO();
        dto.setId(1L);

        // importante: mock com LocalDateTime
        when(fatoApontamentoHorasRepository.findByPeriodo(inicio, fim))
                .thenReturn(List.of(entity));
        when(fatoMapper.fatoApontamentoToDTO(entity)).thenReturn(dto);

        List<FatoApontamentoHorasDTO> result = service.findByPeriodo(inicioData, fimData);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void create_deveResolverDimensoesSalvarERetornarDTO() {

        DimPeriodoDTO periodoDTO = new DimPeriodoDTO(1L, 1, 1, 1, 2025);
        DimProjetoDTO projetoDTO = new DimProjetoDTO(2L, "NeoHorizon", "NH", "PRJ-1");
        DimAtividadeDTO atividadeDTO = DimAtividadeDTO.builder()
                .id(3L)
                .nome("Implementação")
                .build();
        DimDevDTO devDTO = new DimDevDTO(4L, "Alice", null);

        FatoApontamentoHorasDTO input = new FatoApontamentoHorasDTO();
        input.setDimPeriodo(periodoDTO);
        input.setDimProjeto(projetoDTO);
        input.setDimAtividade(atividadeDTO);
        input.setDimDev(devDTO);
        input.setHorasTrabalhadas(4.0);
        input.setDescricaoTrabalho("Teste");

        DimPeriodo periodo = DimPeriodo.builder().id(1L).build();
        DimProjeto projeto = DimProjeto.builder().id(2L).build();
        DimAtividade atividade = DimAtividade.builder().id(3L).build();
        DimDev dev = DimDev.builder().id(4L).build();

        FatoApontamentoHoras entityToSave = FatoApontamentoHoras.builder()
                .horasTrabalhadas(4.0)
                .build();

        FatoApontamentoHoras saved = FatoApontamentoHoras.builder()
                .id(10L)
                .horasTrabalhadas(4.0)
                .build();

        FatoApontamentoHorasDTO expected = new FatoApontamentoHorasDTO();
        expected.setId(10L);

        // repos de dimensão retornam Optional<Entity>
        when(dimPeriodoRepository.findById(1L)).thenReturn(Optional.of(periodo));
        when(dimDevRepository.findById(4L)).thenReturn(Optional.of(dev));
        when(dimAtividadeRepository.findById(3L)).thenReturn(Optional.of(atividade));
        when(dimProjetoRepository.findById(2L)).thenReturn(Optional.of(projeto));

        // FatoMapper recebe APENAS o DTO
        when(fatoMapper.dtoToFatoApontamento(input)).thenReturn(entityToSave);

        when(fatoApontamentoHorasRepository.save(entityToSave)).thenReturn(saved);
        when(fatoMapper.fatoApontamentoToDTO(saved)).thenReturn(expected);

        FatoApontamentoHorasDTO result = service.create(input);

        assertEquals(expected, result);
    }

    @Test
    void deleteById_quandoNaoExiste_deveLancarEntityNotFound() {
        when(fatoApontamentoHorasRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void deleteById_quandoExiste_deveDeletar() {
        when(fatoApontamentoHorasRepository.existsById(1L)).thenReturn(true);

        service.deleteById(1L);

        verify(fatoApontamentoHorasRepository).deleteById(1L);
    }
}
