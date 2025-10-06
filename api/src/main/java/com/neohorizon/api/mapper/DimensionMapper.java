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

    // Dimensão Desenvolvedor
    DimDevDTO devToDTO(DimDev dimDev);
    DimDev dtoToDev(DimDevDTO dimDevDTO);
    List<DimDevDTO> devListToDTO(List<DimDev> dimDevs);
    List<DimDev> dtoListToDev(List<DimDevDTO> dimDevDTOs);

    // Dimensão Atividade
    @Mapping(source = "dimProjeto.id", target = "projetoId")
    @Mapping(source = "dimProjeto.nome", target = "projetoNome")
    DimAtividadeDTO atividadeToDTO(DimAtividade dimAtividade);
    
    @Mapping(source = "projetoId", target = "dimProjeto.id")
    @Mapping(target = "dimProjeto.nome", ignore = true)
    @Mapping(target = "dimProjeto.key", ignore = true)
    @Mapping(target = "dimProjeto.jira_id", ignore = true)
    DimAtividade dtoToAtividade(DimAtividadeDTO dimAtividadeDTO);
    
    List<DimAtividadeDTO> atividadeListToDTO(List<DimAtividade> dimAtividades);
    List<DimAtividade> dtoListToAtividade(List<DimAtividadeDTO> dimAtividadeDTOs);

    // Dimensão Período
    DimPeriodoDTO periodoToDTO(DimPeriodo dimPeriodo);
    DimPeriodo dtoToPeriodo(DimPeriodoDTO dimPeriodoDTO);
    List<DimPeriodoDTO> periodoListToDTO(List<DimPeriodo> dimPeriodos);
    List<DimPeriodo> dtoListToPeriodo(List<DimPeriodoDTO> dimPeriodoDTOs);

    // Dimensão Projeto
    DimProjetoDTO projetoToDTO(DimProjeto dimProjeto);
    DimProjeto dtoToProjeto(DimProjetoDTO dimProjetoDTO);
    List<DimProjetoDTO> projetoListToDTO(List<DimProjeto> dimProjetos);
    List<DimProjeto> dtoListToProjeto(List<DimProjetoDTO> dimProjetoDTOs);

    // Dimensão Status
    DimStatusDTO statusToDTO(DimStatus dimStatus);
    DimStatus dtoToStatus(DimStatusDTO dimStatusDTO);
    List<DimStatusDTO> statusListToDTO(List<DimStatus> dimStatuss);
    List<DimStatus> dtoListToStatus(List<DimStatusDTO> dimStatusDTOs);

    // Dimensão Tipo
    DimTipoDTO tipoToDTO(DimTipo dimTipo);
    DimTipo dtoToTipo(DimTipoDTO dimTipoDTO);
    List<DimTipoDTO> tipoListToDTO(List<DimTipo> dimTipos);
    List<DimTipo> dtoListToTipo(List<DimTipoDTO> dimTipoDTOs);
}
    