package com.neohorizon.api.service.utils;

import org.springframework.stereotype.Service;
import com.neohorizon.api.repository.utils.DeletarDadosRepository;

@Service
public class DeletarDadosService {

    private final DeletarDadosRepository deletarDadosRepository;

    public DeletarDadosService(DeletarDadosRepository deletarDadosRepository) {
        
        this.deletarDadosRepository = deletarDadosRepository;
    }

    public Boolean deleteDados()
    {
        Boolean isSucesso = false;

        deletarDadosRepository.truncateTables();

        isSucesso = true;
        return isSucesso;
    }
}

