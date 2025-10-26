// src/main/java/com/neohorizon/api/controller/FatoCustoHoraController.java
package com.neohorizon.api.controller.fato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.controller.comum.BaseController;
import com.neohorizon.api.dto.response.fato.FatoCustoHoraDTO;
import com.neohorizon.api.dto.response.metrica.CustoHorasPorDevDTO;
import com.neohorizon.api.dto.response.metrica.CustoPorProjetoDTO;
import com.neohorizon.api.dto.response.metrica.CustoTotalDTO;
import com.neohorizon.api.dto.response.metrica.EvolucaoCustoDTO;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.service.fato.FatoCustoHoraService;

@RestController
@RequestMapping("/fato-custo-hora")
public class FatoCustoHoraController extends BaseController {

    private final FatoCustoHoraService service;
    public FatoCustoHoraController(FatoCustoHoraService service) {
        this.service = service;
    }

    @GetMapping("/total")
    public ResponseEntity<CustoTotalDTO> getTotal() {
        return ok(service.obteinTotal());
    }

    @GetMapping("/total-por-projeto")
    public ResponseEntity<List<CustoPorProjetoDTO>> getTotalByProject() {
        return ok(service.obteinTotalByProject());
    }

    @GetMapping("/por-dev")
    public ResponseEntity<List<CustoHorasPorDevDTO>> getTotalByDev() {
        return ok(service.obteinTotalByDev());
    }

    @GetMapping("/evolucao")
    public ResponseEntity<List<EvolucaoCustoDTO>> getEvolution(
            @RequestParam(defaultValue = "mes") String granularidade) {
        return ok(service.obteinEvolution(granularidade));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FatoCustoHoraDTO>> getAllEntitiesByFilter(
            @RequestParam("projeto_id") Long projetoId,
            @RequestParam("periodo_id") Long periodoId,
            @RequestParam("dev_id") Long devId) {

        DimProjeto dimProjeto = new DimProjeto();
        DimPeriodo dimPeriodo = new DimPeriodo();
        DimDev dimDev = new DimDev();

        dimProjeto.setId(projetoId);
        dimPeriodo.setId(periodoId);
        dimDev.setId(devId);

        if (periodoId == 0) {
            dimPeriodo = null;
        }

        if (projetoId == 0) {
            dimProjeto = null;
        }

        if (devId == 0) {
            dimDev = null;
        }

        List<FatoCustoHoraDTO> fatoCustoHoraDTOs = service.getAllEntitiesByFilter(dimProjeto, dimPeriodo, dimDev);
        return ok(fatoCustoHoraDTOs);
    }

    @GetMapping
    public ResponseEntity<List<FatoCustoHoraDTO>> getAllEntities() {
        List<FatoCustoHoraDTO> fatoCustoHoraDTOs = service.getAllEntities();
        return ok(fatoCustoHoraDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoCustoHoraDTO> getFatoCustoHoraById(@PathVariable Long id) {
        FatoCustoHoraDTO fatoCustoHoraDTO = service.findById(id);
        if (fatoCustoHoraDTO == null) {
            return notFound();
        }
        return ok(fatoCustoHoraDTO);
    }

    @PostMapping
    public ResponseEntity<FatoCustoHoraDTO> create(@RequestBody FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHoraDTO createdEntity = service.create(fatoCustoHoraDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoCustoHoraDTO> updateFatoCustoHora(@PathVariable Long id,
            @RequestBody FatoCustoHoraDTO fatoCustoHoraDTO) {
        FatoCustoHoraDTO updatedEntity = service.update(id, fatoCustoHoraDTO);
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoCustoHora(@PathVariable Long id) {
        service.deleteById(id);
        return noContent();
    }

    @PostMapping("/recalcular")
    public ResponseEntity<Integer> recalc(
            @RequestParam(name = "data_inicio", required = false) LocalDate dataInicio,
            @RequestParam(name = "data_fim", required = false) LocalDate dataFim,
            @RequestParam(name = "dev_id", required = false) Long devId,
            @RequestParam(name = "projeto_id", required = false) Long projetoId) {

        int affected = service.recalcAndPersist(dataInicio, dataFim, devId, projetoId);
        return ok(affected);
    }
}
