package com.neohorizon.api.service.dimesao;

import java.math.BigDecimal;
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

import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.service.dimensao.DimDevService;

@ExtendWith(MockitoExtension.class)
class DimDevServiceTest {

    @Mock
    private DimDevRepository dimDevRepository;

    @Mock
    private DimensionMapper dimensionMapper;

    @InjectMocks
    private DimDevService service;

    @Test
    void getById_quandoEncontrar_deveRetornarDTO() {
        DimDev entity = DimDev.builder()
                .id(1L).nome("Alice").custoHora(new BigDecimal("100")).build();
        DimDevDTO dto = new DimDevDTO(1L, "Alice", new BigDecimal("100"));

        when(dimDevRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dimensionMapper.devToDTO(entity)).thenReturn(dto);

        DimDevDTO result = service.getById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getById_quandoNaoEncontrar_deveLancarEntityNotFound() {
        when(dimDevRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.getById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void getAllEntities_deveRetornarListaDeDTOs() {
        DimDev dev = DimDev.builder()
                .id(1L).nome("Alice").custoHora(new BigDecimal("100")).build();
        DimDevDTO dto = new DimDevDTO(1L, "Alice", new BigDecimal("100"));

        when(dimDevRepository.findAll()).thenReturn(List.of(dev));
        when(dimensionMapper.devToDTO(dev)).thenReturn(dto);

        List<DimDevDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void create_deveSalvarERetornarDTO() {
        DimDevDTO input = new DimDevDTO(null, "Alice", new BigDecimal("120"));
        DimDev entity = DimDev.builder().nome("Alice").custoHora(new BigDecimal("120")).build();
        DimDev saved = DimDev.builder().id(10L).nome("Alice").custoHora(new BigDecimal("120")).build();
        DimDevDTO expected = new DimDevDTO(10L, "Alice", new BigDecimal("120"));

        when(dimensionMapper.dtoToDev(input)).thenReturn(entity);
        when(dimDevRepository.save(entity)).thenReturn(saved);
        when(dimensionMapper.devToDTO(saved)).thenReturn(expected);

        DimDevDTO result = service.create(input);

        assertEquals(expected, result);
    }

    @Test
    void update_quandoNaoEncontrar_deveLancarEntityNotFound() {
        DimDevDTO input = new DimDevDTO(null, "X", new BigDecimal("10"));

        when(dimDevRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.update(1L, input)
        );
        assertNotNull(ex);
    }

    @Test
    void update_quandoEncontrar_deveAtualizarERetornarDTO() {
        DimDevDTO input = new DimDevDTO(null, "Alice Nova", new BigDecimal("150"));
        DimDev existing = DimDev.builder()
                .id(1L).nome("Antiga").custoHora(new BigDecimal("100")).build();
        DimDev updated = DimDev.builder()
                .id(1L).nome("Alice Nova").custoHora(new BigDecimal("150")).build();
        DimDevDTO expected = new DimDevDTO(1L, "Alice Nova", new BigDecimal("150"));

        when(dimDevRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(dimDevRepository.save(existing)).thenReturn(updated);
        when(dimensionMapper.devToDTO(updated)).thenReturn(expected);

        DimDevDTO result = service.update(1L, input);

        assertEquals(expected, result);
        assertEquals("Alice Nova", existing.getNome());
    }

    @Test
    void delete_quandoNaoExiste_deveLancarEntityNotFound() {
        when(dimDevRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.delete(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void delete_quandoErroNoRepositorio_deveLancarBusinessException() {
        when(dimDevRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("falha")).when(dimDevRepository).deleteById(1L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.delete(1L)
        );
        assertNotNull(ex);
    }
}
