package com.neohorizon.api.service.fato;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neohorizon.api.dto.metrica.AtividadeAggregationDTO;
import com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO;
import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;
import com.neohorizon.api.dto.response.fato.FatoAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.entity.dimensao.DimTipo;
import com.neohorizon.api.entity.fato.FatoAtividade;
import com.neohorizon.api.enums.AggregationType;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.dimensao.DimStatusRepository;
import com.neohorizon.api.repository.dimensao.DimTipoRepository;
import com.neohorizon.api.repository.fato.FatoAtividadeRepository;

@ExtendWith(MockitoExtension.class)
class FatoAtividadeServiceTest {

    @Mock
    private FatoAtividadeRepository fatoAtividadeRepository;

    @Mock
    private FatoMapper fatoMapper;

    @Mock
    private DimProjetoRepository dimProjetoRepository;

    @Mock
    private DimPeriodoRepository dimPeriodoRepository;

    @Mock
    private DimStatusRepository dimStatusRepository;

    @Mock
    private DimTipoRepository dimTipoRepository;


    @InjectMocks
    private FatoAtividadeService service;

    @Test
    void getAllEntities_deveRetornarListaDeDTO() {
        FatoAtividade entity = new FatoAtividade();
        FatoAtividadeDTO dto = FatoAtividadeDTO.builder()
                .id(1L)
                .quantidade(BigDecimal.ONE)
                .build();

        when(fatoAtividadeRepository.findAll()).thenReturn(List.of(entity));
        when(fatoMapper.fatoAtividadeToDTO(entity)).thenReturn(dto);

        List<FatoAtividadeDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(fatoAtividadeRepository).findAll();
        verify(fatoMapper).fatoAtividadeToDTO(entity);
    }

