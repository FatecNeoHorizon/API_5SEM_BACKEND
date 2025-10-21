package com.neohorizon.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.entity.dimensao.DimTipo;

@Mapper(componentModel = "spring")
public interface DimensionMapper {

    // Dimensão Atividade
    // Entidade -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "atividade_jira_id", target = "atividade_jira_id")
    @Mapping(source = "ativo", target = "ativo")
    DimAtividadeDTO atividadeToDTO(DimAtividade dimAtividade);

    // DTO -> Entidade
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "atividade_jira_id", target = "atividade_jira_id")
    @Mapping(source = "ativo", target = "ativo")
    DimAtividade dtoToAtividade(DimAtividadeDTO dimAtividadeDTO);

    // Lista de Dimensão Atividade 
    List<DimAtividadeDTO> atividadeListToDTO(List<DimAtividade> dimAtividades);
    List<DimAtividade> dtoListToAtividade(List<DimAtividadeDTO> dimAtividadeDTOs);

    
    // Dimensão Desenvolvedor
    // Entidade -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "custo_hora", target = "custo_hora")
    DimDevDTO devToDTO(DimDev dimDev);

    // DTO -> Entidade
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "custo_hora", target = "custo_hora")
    DimDev dtoToDev(DimDevDTO dimDevDTO);

    // Lista de Dimensão Desenvolvedor    
    List<DimDevDTO> devListToDTO(List<DimDev> dimDevs);
    List<DimDev> dtoListToDev(List<DimDevDTO> dimDevDTOs);

    // Dimensão Período
    // Entidade -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "dia", target = "dia")
    @Mapping(source = "semana", target = "semana")
    @Mapping(source = "mes", target = "mes")
    @Mapping(source = "ano", target = "ano")
    DimPeriodo dtoToPeriodo(DimPeriodoDTO dimPeriodoDTO);

    // DTO -> Entidade
    @Mapping(source = "id", target = "id")
    @Mapping(source = "dia", target = "dia")
    @Mapping(source = "semana", target = "semana")
    @Mapping(source = "mes", target = "mes")
    @Mapping(source = "ano", target = "ano")
    DimPeriodoDTO periodoToDTO(DimPeriodo dimPeriodo);

    // Lista de Dimensão Período
    List<DimPeriodo> dtoListToPeriodo(List<DimPeriodoDTO> dimPeriodoDTOs);
    List<DimPeriodoDTO> periodoListToDTO(List<DimPeriodo> dimPeriodos);

    // Dimensão Projeto
    // Entidade -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "projeto_jira_id", target = "projeto_jira_id")
    DimProjeto dtoToProjeto(DimProjetoDTO dimProjetoDTO);
    // DTO -> Entidade
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "projeto_jira_id", target = "projeto_jira_id")
    DimProjetoDTO projetoToDTO(DimProjeto dimProjeto);
    
    // Lista de Dimensão Projeto
    List<DimProjetoDTO> projetoListToDTO(List<DimProjeto> dimProjetos);
    List<DimProjeto> dtoListToProjeto(List<DimProjetoDTO> dimProjetoDTOs);

    // Dimensão Status
    // Entidade -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "statusJiraId", target = "statusJiraId")
    DimStatusDTO statusToDTO(DimStatus dimStatus);

    // DTO -> Entidade
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "statusJiraId", target = "statusJiraId")
    DimStatus dtoToStatus(DimStatusDTO dimStatusDTO);

    // Lista de Dimensão Status
    List<DimStatusDTO> statusListToDTO(List<DimStatus> dimStatuss);
    List<DimStatus> dtoListToStatus(List<DimStatusDTO> dimStatusDTOs);

    // Dimensão Tipo
    // Entidade -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "tipoJiraId", target = "tipoJiraId")
    DimTipoDTO tipoToDTO(DimTipo dimTipo);

    // DTO -> Entidade
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "tipoJiraId", target = "tipoJiraId")
    DimTipo dtoToTipo(DimTipoDTO dimTipoDTO);

    // Lista de Dimensão Tipo
    List<DimTipoDTO> tipoListToDTO(List<DimTipo> dimTipos);
    List<DimTipo> dtoListToTipo(List<DimTipoDTO> dimTipoDTOs);
}
    