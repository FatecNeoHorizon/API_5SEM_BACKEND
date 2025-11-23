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

import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimStatusRepository;
import com.neohorizon.api.service.dimensao.DimStatusService;

@ExtendWith(MockitoExtension.class)
class DimStatusServiceTest {

    @Mock
    private DimStatusRepository dimStatusRepository;

    @Mock
    private DimensionMapper dimensionMapper;

    @InjectMocks
    private DimStatusService service;

    @Test
    void getAllEntities_deveRetornarListaDeDTOs() {
        DimStatus entity = DimStatus.builder().id(1L).nome("Done").statusJiraId("DONE").build();
        DimStatusDTO dto = new DimStatusDTO(1L, "Done", "DONE");

        when(dimStatusRepository.findAll()).thenReturn(List.of(entity));
        when(dimensionMapper.statusToDTO(entity)).thenReturn(dto);

        List<DimStatusDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_quandoNaoEncontrar_deveLancarEntityNotFound() {
        when(dimStatusRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void save_deveSalvarERetornarDTO() {
        DimStatusDTO input = new DimStatusDTO(null, "Done", "DONE");
        DimStatus entity = DimStatus.builder().nome("Done").statusJiraId("DONE").build();
        DimStatus saved = DimStatus.builder().id(10L).nome("Done").statusJiraId("DONE").build();
        DimStatusDTO expected = new DimStatusDTO(10L, "Done", "DONE");

        when(dimensionMapper.dtoToStatus(input)).thenReturn(entity);
        when(dimStatusRepository.save(entity)).thenReturn(saved);
        when(dimensionMapper.statusToDTO(saved)).thenReturn(expected);

        DimStatusDTO result = service.save(input);

        assertEquals(expected, result);
    }

    @Test
    void deleteById_quandoNaoExiste_deveLancarEntityNotFound() {
        when(dimStatusRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void deleteById_quandoErroRepositorio_deveLancarBusinessException() {
        when(dimStatusRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("erro")).when(dimStatusRepository).deleteById(1L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }
}