    @Test
    void findById_quandoExiste_deveRetornarDTO() {
        FatoAtividade entity = new FatoAtividade();
        FatoAtividadeDTO dto = FatoAtividadeDTO.builder()
                .id(1L)
                .quantidade(BigDecimal.ONE)
                .build();

        when(fatoAtividadeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(fatoMapper.fatoAtividadeToDTO(entity)).thenReturn(dto);

        FatoAtividadeDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void findById_quandoNaoExiste_deveRetornarNull() {
        when(fatoAtividadeRepository.findById(1L)).thenReturn(Optional.empty());

        FatoAtividadeDTO result = service.findById(1L);

        assertNull(result);
    }

    @Test
    void create_deveResolverDimensoesSalvarERetornarDTO() {
        DimProjetoDTO projetoDTO = new DimProjetoDTO(1L, "Projeto X", "PX", "JIRA-1");
        DimPeriodoDTO periodoDTO = new DimPeriodoDTO(2L, 1, 1, 1, 2025);
        DimStatusDTO statusDTO = new DimStatusDTO(3L, "Em Progresso", "ST-1");
        DimTipoDTO tipoDTO = new DimTipoDTO(4L, "Desenvolvimento", "Implantação", "TP-1");

        FatoAtividadeDTO input = FatoAtividadeDTO.builder()
                .dimProjeto(projetoDTO)
                .dimPeriodo(periodoDTO)
                .dimStatus(statusDTO)
                .dimTipo(tipoDTO)
                .quantidade(BigDecimal.TEN)
                .build();

        DimProjeto projeto = new DimProjeto();
        DimPeriodo periodo = new DimPeriodo();
        DimStatus status = new DimStatus();
        DimTipo tipo = new DimTipo();

        FatoAtividade entityToSave = new FatoAtividade();
        FatoAtividade savedEntity = new FatoAtividade();

        FatoAtividadeDTO expected = FatoAtividadeDTO.builder()
                .id(99L)
                .build();

        when(dimProjetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(dimPeriodoRepository.findById(2L)).thenReturn(Optional.of(periodo));
        when(dimStatusRepository.findById(3L)).thenReturn(Optional.of(status));
        when(dimTipoRepository.findById(4L)).thenReturn(Optional.of(tipo));

        when(fatoMapper.dtoToFatoAtividade(input)).thenReturn(entityToSave);
        when(fatoAtividadeRepository.save(entityToSave)).thenReturn(savedEntity);
        when(fatoMapper.fatoAtividadeToDTO(savedEntity)).thenReturn(expected);

        FatoAtividadeDTO result = service.create(input);

        assertEquals(expected, result);

        verify(dimProjetoRepository).findById(1L);
        verify(dimPeriodoRepository).findById(2L);
        verify(dimStatusRepository).findById(3L);
        verify(dimTipoRepository).findById(4L);
        verify(fatoAtividadeRepository).save(entityToSave);
    }

    @Test
    void update_quandoExiste_deveAtualizarQuantidade() {
        Long id = 1L;
        FatoAtividade existing = new FatoAtividade();
        existing.setQuantidade(BigDecimal.ONE);

        FatoAtividadeDTO input = FatoAtividadeDTO.builder()
                .quantidade(BigDecimal.TEN)
                .build();

        FatoAtividade updated = new FatoAtividade();
        updated.setQuantidade(BigDecimal.TEN);

        FatoAtividadeDTO expected = FatoAtividadeDTO.builder()
                .id(id)
                .quantidade(BigDecimal.TEN)
                .build();

        when(fatoAtividadeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(fatoAtividadeRepository.save(existing)).thenReturn(updated);
        when(fatoMapper.fatoAtividadeToDTO(updated)).thenReturn(expected);

        FatoAtividadeDTO result = service.update(id, input);

        assertEquals(expected, result);
        verify(fatoAtividadeRepository).findById(id);
        verify(fatoAtividadeRepository).save(existing);
    }

    @Test
    void deleteById_deveChamarRepositorio() {
        service.deleteById(1L);
        verify(fatoAtividadeRepository).deleteById(1L);
    }

    @Test
    void getTotalAtividades_deveRetornarValorDoRepositorio() {
        when(fatoAtividadeRepository.countAllAtividades()).thenReturn(42L);

        Long total = service.getTotalAtividades();

        assertEquals(42L, total);
        verify(fatoAtividadeRepository).countAllAtividades();
    }

    @Test
    void getAtividadesByProject_quandoProjectIdNaoNulo_deveBuscarPorProjeto() {
        Long projectId = 10L;
        ProjectAtividadeCountDTO dto = new ProjectAtividadeCountDTO("Projeto X", BigDecimal.TEN);

        when(fatoAtividadeRepository.findAtividadeByProject(projectId))
                .thenReturn(List.of(dto));

        List<ProjectAtividadeCountDTO> result = service.getAtividadesByProject(projectId);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(fatoAtividadeRepository).findAtividadeByProject(projectId);
        verify(fatoAtividadeRepository, never()).findAllProjectAtividades();
    }

    @Test
    void getAtividadesByProject_quandoProjectIdNulo_deveBuscarTodosProjetos() {
        ProjectAtividadeCountDTO dto = new ProjectAtividadeCountDTO("Projeto Y", BigDecimal.ONE);

        when(fatoAtividadeRepository.findAllProjectAtividades())
                .thenReturn(List.of(dto));

        List<ProjectAtividadeCountDTO> result = service.getAtividadesByProject(null);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(fatoAtividadeRepository).findAllProjectAtividades();
    }

    @Test
    void getAllProjectAtividades_deveDelegarParaRepositorio() {
        ProjectAtividadeCountDTO dto = new ProjectAtividadeCountDTO("Projeto Z", BigDecimal.ONE);

        when(fatoAtividadeRepository.findAllProjectAtividades())
                .thenReturn(List.of(dto));

        List<ProjectAtividadeCountDTO> result = service.getAllProjectAtividades();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(fatoAtividadeRepository).findAllProjectAtividades();
    }

    @Test
    void getAtividadesByAggregation_deveMontarPeriodoEChamarRepositorio() {
        String dataInicio = "2025-01-01";
        String dataFim = "2025-01-31";
        String periodo = AggregationType.MES.name(); // MES, DIA, SEMANA, ANO

        ProjectAtividadeCountDTO dto =
                new ProjectAtividadeCountDTO("Projeto A", BigDecimal.TEN);

        when(fatoAtividadeRepository.findAtividadesByPeriod(any(AtividadeAggregationDTO.class)))
                .thenReturn(List.of(dto));

        List<ProjectAtividadeCountDTO> result =
                service.getAtividadesByAggregation(dataInicio, dataFim, periodo);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(fatoAtividadeRepository)
                .findAtividadesByPeriod(any(AtividadeAggregationDTO.class));
    }
}
