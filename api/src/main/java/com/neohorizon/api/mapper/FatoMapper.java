package com.neohorizon.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.dto.response.fato.FatoAtividadeDTO;
import com.neohorizon.api.dto.response.fato.FatoCustoHoraDTO;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.entity.fato.FatoAtividade;
import com.neohorizon.api.entity.fato.FatoCustoHora;

@Mapper(componentModel = "spring")
public interface FatoMapper {

    // Fato Apontamento Horas
    @Mapping(source = "dimDev.id", target = "devId")
    @Mapping(source = "dimDev.nome", target = "devNome")
    @Mapping(source = "dimAtividade.id", target = "atividadeId")
    @Mapping(source = "dimAtividade.nome", target = "atividadeNome")
    @Mapping(source = "dimProjeto.id", target = "projetoId")
    @Mapping(source = "dimProjeto.nome", target = "projetoNome")
    FatoApontamentoHorasDTO apontamentoToDTO(FatoApontamentoHoras fatoApontamentoHoras);
    
    @Mapping(source = "devId", target = "dimDev.id")
    @Mapping(target = "dimDev.nome", ignore = true)
    @Mapping(target = "dimDev.email", ignore = true)
    @Mapping(target = "dimDev.ativo", ignore = true)
    @Mapping(source = "atividadeId", target = "dimAtividade.id")
    @Mapping(target = "dimAtividade.nome", ignore = true)
    @Mapping(target = "dimAtividade.descricao", ignore = true)
    @Mapping(target = "dimAtividade.dimProjeto", ignore = true)
    @Mapping(target = "dimAtividade.ativo", ignore = true)
    @Mapping(source = "projetoId", target = "dimProjeto.id")
    @Mapping(target = "dimProjeto.nome", ignore = true)
    @Mapping(target = "dimProjeto.key", ignore = true)
    @Mapping(target = "dimProjeto.jira_id", ignore = true)
    FatoApontamentoHoras dtoToApontamento(FatoApontamentoHorasDTO fatoApontamentoHorasDTO);
    
    List<FatoApontamentoHorasDTO> apontamentoListToDTO(List<FatoApontamentoHoras> fatoApontamentoHoras);
    List<FatoApontamentoHoras> dtoListToApontamento(List<FatoApontamentoHorasDTO> fatoApontamentoHorasDTOs);

    // Fato Custo Hora (mapeamento direto pois DTO usa entidades)
    FatoCustoHoraDTO custoHoraToDTO(FatoCustoHora fatoCustoHora);
    FatoCustoHora dtoToCustoHora(FatoCustoHoraDTO fatoCustoHoraDTO);
    List<FatoCustoHoraDTO> custoHoraListToDTO(List<FatoCustoHora> fatoCustoHoras);
    List<FatoCustoHora> dtoListToCustoHora(List<FatoCustoHoraDTO> fatoCustoHoraDTOs);

    // Fato Atividade (mapeamento direto pois DTO usa entidades)
    FatoAtividadeDTO atividadeToDTO(FatoAtividade fatoAtividade);
    FatoAtividade dtoToAtividade(FatoAtividadeDTO fatoAtividadeDTO);
    List<FatoAtividadeDTO> atividadeListToDTO(List<FatoAtividade> fatoAtividades);
    List<FatoAtividade> dtoListToAtividade(List<FatoAtividadeDTO> fatoAtividadeDTOs);
}