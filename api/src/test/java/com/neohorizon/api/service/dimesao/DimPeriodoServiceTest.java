package com.neohorizon.api.service.dimesao;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.service.dimensao.DimPeriodoService;

@ExtendWith(MockitoExtension.class)
class DimPeriodoServiceTest {

    @Mock
    private DimPeriodoRepository dimPeriodoRepository;

    @Mock
    private DimensionMapper dimensionMapper;

    @InjectMocks
    private DimPeriodoService service;

    @Test
    void getAllEntities_deveRetornarListaDeDTOs() {
        DimPeriodo entity = DimPeriodo.builder()
                .id(1L).dia(1).semana(1).mes(1).ano(2025).build();
        DimPeriodoDTO dto = new DimPeriodoDTO(1L, 1, 1, 1, 2025);

        when(dimPeriodoRepository.findAll()).thenReturn(List.of(entity));
        when(dimensionMapper.periodoListToDTO(List.of(entity))).thenReturn(List.of(dto));

        List<DimPeriodoDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_quandoEncontrar_deveRetornarDTO() {
        DimPeriodo entity = DimPeriodo.builder()
                .id(1L).dia(1).semana(1).mes(1).ano(2025).build();
        DimPeriodoDTO dto = new DimPeriodoDTO(1L, 1, 1, 1, 2025);

        when(dimPeriodoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dimensionMapper.periodoToDTO(entity)).thenReturn(dto);

        DimPeriodoDTO result = service.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void findById_quandoNaoEncontrar_deveLancarEntityNotFound() {
        when(dimPeriodoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void save_deveSalvarERetornarDTO() {
        DimPeriodoDTO input = new DimPeriodoDTO(null, 1, 1, 1, 2025);
        DimPeriodo entity = DimPeriodo.builder().dia(1).semana(1).mes(1).ano(2025).build();
        DimPeriodo saved = DimPeriodo.builder().id(10L).dia(1).semana(1).mes(1).ano(2025).build();
        DimPeriodoDTO expected = new DimPeriodoDTO(10L, 1, 1, 1, 2025);

        when(dimensionMapper.dtoToPeriodo(input)).thenReturn(entity);
        when(dimPeriodoRepository.save(entity)).thenReturn(saved);
        when(dimensionMapper.periodoToDTO(saved)).thenReturn(expected);

        DimPeriodoDTO result = service.save(input);

        assertEquals(expected, result);
    }

    @Test
    void update_quandoEncontrar_deveAtualizarERetornarDTO() {
        DimPeriodoDTO input = new DimPeriodoDTO(null, 2, 2, 2, 2026);
        DimPeriodo existing = DimPeriodo.builder()
                .id(1L).dia(1).semana(1).mes(1).ano(2025).build();
        DimPeriodo updated = DimPeriodo.builder()
                .id(1L).dia(2).semana(2).mes(2).ano(2026).build();
        DimPeriodoDTO expected = new DimPeriodoDTO(1L, 2, 2, 2, 2026);

        when(dimPeriodoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(dimPeriodoRepository.save(existing)).thenReturn(updated);
        when(dimensionMapper.periodoToDTO(updated)).thenReturn(expected);

        DimPeriodoDTO result = service.update(1L, input);

        assertEquals(expected, result);
        assertEquals(2, existing.getDia());
    }

    @Test
    void deleteById_quandoNaoExiste_deveLancarEntityNotFound() {
        when(dimPeriodoRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteById(99L)
        );
        assertNotNull(ex);
    }

    @Test
    void getAllEntitiesByFilter_deveChamarRepositorioEMapear() {
        DimPeriodo entity = DimPeriodo.builder()
                .id(1L).dia(10).semana(2).mes(3).ano(2025).build();
        DimPeriodoDTO dto = new DimPeriodoDTO(1L, 10, 2, 3, 2025);

        when(dimPeriodoRepository.findByDiaAndSemanaAndMesAndAno(10, 2, 3, 2025))
                .thenReturn(List.of(entity));
        when(dimensionMapper.periodoToDTO(entity)).thenReturn(dto);

        List<DimPeriodoDTO> result = service.getAllEntitiesByFilter(10, 2, 3, 2025);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}
