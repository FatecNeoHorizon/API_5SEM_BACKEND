package com.neohorizon.api.controller.fato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.service.fato.FatoApontamentoHorasService;

@RestController
@RequestMapping("/fato-apontamento-horas")
public class FatoApontamentoHorasController extends BaseController {

    private final FatoApontamentoHorasService fatoApontamentoHorasService;

    public FatoApontamentoHorasController(FatoApontamentoHorasService fatoApontamentoHorasService) {
        this.fatoApontamentoHorasService = fatoApontamentoHorasService;
    }

    @GetMapping
    public ResponseEntity<List<FatoApontamentoHorasDTO>> getAllEntities() {
        List<FatoApontamentoHorasDTO> fatoApontamentoHorasDTOs = fatoApontamentoHorasService.getAllEntities();
        return ok(fatoApontamentoHorasDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoApontamentoHorasDTO> getFatoApontamentoHorasById(@PathVariable Long id) {
        FatoApontamentoHorasDTO fatoApontamentoHorasDTO = fatoApontamentoHorasService.findById(id);
        if (fatoApontamentoHorasDTO == null) {
            return notFound();
        }
        return ok(fatoApontamentoHorasDTO);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<FatoApontamentoHorasDTO>> getApontamentosByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<FatoApontamentoHorasDTO> apontamentos = fatoApontamentoHorasService.findByPeriodo(dataInicio, dataFim);
        return ok(apontamentos);
    }

    @PostMapping
    public ResponseEntity<FatoApontamentoHorasDTO> addFatoApontamentoHoras(@RequestBody FatoApontamentoHorasDTO fatoApontamentoHorasDTO) {
        FatoApontamentoHorasDTO createdEntity = fatoApontamentoHorasService.create(fatoApontamentoHorasDTO);
        return created(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoApontamentoHorasDTO> updateFatoApontamentoHoras(@PathVariable Long id, @RequestBody FatoApontamentoHorasDTO fatoApontamentoHorasDTO) {
        FatoApontamentoHorasDTO updatedEntity = fatoApontamentoHorasService.update(id, fatoApontamentoHorasDTO);
        if (updatedEntity == null) {
            return notFound();
        }
        return ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoApontamentoHoras(@PathVariable Long id) {
        fatoApontamentoHorasService.deleteById(id);
        return noContent();
    }
}
