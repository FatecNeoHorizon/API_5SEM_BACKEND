package com.neohorizon.api.service.fato;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neohorizon.api.dto.response.fato.FatoCustoHoraDTO;
import com.neohorizon.api.dto.response.metrica.CustoTotalDTO;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.fato.FatoCustoHora;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.fato.FatoCustoHoraRepository;

@ExtendWith(MockitoExtension.class)
class FatoCustoHoraServiceTest {

    @Mock
    private FatoCustoHoraRepository repo;

    @Mock
    private FatoMapper fatoMapper;

    @InjectMocks
    private FatoCustoHoraService service;

    @Test
    void getAllEntitiesByFilter_deveFiltrarPorDimensoesEConverterDTO() {
        DimProjeto projeto = new DimProjeto();
        DimPeriodo periodo = new DimPeriodo();
        DimDev dev = new DimDev();

        FatoCustoHora entity = new FatoCustoHora();
        FatoCustoHoraDTO dto = new FatoCustoHoraDTO(
                1L, projeto, periodo, dev,
                BigDecimal.TEN, BigDecimal.ONE
        );

        when(repo.findByDimProjetoAndDimPeriodoAndDimDev(projeto, periodo, dev))
                .thenReturn(List.of(entity));
        when(fatoMapper.custoHoraToDTO(entity)).thenReturn(dto);

        List<FatoCustoHoraDTO> result =
                service.getAllEntitiesByFilter(projeto, periodo, dev);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(repo).findByDimProjetoAndDimPeriodoAndDimDev(projeto, periodo, dev);
        verify(fatoMapper).custoHoraToDTO(entity);
    }

    @Test
    void getAllEntities_deveRetornarTodosComoDTO() {
        FatoCustoHora entity = new FatoCustoHora();
        FatoCustoHoraDTO dto = new FatoCustoHoraDTO();

        when(repo.findAll()).thenReturn(List.of(entity));
        when(fatoMapper.custoHoraToDTO(entity)).thenReturn(dto);

        List<FatoCustoHoraDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(repo).findAll();
    }

    @Test
    void findById_quandoExiste_deveRetornarDTO() {
        FatoCustoHora entity = new FatoCustoHora();
        FatoCustoHoraDTO dto = new FatoCustoHoraDTO();

        when(repo.findById(1L)).thenReturn(Optional.of(entity));
        when(fatoMapper.custoHoraToDTO(entity)).thenReturn(dto);

        FatoCustoHoraDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void findById_quandoNaoExiste_deveRetornarNull() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        FatoCustoHoraDTO result = service.findById(1L);

        assertNull(result);
    }

    @Test
    void create_deveConverterSalvarERetornarDTO() {
        FatoCustoHoraDTO input = new FatoCustoHoraDTO();
        FatoCustoHora entityToSave = new FatoCustoHora();
        FatoCustoHora saved = new FatoCustoHora();
        FatoCustoHoraDTO expected = new FatoCustoHoraDTO();
        expected.setId(10L);

        when(fatoMapper.dtoToCustoHora(input)).thenReturn(entityToSave);
        when(repo.save(entityToSave)).thenReturn(saved);
        when(fatoMapper.custoHoraToDTO(saved)).thenReturn(expected);

        FatoCustoHoraDTO result = service.create(input);

        assertEquals(expected, result);
        verify(fatoMapper).dtoToCustoHora(input);
        verify(repo).save(entityToSave);
    }

    @Test
    void update_quandoExiste_deveAtualizarEConverter() {
        Long id = 1L;

        FatoCustoHora existing = new FatoCustoHora();
        FatoCustoHoraDTO input = new FatoCustoHoraDTO();
        input.setCusto(BigDecimal.TEN);
        input.setHorasQuantidade(BigDecimal.ONE);

        FatoCustoHora updated = new FatoCustoHora();
        FatoCustoHoraDTO expected = new FatoCustoHoraDTO();
        expected.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(updated);
        when(fatoMapper.custoHoraToDTO(updated)).thenReturn(expected);

        FatoCustoHoraDTO result = service.update(id, input);

        assertEquals(expected, result);
        verify(repo).findById(id);
        verify(repo).save(existing);
    }

    @Test
    void update_quandoNaoExiste_deveLancarIllegalArgumentException() {
        Long id = 1L;
        FatoCustoHoraDTO input = new FatoCustoHoraDTO();

        when(repo.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.update(id, input)
        );

        assertNotNull(ex); 
    }

    @Test
    void deleteById_deveChamarRepositorio() {
        service.deleteById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void obteinTotal_deveCriarDTOComTotalDoRepositorio() {
        when(repo.totalGeral()).thenReturn(150L);

        CustoTotalDTO result = service.obteinTotal();

        assertNotNull(result);
        assertEquals(150L, result.total);
        verify(repo).totalGeral();
    }

}
