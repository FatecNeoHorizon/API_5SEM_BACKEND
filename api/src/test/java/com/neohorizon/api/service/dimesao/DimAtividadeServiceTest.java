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

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.service.dimensao.DimAtividadeService;

@ExtendWith(MockitoExtension.class)
class DimAtividadeServiceTest {

    @Mock
    private DimAtividadeRepository dimAtividadeRepository;

    @Mock
    private DimensionMapper dimensionMapper;

    @InjectMocks
    private DimAtividadeService service;

    @Test
    void getAllEntities_deveRetornarListaDeDTOsAtivos() {
        DimAtividade entity = DimAtividade.builder()
                .id(1L)
                .nome("Implementação")
                .descricao("API")
                .ativo(true)
                .build();

        DimAtividadeDTO dto = DimAtividadeDTO.builder()
                .id(1L)
                .nome("Implementação")
                .descricao("API")
                .ativo(true)
                .build();

        when(dimAtividadeRepository.findByAtivoTrue()).thenReturn(List.of(entity));
        when(dimensionMapper.atividadeListToDTO(List.of(entity))).thenReturn(List.of(dto));

        List<DimAtividadeDTO> result = service.getAllEntities();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_quandoEncontrar_deveRetornarDTO() {
        DimAtividade entity = DimAtividade.builder()
                .id(1L).nome("Implementação").descricao("API").ativo(true).build();
        DimAtividadeDTO dto = DimAtividadeDTO.builder()
                .id(1L).nome("Implementação").descricao("API").ativo(true).build();

        when(dimAtividadeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dimensionMapper.atividadeToDTO(entity)).thenReturn(dto);

        DimAtividadeDTO result = service.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void findById_quandoNaoEncontrar_deveLancarEntityNotFound() {
        when(dimAtividadeRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(1L)
        );
        assertNotNull(ex);
    }

    @Test
    void save_deveConverterSalvarERetornarDTO() {
        DimAtividadeDTO input = DimAtividadeDTO.builder()
                .nome("Implementação").descricao("API").ativo(true).build();

        DimAtividade entity = DimAtividade.builder()
                .nome("Implementação").descricao("API").ativo(true).build();

        DimAtividade saved = DimAtividade.builder()
                .id(10L).nome("Implementação").descricao("API").ativo(true).build();

        DimAtividadeDTO expected = DimAtividadeDTO.builder()
                .id(10L).nome("Implementação").descricao("API").ativo(true).build();

        when(dimensionMapper.dtoToAtividade(input)).thenReturn(entity);
        when(dimAtividadeRepository.save(entity)).thenReturn(saved);
        when(dimensionMapper.atividadeToDTO(saved)).thenReturn(expected);

        DimAtividadeDTO result = service.save(input);

        assertEquals(expected, result);
    }

    @Test
    void update_quandoEncontrar_deveAtualizarERetornarDTO() {
        DimAtividadeDTO input = DimAtividadeDTO.builder()
                .nome("Novo nome").descricao("Nova desc").ativo(true).build();

        DimAtividade existing = DimAtividade.builder()
                .id(1L).nome("Velho").descricao("Velha").ativo(true).build();

        DimAtividade updated = DimAtividade.builder()
                .id(1L).nome("Novo nome").descricao("Nova desc").ativo(true).build();

        DimAtividadeDTO expected = DimAtividadeDTO.builder()
                .id(1L).nome("Novo nome").descricao("Nova desc").ativo(true).build();

        when(dimAtividadeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(dimAtividadeRepository.save(existing)).thenReturn(updated);
        when(dimensionMapper.atividadeToDTO(updated)).thenReturn(expected);

        DimAtividadeDTO result = service.update(1L, input);

        assertEquals(expected, result);
        assertEquals("Novo nome", existing.getNome());
    }

    @Test
    void deleteById_quandoNaoExiste_deveLancarEntityNotFound() {
        when(dimAtividadeRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteById(99L)
        );
        assertNotNull(ex);
    }

    @Test
    void deleteById_quandoErroAoSalvar_deveLancarBusinessException() {
        DimAtividade entity = DimAtividade.builder()
                .id(1L).nome("X").descricao("Y").ativo(true).build();

        when(dimAtividadeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dimAtividadeRepository.save(entity)).thenThrow(new RuntimeException("Erro qualquer"));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.deleteById(1L)
        );
        assertNotNull(ex);
    }
}
