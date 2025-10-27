// src/main/java/com/neohorizon/api/service/FatoCustoHoraService.java
package com.neohorizon.api.service.fato;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    public FatoCustoHoraService(FatoCustoHoraRepository repo, FatoMapper fatoMapper) {
        this.repo = repo;
        this.fatoMapper = fatoMapper;
    }

    public CustoTotalDTO obteinTotal() {
        return new CustoTotalDTO(repo.totalGeral());
    }

    public List<CustoPorProjetoDTO> obteinTotalByProject() {
        return repo.totalPorProjetoRaw().parallelStream()
                .map(this::convertToCostByProjetDTO)
                .toList();
    }

    public List<CustoHorasPorDevDTO> obteinTotalByDev() {
        return repo.totalPorDevRaw().parallelStream()
                .map(this::convertToCostHourByDevDTO)
                .toList();
    }

    private CustoPorProjetoDTO convertToCostByProjetDTO(Object[] row) {
        return new CustoPorProjetoDTO((Long) row[0], (String) row[1], nz((Number) row[2]));
    }

    private CustoHorasPorDevDTO convertToCostHourByDevDTO(Object[] row) {
        return new CustoHorasPorDevDTO((Long) row[0], (String) row[1], nz((Number) row[2]), nz((Number) row[3]));
    }

    public List<EvolucaoCustoDTO> obteinEvolution(String granularidade) {
        String g = (granularidade == null || granularidade.isBlank()) ? "mes" : granularidade.toLowerCase(Locale.ROOT);
        
        return switch (g) {
            case "ano" -> repo.evolucaoAnoRaw().parallelStream()
                    .map(this::convertToYearEvolution)
                    .toList();
            case "semana" -> repo.evolucaoSemanaRaw().parallelStream()
                    .map(this::convertToWeekEvolution)
                    .toList();
            case "dia" -> repo.evolucaoDiaRaw().parallelStream()
                    .map(this::convertToDayEvolution)
                    .toList();
            default -> repo.evolucaoMesRaw().parallelStream()
                    .map(this::convertToMonthEvolution)
                    .toList();
        };
    }

    private EvolucaoCustoDTO convertToYearEvolution(Object[] row) {
        return new EvolucaoCustoDTO(String.valueOf((Integer) row[0]), nz((Number) row[1]));
    }

    private EvolucaoCustoDTO convertToWeekEvolution(Object[] row) {
        return new EvolucaoCustoDTO(((Integer) row[0]) + "-S" + ((Integer) row[1]), nz((Number) row[2]));
    }

    private EvolucaoCustoDTO convertToDayEvolution(Object[] row) {
        return new EvolucaoCustoDTO(
                String.format("%04d-%02d-%02d", (Integer) row[0], (Integer) row[1], (Integer) row[2]),
                nz((Number) row[3])
        );
    }

    private EvolucaoCustoDTO convertToMonthEvolution(Object[] row) {
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

     public FatoCustoHoraDTO create(FatoCustoHoraDTO fatoCustoHoraDTO) {
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
