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

import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;
import com.neohorizon.api.entity.dimensao.DimTipo;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimTipoRepository;
import com.neohorizon.api.service.dimensao.DimTipoService;

@ExtendWith(MockitoExtension.class)
class DimTipoServiceTest {

    @Mock
    private DimTipoRepository dimTipoRepository;

    @Mock
    private DimensionMapper dimensionMapper;

    @InjectMocks
    private DimTipoService service;

    @Test
    void getAllEntities_deveRetornarListaDeDTOs() {
        DimTipo entity = DimTipo.builder().id(1L).nome("Bug").descricao("Correção").tipoJiraId("BUG").build();
        DimTipoDTO dto = new DimTipoDTO(1L, "Bug", "Correção", "BUG");

        when(dimTipoRepository.findAll()).thenReturn(List.of(entity));
        when(dimensionMapper.tipoToDTO(entity)).thenReturn(dto);

        List<DimTipoDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findByNome_deveRetornarListaDTO() {
        DimTipo entity = DimTipo.builder().id(1L).nome("Bug").descricao("Correção").tipoJiraId("BUG").build();
        DimTipoDTO dto = new DimTipoDTO(1L, "Bug", "Correção", "BUG");

        when(dimTipoRepository.findByNome("Bug")).thenReturn(List.of(entity));
        when(dimensionMapper.tipoToDTO(entity)).thenReturn(dto);

        List<DimTipoDTO> result = service.findByNome("Bug");

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_quandoNaoEncontrar_deveLancarEntityNotFound() {
        when(dimTipoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void save_deveSalvarERetornarDTO() {
        DimTipoDTO input = new DimTipoDTO(null, "Bug", "Correção", "BUG");
        DimTipo entity = DimTipo.builder().nome("Bug").descricao("Correção").tipoJiraId("BUG").build();
        DimTipo saved = DimTipo.builder().id(10L).nome("Bug").descricao("Correção").tipoJiraId("BUG").build();
        DimTipoDTO expected = new DimTipoDTO(10L, "Bug", "Correção", "BUG");

        when(dimensionMapper.dtoToTipo(input)).thenReturn(entity);
        when(dimTipoRepository.save(entity)).thenReturn(saved);
        when(dimensionMapper.tipoToDTO(saved)).thenReturn(expected);

        DimTipoDTO result = service.save(input);

        assertEquals(expected, result);
    }

    @Test
    void deleteById_quandoNaoExiste_deveLancarEntityNotFound() {
        when(dimTipoRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void deleteById_quandoErroRepositorio_deveLancarBusinessException() {
        when(dimTipoRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("erro")).when(dimTipoRepository).deleteById(1L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }
}
