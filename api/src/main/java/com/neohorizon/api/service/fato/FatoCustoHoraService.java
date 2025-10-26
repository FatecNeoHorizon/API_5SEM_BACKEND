// src/main/java/com/neohorizon/api/service/FatoCustoHoraService.java
package com.neohorizon.api.service.fato;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;
import com.neohorizon.api.mapper.FatoMapper;
import com.neohorizon.api.repository.fato.FatoApontamentoHorasRepository;
import com.neohorizon.api.repository.fato.FatoCustoHoraRepository;
import com.neohorizon.api.repository.dimensao.DimDevRepository;
import com.neohorizon.api.repository.dimensao.DimPeriodoRepository;
import com.neohorizon.api.repository.dimensao.DimProjetoRepository;

@Service
public class FatoCustoHoraService {

    private static final Logger log = LoggerFactory.getLogger(FatoCustoHoraService.class);

    private final FatoCustoHoraRepository repo;
    private final FatoMapper fatoMapper;
    private final FatoApontamentoHorasRepository apontRepo;
    private final DimProjetoRepository projetoRepo;
    private final DimPeriodoRepository periodoRepo;
    private final DimDevRepository devRepo;

    public FatoCustoHoraService(FatoCustoHoraRepository repo, FatoMapper fatoMapper,
                                FatoApontamentoHorasRepository apontRepo,
                                DimProjetoRepository projetoRepo,
                                DimPeriodoRepository periodoRepo,
                                DimDevRepository devRepo) {
        this.repo = repo;
        this.fatoMapper = fatoMapper;
        this.apontRepo = apontRepo;
        this.projetoRepo = projetoRepo;
        this.periodoRepo = periodoRepo;
        this.devRepo = devRepo;
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

    public int recalcAndPersist(LocalDate fromDate, LocalDate toDate, Long devId, Long projetoId) {
        LocalDateTime from = (fromDate != null) ? fromDate.atStartOfDay() : LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime to = (toDate != null) ? toDate.atTime(23, 59, 59) : LocalDate.now().atTime(23, 59, 59);

        Stream<com.neohorizon.api.entity.fato.FatoApontamentoHoras> apontStream;
        if (devId != null) {
            apontStream = apontRepo.findByDevAndPeriodo(devId, from, to).stream();
        } else {
            apontStream = apontRepo.findByPeriodo(from, to).stream();
        }
        if (projetoId != null) {
            apontStream = apontStream.filter(a -> Objects.equals(a.getDimProjeto().getId(), projetoId));
        }

        record Key(Long devId, Long projetoId, Long periodoId) {}
        Map<Key, Double> horasPorChave = new HashMap<>();
        apontStream.forEach(a -> {
            Key k = new Key(a.getDimDev().getId(), a.getDimProjeto().getId(), a.getDimPeriodo().getId());
            horasPorChave.merge(k, a.getHorasTrabalhadas(), Double::sum);
        });

        int affected = 0;
        for (Map.Entry<Key, Double> e : horasPorChave.entrySet()) {
            Key k = e.getKey();
            Double horasDbl = e.getValue() == null ? 0.0 : e.getValue();
            BigDecimal horas = BigDecimal.valueOf(horasDbl);

            var dev = devRepo.findById(k.devId()).orElse(null);
            var projeto = projetoRepo.findById(k.projetoId()).orElse(null);
            var periodo = periodoRepo.findById(k.periodoId()).orElse(null);

            if (dev == null || projeto == null || periodo == null) {
                log.warn("Ignorando upsert faltando chave: dev={}, projeto={}, periodo={}", k.devId(), k.projetoId(), k.periodoId());
                continue;
            }

            BigDecimal tarifa = dev.getCustoHora() == null ? BigDecimal.ZERO : dev.getCustoHora();
            BigDecimal custo = tarifa.multiply(horas);

            try {
                var existentes = repo.findByDimProjetoAndDimPeriodoAndDimDev(projeto, periodo, dev);
                FatoCustoHora target = existentes == null || existentes.isEmpty() ? FatoCustoHora.builder()
                        .dimProjeto(projeto)
                        .dimPeriodo(periodo)
                        .dimDev(dev)
                        .build()
                        : existentes.get(0);

                target.setHorasQuantidade(horas);
                target.setCusto(custo);

                repo.save(target);
                affected++;
                log.info("Upsert custo-hora: dev={} projeto={} periodo={} horas={} custo={}",
                        dev.getId(), projeto.getId(), periodo.getId(), horas, custo);
            } catch (Exception ex) {
                log.error("Falha ao persistir custo-hora: dev={} projeto={} periodo={}: {}",
                        k.devId(), k.projetoId(), k.periodoId(), ex.getMessage(), ex);
            }
        }
        return affected;
    }

    public int recalcForTriplet(Long devId, Long projetoId, Long periodoId) {
        if (devId == null || projetoId == null || periodoId == null) return 0;

        LocalDateTime from = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime to = LocalDate.now().atTime(23, 59, 59);
        var apont = apontRepo.findByDevAndPeriodo(devId, from, to).stream()
                .filter(a -> Objects.equals(a.getDimProjeto().getId(), projetoId))
                .filter(a -> Objects.equals(a.getDimPeriodo().getId(), periodoId))
                .toList();

        if (apont.isEmpty()) return 0;

        double totalHoras = apont.stream().mapToDouble(FatoApontamentoHoras::getHorasTrabalhadas).sum();
        var dev = devRepo.findById(devId).orElse(null);
        var projeto = projetoRepo.findById(projetoId).orElse(null);
        var periodo = periodoRepo.findById(periodoId).orElse(null);
        if (dev == null || projeto == null || periodo == null) return 0;

        BigDecimal horas = BigDecimal.valueOf(totalHoras);
        BigDecimal tarifa = dev.getCustoHora() == null ? BigDecimal.ZERO : dev.getCustoHora();
        BigDecimal custo = tarifa.multiply(horas);

        var existentes = repo.findByDimProjetoAndDimPeriodoAndDimDev(projeto, periodo, dev);
        FatoCustoHora target = existentes == null || existentes.isEmpty() ? FatoCustoHora.builder()
                .dimProjeto(projeto)
                .dimPeriodo(periodo)
                .dimDev(dev)
                .build()
                : existentes.get(0);

        target.setHorasQuantidade(horas);
        target.setCusto(custo);
        repo.save(target);
        log.info("Upsert (triplo) custo-hora: dev={} projeto={} periodo={} horas={} custo={}",
                devId, projetoId, periodoId, horas, custo);
        return 1;
    }
}
