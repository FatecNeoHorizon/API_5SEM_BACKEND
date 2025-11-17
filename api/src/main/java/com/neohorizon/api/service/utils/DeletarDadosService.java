package com.neohorizon.api.service.utils;

import org.springframework.stereotype.Service;

import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;
import com.neohorizon.api.repository.dimensao.DimStatusRepository;
import com.neohorizon.api.repository.dimensao.DimTipoRepository;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;
import com.neohorizon.api.repository.fato.FatoAtividadeRepository;
import com.neohorizon.api.repository.fato.FatoCustoHoraRepository;

@Service
public class DeletarDadosService {

    private final FatoApontamentoHorasRepository fatoApontamentoHorasRepository;
    private final FatoAtividadeRepository fatoAtividadeRepository;
    private final FatoCustoHoraRepository fatoCustoHoraRepository;
    
    private final DimAtividadeRepository dimAtividadeRepository;
    private final DimPeriodoRepository dimPeriodoRepository;
    private final DimProjetoRepository dimProjetoRepository;
    private final DimStatusRepository dimStatusRepository;
    private final DimTipoRepository dimTipoRepository;

    public DeletarDadosService(FatoApontamentoHorasRepository fatoApontamentoHorasRepository, FatoAtividadeRepository fatoAtividadeRepository, FatoCustoHoraRepository fatoCustoHoraRepository,
        DimAtividadeRepository dimAtividadeRepository, DimPeriodoRepository dimPeriodoRepository, DimProjetoRepository dimProjetoRepository,
        DimStatusRepository dimStatusRepository, DimTipoRepository dimTipoRepository) {
        
        
        this.fatoApontamentoHorasRepository = fatoApontamentoHorasRepository;
        this.fatoAtividadeRepository = fatoAtividadeRepository;
        this.fatoCustoHoraRepository = fatoCustoHoraRepository;


        this.dimAtividadeRepository = dimAtividadeRepository;
        this.dimPeriodoRepository = dimPeriodoRepository;
        this.dimProjetoRepository = dimProjetoRepository;
        this.dimStatusRepository = dimStatusRepository;
        this.dimTipoRepository = dimTipoRepository;
    }

    public Boolean deleteDados()
    {
        Boolean isSucesso = false;

        fatoApontamentoHorasRepository.deleteAll();
        fatoAtividadeRepository.deleteAll();
        fatoCustoHoraRepository.deleteAll();

        dimAtividadeRepository.deleteAll();
        dimPeriodoRepository.deleteAll();
        dimProjetoRepository.deleteAll();
        dimStatusRepository.deleteAll();
        dimTipoRepository.deleteAll();
        isSucesso = true;
        return isSucesso;
    }
}

