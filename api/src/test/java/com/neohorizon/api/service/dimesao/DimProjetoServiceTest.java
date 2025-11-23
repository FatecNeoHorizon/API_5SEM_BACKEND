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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.service.dimensao.DimProjetoService;

@ExtendWith(MockitoExtension.class)
class DimProjetoServiceTest {

    @Mock
    private DimProjetoRepository dimProjetoRepository;

    @Mock
    private DimensionMapper dimensionMapper;

    @InjectMocks
    private DimProjetoService service;

    @Test
    void getAllEntities_deveRetornarListaDeDTOs() {
        DimProjeto entity = DimProjeto.builder()
                .id(1L).nome("NeoHorizon").key("NH").projeto_jira_id("PRJ-1").build();
        DimProjetoDTO dto = new DimProjetoDTO(1L, "NeoHorizon", "NH", "PRJ-1");

        when(dimProjetoRepository.findAll()).thenReturn(List.of(entity));
        when(dimensionMapper.projetoToDTO(entity)).thenReturn(dto);

        List<DimProjetoDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_quandoNaoEncontrar_deveLancarEntityNotFound() {
        when(dimProjetoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void save_deveSalvarERetornarDTO() {
        DimProjetoDTO input = new DimProjetoDTO(null, "NeoHorizon", "NH", "PRJ-1");
        DimProjeto entity = DimProjeto.builder().nome("NeoHorizon").key("NH").projeto_jira_id("PRJ-1").build();
        DimProjeto saved = DimProjeto.builder().id(10L).nome("NeoHorizon").key("NH").projeto_jira_id("PRJ-1").build();
        DimProjetoDTO expected = new DimProjetoDTO(10L, "NeoHorizon", "NH", "PRJ-1");

        when(dimensionMapper.dtoToProjeto(input)).thenReturn(entity);
        when(dimProjetoRepository.save(entity)).thenReturn(saved);
        when(dimensionMapper.projetoToDTO(saved)).thenReturn(expected);

        DimProjetoDTO result = service.save(input);

        assertEquals(expected, result);
    }

    @Test
    void update_quandoNaoExistir_deveLancarEntityNotFound() {
        DimProjetoDTO input = new DimProjetoDTO(null, "X", "Y", "Z");

        when(dimProjetoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.update(1L, input)
        );
        assertNotNull(ex);
    }

    @Test
    void deleteById_quandoNaoExiste_deveLancarEntityNotFound() {
        when(dimProjetoRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void deleteById_quandoErroRepositorio_deveLancarBusinessException() {
        when(dimProjetoRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("erro")).when(dimProjetoRepository).deleteById(1L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }
}
