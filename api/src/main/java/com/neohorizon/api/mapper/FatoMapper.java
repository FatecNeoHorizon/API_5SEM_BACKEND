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
    @Mapping(source = "dimDev", target = "dimDev")
    @Mapping(source = "dimAtividade", target = "dimAtividade")
    @Mapping(source = "dimProjeto", target = "dimProjeto")
    @Mapping(source = "dimPeriodo", target = "dimPeriodo")
    @Mapping(source = "horasTrabalhadas", target = "horasTrabalhadas")
    @Mapping(source = "descricaoTrabalho", target = "descricaoTrabalho")
    @Mapping(source = "dataCriacao", target = "dataCriacao")
    @Mapping(source = "dataAtualizacao", target = "dataAtualizacao")
    FatoApontamentoHorasDTO fatoApontamentoToDTO(FatoApontamentoHoras fatoApontamentoHoras);
    
    @Mapping(source = "dimDev", target = "dimDev")
    @Mapping(source = "dimAtividade", target = "dimAtividade")
    @Mapping(source = "dimProjeto", target = "dimProjeto")
    @Mapping(source = "dimPeriodo", target = "dimPeriodo")
    @Mapping(source = "horasTrabalhadas", target = "horasTrabalhadas")
    @Mapping(source = "descricaoTrabalho", target = "descricaoTrabalho")
    @Mapping(source = "dataCriacao", target = "dataCriacao")
    @Mapping(source = "dataAtualizacao", target = "dataAtualizacao")
    FatoApontamentoHoras dtoToFatoApontamento(FatoApontamentoHorasDTO fatoApontamentoHorasDTO);

    List<FatoApontamentoHorasDTO> fatoApontamentoListToDTO(List<FatoApontamentoHoras> fatoApontamentoHoras);
    List<FatoApontamentoHoras> dtoListToFatoApontamento(List<FatoApontamentoHorasDTO> fatoApontamentoHorasDTOs);


    // Fato Atividade (mapeamento direto pois DTO usa entidades)
    @Mapping(source = "dimProjeto", target = "dimProjeto")
    @Mapping(source = "dimPeriodo", target = "dimPeriodo")
    @Mapping(source = "dimStatus", target = "dimStatus")
    @Mapping(source = "dimTipo", target = "dimTipo")
    @Mapping(source = "quantidade", target = "quantidade")
    FatoAtividadeDTO fatoAtividadeToDTO(FatoAtividade fatoAtividade);

    @Mapping(source = "dimProjeto", target = "dimProjeto")
    @Mapping(source = "dimPeriodo", target = "dimPeriodo")
    @Mapping(source = "dimStatus", target = "dimStatus")
    @Mapping(source = "dimTipo", target = "dimTipo")
    @Mapping(source = "quantidade", target = "quantidade")
    FatoAtividade dtoToFatoAtividade(FatoAtividadeDTO fatoAtividadeDTO);

    List<FatoAtividadeDTO> fatoAtividadeListToDTO(List<FatoAtividade> fatoAtividades);
    List<FatoAtividade> dtoListToFatoAtividade(List<FatoAtividadeDTO> fatoAtividadeDTOs);



    // Fato Custo Hora (mapeamento direto pois DTO usa entidades)
    @Mapping(source = "dimProjeto", target = "dimProjeto")
    @Mapping(source = "dimPeriodo", target = "dimPeriodo")
    @Mapping(source = "dimDev", target = "dimDev")
    @Mapping(source = "custo", target = "custo")
    @Mapping(source = "horas_quantidade", target = "horas_quantidade")
    FatoCustoHoraDTO custoHoraToDTO(FatoCustoHora fatoCustoHora);

    @Mapping(source = "dimProjeto", target = "dimProjeto")
    @Mapping(source = "dimPeriodo", target = "dimPeriodo")
    @Mapping(source = "dimDev", target = "dimDev")
    @Mapping(source = "custo", target = "custo")
    @Mapping(source = "horas_quantidade", target = "horas_quantidade")
    FatoCustoHora dtoToCustoHora(FatoCustoHoraDTO fatoCustoHoraDTO);
    
    List<FatoCustoHoraDTO> custoHoraListToDTO(List<FatoCustoHora> fatoCustoHoras);
    List<FatoCustoHora> dtoListToCustoHora(List<FatoCustoHoraDTO> fatoCustoHoraDTOs);

    
}