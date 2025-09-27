// src/main/java/com/neohorizon/api/service/FatoCustoHoraService.java
package com.neohorizon.api.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neohorizon.api.dto.CustoHorasPorDevDTO;
import com.neohorizon.api.dto.CustoPorProjetoDTO;
import com.neohorizon.api.dto.CustoTotalDTO;
import com.neohorizon.api.dto.EvolucaoCustoDTO;
import com.neohorizon.api.dto.FatoCustoHoraDTO;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.entity.FatoCustoHora;
import com.neohorizon.api.repository.FatoCustoHoraRepository;

@Service
public class FatoCustoHoraService {

    private final FatoCustoHoraRepository repo;

    @Autowired
    public FatoCustoHoraService(FatoCustoHoraRepository repo) {
        this.repo = repo;
    }

    public CustoTotalDTO obterTotalGeral() {
        return new CustoTotalDTO(repo.totalGeral());
    }

    public List<CustoPorProjetoDTO> obterTotalPorProjeto() {
        return repo.totalPorProjetoRaw().stream()
                .map(r -> new CustoPorProjetoDTO((Long) r[0], (String) r[1], nz((Number) r[2])))
                .collect(Collectors.toList());
    }

    public List<CustoHorasPorDevDTO> obterTotalPorDev() {
        return repo.totalPorDevRaw().stream()
                .map(r -> new CustoHorasPorDevDTO((Long) r[0], (String) r[1], nz((Number) r[2]), nz((Number) r[3])))
                .collect(Collectors.toList());
    }

    public List<EvolucaoCustoDTO> obterEvolucao(String granularidade) {
        String g = (granularidade == null || granularidade.isBlank()) ? "mes" : granularidade.toLowerCase(Locale.ROOT);
        return switch (g) {
            case "ano" -> repo.evolucaoAnoRaw().stream()
                    .map(r -> new EvolucaoCustoDTO(String.valueOf((Integer) r[0]), nz((Number) r[1])))
                    .collect(Collectors.toList());
            case "semana" -> repo.evolucaoSemanaRaw().stream()
                    .map(r -> new EvolucaoCustoDTO(((Integer) r[0]) + "-S" + ((Integer) r[1]), nz((Number) r[2])))
                    .collect(Collectors.toList());
            case "dia" -> repo.evolucaoDiaRaw().stream()
                    .map(r -> new EvolucaoCustoDTO(
                            String.format("%04d-%02d-%02d", (Integer) r[0], (Integer) r[1], (Integer) r[2]),
                            nz((Number) r[3])
                    ))
                    .collect(Collectors.toList());
            default -> repo.evolucaoMesRaw().stream()
                    .map(r -> new EvolucaoCustoDTO(
                            monthLabel((Integer) r[1]) + "/" + ((Integer) r[0]),
                            nz((Number) r[2])
                    ))
                    .collect(Collectors.toList());
        };
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
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

     public List<FatoCustoHoraDTO> getAllEntities() {
        return repo.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
     }

     public FatoCustoHoraDTO findById(Long id) {
        return repo.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

     public FatoCustoHoraDTO save(FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHora fatoCustoHora = convertToEntity(fatoCustoHoraDTO);
        FatoCustoHora savedEntity = repo.save(fatoCustoHora);
        return convertToDTO(savedEntity);
    }

    public FatoCustoHoraDTO update(Long id, FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHora existingEntity = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FatoCustoHora with ID " + id + " not found."));

        existingEntity.setDimProjeto(fatoCustoHoraDTO.getDimProjeto());
        existingEntity.setDimPeriodo(fatoCustoHoraDTO.getDimPeriodo());
        existingEntity.setDimDev(fatoCustoHoraDTO.getDimDev());
        existingEntity.setCusto(fatoCustoHoraDTO.getCusto());
        existingEntity.setHoras_quantidade(fatoCustoHoraDTO.getHoras_quantidade());

        FatoCustoHora updatedEntity = repo.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    private FatoCustoHoraDTO convertToDTO(FatoCustoHora entity) {
        if (entity == null) {
            return null;
        }
        FatoCustoHoraDTO dto = new FatoCustoHoraDTO();
        dto.setId(entity.getId());
        dto.setDimProjeto(entity.getDimProjeto());
        dto.setDimPeriodo(entity.getDimPeriodo());
        dto.setDimDev(entity.getDimDev());
        dto.setCusto(entity.getCusto());
        dto.setHoras_quantidade(entity.getHoras_quantidade());

        return dto;
    }

    private FatoCustoHora convertToEntity(FatoCustoHoraDTO dto) {
        if (dto == null) {
            return null;
        }
        FatoCustoHora entity = new FatoCustoHora();
        entity.setId(dto.getId());

        entity.setDimProjeto(dto.getDimProjeto());
        entity.setDimPeriodo(dto.getDimPeriodo());
        entity.setDimDev(dto.getDimDev());
        entity.setCusto(dto.getCusto());
        entity.setHoras_quantidade(dto.getHoras_quantidade());
        return entity;
    }
}
