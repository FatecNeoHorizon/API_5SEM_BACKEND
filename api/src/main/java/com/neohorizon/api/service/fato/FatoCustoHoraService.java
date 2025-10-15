// src/main/java/com/neohorizon/api/service/FatoCustoHoraService.java
package com.neohorizon.api.service.fato;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.response.fato.FatoCustoHoraDTO;
import com.neohorizon.api.dto.response.metrica.CustoHorasPorDevDTO;
import com.neohorizon.api.dto.response.metrica.CustoPorProjetoDTO;
import com.neohorizon.api.dto.response.metrica.CustoTotalDTO;
import com.neohorizon.api.dto.response.metrica.EvolucaoCustoDTO;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.fato.FatoCustoHora;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.fato.FatoCustoHoraRepository;

@Service
public class FatoCustoHoraService {

    private final FatoCustoHoraRepository repo;
    private final FatoMapper fatoMapper;

    @Autowired
    public FatoCustoHoraService(FatoCustoHoraRepository repo, FatoMapper fatoMapper) {
        this.repo = repo;
        this.fatoMapper = fatoMapper;
    }

    public CustoTotalDTO obterTotalGeral() {
        return new CustoTotalDTO(repo.totalGeral());
    }

    public List<CustoPorProjetoDTO> obterTotalPorProjeto() {
        // OTIMIZAÇÃO: Usar parallelStream() + method reference para conversão eficiente
        return repo.totalPorProjetoRaw().parallelStream()
                .map(this::convertToCustoPorProjetoDTO)
                .toList();
    }

    public List<CustoHorasPorDevDTO> obterTotalPorDev() {
        // OTIMIZAÇÃO: Usar parallelStream() + method reference para conversão eficiente
        return repo.totalPorDevRaw().parallelStream()
                .map(this::convertToCustoHorasPorDevDTO)
                .toList();
    }

    /**
     * OTIMIZAÇÃO: Method reference para evitar lambda repetitivo
     */
    private CustoPorProjetoDTO convertToCustoPorProjetoDTO(Object[] row) {
        return new CustoPorProjetoDTO((Long) row[0], (String) row[1], nz((Number) row[2]));
    }

    /**
     * OTIMIZAÇÃO: Method reference para evitar lambda repetitivo  
     */
    private CustoHorasPorDevDTO convertToCustoHorasPorDevDTO(Object[] row) {
        return new CustoHorasPorDevDTO((Long) row[0], (String) row[1], nz((Number) row[2]), nz((Number) row[3]));
    }

    public List<EvolucaoCustoDTO> obterEvolucao(String granularidade) {
        String g = (granularidade == null || granularidade.isBlank()) ? "mes" : granularidade.toLowerCase(Locale.ROOT);
        
        // OTIMIZAÇÃO: Usar parallelStream() + method references
        return switch (g) {
            case "ano" -> repo.evolucaoAnoRaw().parallelStream()
                    .map(this::convertToEvolucaoAno)
                    .toList();
            case "semana" -> repo.evolucaoSemanaRaw().parallelStream()
                    .map(this::convertToEvolucaoSemana)
                    .toList();
            case "dia" -> repo.evolucaoDiaRaw().parallelStream()
                    .map(this::convertToEvolucaoDia)
                    .toList();
            default -> repo.evolucaoMesRaw().parallelStream()
                    .map(this::convertToEvolucaoMes)
                    .toList();
        };
    }

    /**
     * OTIMIZAÇÃO: Method references para conversões específicas
     */
    private EvolucaoCustoDTO convertToEvolucaoAno(Object[] row) {
        return new EvolucaoCustoDTO(String.valueOf((Integer) row[0]), nz((Number) row[1]));
    }

    private EvolucaoCustoDTO convertToEvolucaoSemana(Object[] row) {
        return new EvolucaoCustoDTO(((Integer) row[0]) + "-S" + ((Integer) row[1]), nz((Number) row[2]));
    }

    private EvolucaoCustoDTO convertToEvolucaoDia(Object[] row) {
        return new EvolucaoCustoDTO(
                String.format("%04d-%02d-%02d", (Integer) row[0], (Integer) row[1], (Integer) row[2]),
                nz((Number) row[3])
        );
    }

    private EvolucaoCustoDTO convertToEvolucaoMes(Object[] row) {
        return new EvolucaoCustoDTO(
                monthLabel((Integer) row[1]) + "/" + ((Integer) row[0]),
                nz((Number) row[2])
        );
    }

    private static Long nz(Number n) {
        return n == null ? 0L : n.longValue();
    }

    private static String monthLabel(Integer m) {
        if (m == null) return "";
        return switch (m) {
            case 1 -> "Jan";
            case 2 -> "Fev";
            case 3 -> "Mar";
            case 4 -> "Abr";
            case 5 -> "Mai";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Ago";
            case 9 -> "Set";
            case 10 -> "Out";
            case 11 -> "Nov";
            case 12 -> "Dez";
            default -> String.valueOf(m);
        };
    }

     public List<FatoCustoHoraDTO> getAllEntitiesByFilter(DimProjeto dimProjeto, DimPeriodo dimPeriodo, DimDev dimDev)
    {
        return repo.findByDimProjetoAndDimPeriodoAndDimDev(dimProjeto, dimPeriodo, dimDev)
                .parallelStream()
                .map(fatoMapper::custoHoraToDTO)
                .collect(Collectors.toList());

    }

     public List<FatoCustoHoraDTO> getAllEntities() {
        return repo.findAll()
        .parallelStream()
        .map(fatoMapper::custoHoraToDTO)
        .collect(Collectors.toList());
     }

     public FatoCustoHoraDTO findById(Long id) {
        return repo.findById(id)
                .map(fatoMapper::custoHoraToDTO)
                .orElse(null);
    }

     public FatoCustoHoraDTO save(FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHora fatoCustoHora = fatoMapper.dtoToCustoHora(fatoCustoHoraDTO);
        FatoCustoHora savedEntity = repo.save(fatoCustoHora);
        return fatoMapper.custoHoraToDTO(savedEntity);
    }

    public FatoCustoHoraDTO update(Long id, FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHora existingEntity = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FatoCustoHora with ID " + id + " not found."));

        existingEntity.setDimProjeto(fatoCustoHoraDTO.getDimProjeto());
        existingEntity.setDimPeriodo(fatoCustoHoraDTO.getDimPeriodo());
        existingEntity.setDimDev(fatoCustoHoraDTO.getDimDev());
        existingEntity.setCusto(fatoCustoHoraDTO.getCusto());
        existingEntity.setHorasQuantidade(fatoCustoHoraDTO.getHorasQuantidade());

        FatoCustoHora updatedEntity = repo.save(existingEntity);
        return fatoMapper.custoHoraToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
