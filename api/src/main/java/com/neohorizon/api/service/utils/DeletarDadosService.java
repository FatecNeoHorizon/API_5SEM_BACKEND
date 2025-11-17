package com.neohorizon.api.service.utils;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.DimensionMapper;
import com.neohorizon.api.repository.dimensao.DimAtividadeRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class DeletarDadosService {

    private final DimAtividadeRepository dimAtividadeRepository;

    public DeletarDadosService(DimAtividadeRepository dimAtividadeRepository) {
        this.dimAtividadeRepository = dimAtividadeRepository;
    }

    public Boolean deleteDados()
    {
        Boolean isSucesso = false;
        dimAtividadeRepository.deleteAll();
        isSucesso = true;
        return isSucesso;
    }
}

